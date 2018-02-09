package com.hoyo.imagemanager;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageManager imageManager = new ImageManager(MainActivity.this) {
            @Override
            public void onSuccess(String imagepath) {
                Log.d("mypath", imagepath);


            }

            @Override
            public void onError(String msg) {
                Log.d("myerror", msg);
            }
        };


        imageManager.StartCamera("123");
    }
}
