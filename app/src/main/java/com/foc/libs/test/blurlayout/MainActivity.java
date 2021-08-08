package com.foc.libs.test.blurlayout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.foc.libs.blurred.bottom.blurredbottomnavigation.BlurItem;
import com.foc.libs.blurred.bottom.blurredbottomnavigation.BlurLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BlurLayout blurLayout = findViewById(R.id.blurLayout);
        BlurItem blurItem = findViewById(R.id.blurItem);
        blurLayout.attachItem(blurItem);
    }
}