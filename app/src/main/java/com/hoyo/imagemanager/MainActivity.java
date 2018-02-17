package com.hoyo.imagemanager;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.hoyo.imagemanager.v2.MiniImageManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Version 1 Example

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


        // Version 2 Example

        /*new MiniImageManager(MainActivity.this) {
            @Override
            public void onImageCaptured(String imagePath) {

            }
        }.setupCameraParameters(640,480,"456")
                .setCameraListener(errorListener)
                .takePicture();
*/
    }

    MiniImageManager.ImageManagerErrorListener errorListener = new MiniImageManager.ImageManagerErrorListener() {
        @Override
        public void onError(String msg) {

        }
    };
}
