package com.foc.libs.blurred.bottom.blurredbottomnavigation;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

public class BlurItem extends CardView {


    //background image view
    private ImageView backImage;

    public BlurItem(@NonNull Context context) {
        super(context);
        init(null);

    }

    public BlurItem(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }


    public BlurItem(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {

        //create background image
        backImage = new ImageView(getContext());
        backImage.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        backImage.setScaleType(ImageView.ScaleType.FIT_XY);
        this.addView(backImage);

        if (attrs == null)
            return;

    }

    public void show(Bitmap bitmap) {
        if (bitmap != null)
            backImage.setImageBitmap(bitmap);
    }


}
