package com.foc.libs.blurred.bottom.blurredbottomnavigation;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;


import com.hoko.blur.HokoBlur;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BlurLayout extends FrameLayout {


    String TAG = "BlurLayout";


    //layout width
    private int w = -1;
    //layout height
    private int h = -1;
    //blur value
    private float blurValue = 1;
    //blur item(s)
    private List<BlurItem> blurItem = new ArrayList<>();

    //check blurring is running
    private boolean isBlurring;

    private float avg = 0.0f;
    private int c = 0;


    public BlurLayout(@NonNull Context context) {
        super(context);
        init(null);

    }

    public BlurLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public BlurLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {


        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //get width , height
                w = getWidth();
                h = getHeight();
                //remove observer
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });


        //get attrs
        getAttrs(attrs);
    }


    private void getAttrs(@Nullable AttributeSet attrs) {

        if (attrs == null)
            return;

        //get attrs
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BlurLayout);

        //get blur value
        blurValue = typedArray.getFloat(R.styleable.BlurLayout_blurValue, blurValue);

        //recycle
        typedArray.recycle();
    }

    /**
     * Start blurring the layout
     */
    private void startBlurring() {

        //check blur item exists
        if (blurItem != null)
            new Handler(Looper.getMainLooper()).post(this::blur);

    }

    /**
     * blur layout and send it to blur item
     */
    private void blur() {


        //check blurring is allowed
        if (!isBlurring)
            return;

        //####time
        long startT = System.currentTimeMillis();

        for (BlurItem item : blurItem) {
            //get bitmap of blur item
            Bitmap bitmap = getItemBitmap(item);

            //blur bitmap
            Bitmap blurredBitmap = blurWithHokoBlur(bitmap);

            //show blur bitmap
            item.show(blurredBitmap);
        }

        //####show fps
        long endT = (System.currentTimeMillis() - startT);
        int fps = (int) (1000f / endT);
        c++;
        avg += fps;
        Log.i(TAG, "blur layout time = " + endT + "\tFPS= " + fps + "\tAVG FPS= " + (avg / c));

        //again
        new Handler(Looper.getMainLooper()).postDelayed(this::blur, 0);
    }


    //items bound
    private int l = -1, t = -1, r = -1, b = -1;

    private Bitmap getItemBitmap(BlurItem item) {

        if (item == null || w == -1 || h == -1)
            return null;

        //get item bounds
        l = item.getLeft();
        t = item.getTop();
        r = item.getRight();
        b = item.getBottom();

        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.clipRect(new RectF(l, t, r, b), Region.Op.REPLACE);
        draw(canvas);
        return Bitmap.createBitmap(bitmap, l, t, r - l, b - t);
    }

    /**
     * Blur Bitmap with render script
     *
     * @return Bitmap|null
     */
    private Bitmap blurBitmap(Bitmap input) {

        //check input
        if (input == null)
            return null;

        try {

            RenderScript rsScript = RenderScript.create(getContext());
            Allocation alloc = Allocation.createFromBitmap(rsScript, input);

            ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rsScript, Element.U8_4(rsScript));
            blur.setRadius(blurValue);
            blur.setInput(alloc);

            Bitmap result = Bitmap.createBitmap(input.getWidth(), input.getHeight(), Bitmap.Config.ARGB_8888);
            Allocation outAlloc = Allocation.createFromBitmap(rsScript, result);

            blur.forEach(outAlloc);
            outAlloc.copyTo(result);

            input.recycle();

            rsScript.destroy();


            return result;
        } catch (Exception e) {
            return input;
        }
    }

    /**
     * Blur Bitmap with Hoko blur lib
     *
     * @return Bitmap|null
     */
    private Bitmap blurWithHokoBlur(Bitmap bitmap) {
        if (bitmap == null)
            return null;
        return HokoBlur.with(getContext())
                .scheme(HokoBlur.SCHEME_NATIVE)
                .mode(HokoBlur.MODE_STACK)
                .radius((int) blurValue)
                .processor()
                .blur(bitmap);
    }


    /**
     * Attach your blur item layout like bottom navigation layout for blurring
     * You must call apply() at the end
     *
     * @param blurItem
     */
    public BlurLayout attachItem(BlurItem blurItem) {
        this.blurItem.add(blurItem);
        return this;
    }


    /**
     * Check if blurring is running
     *
     * @return
     */
    public boolean isBlurring() {
        return isBlurring;
    }

    /**
     * Disable blurring
     */
    public void disableBlurring() {
        isBlurring = false;
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            //resume
            if (visibility == VISIBLE) {
                isBlurring = true;
                startBlurring();
            }
            //pause
            else
                isBlurring = false;

        }//end check android version
    }

    @Override
    public void onVisibilityAggregated(boolean isVisible) {
        super.onVisibilityAggregated(isVisible);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {


            //resume
            if (isVisible) {
                isBlurring = true;
                startBlurring();
            }
            //pause
            else
                isBlurring = false;

        }//end check android version

    }
}
