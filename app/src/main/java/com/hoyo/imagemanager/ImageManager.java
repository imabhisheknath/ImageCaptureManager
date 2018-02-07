package com.hoyo.imagemanager;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public abstract class ImageManager {

    private Context context;

    private Camera mCamera;

    private String case_id;

    public ImageManager(Context context, String case_id) {
        this.context = context;
        this.case_id = case_id;

    }


    public void StartCamera() {
        Camera.Parameters parameters;

        mCamera = Camera.open();
        SurfaceView sv = new SurfaceView(context);


        try {
            mCamera.setPreviewDisplay(sv.getHolder());
            parameters = mCamera.getParameters();
            parameters.setPreviewSize(640, 480);
            parameters.setPictureSize(640,
                    480);
            mCamera.setParameters(parameters);
            mCamera.startPreview();
            mCamera.takePicture(null, null, mCall);
        } catch (IOException e) {
            e.printStackTrace();
            onError(e.getMessage());
        }

    }


    private void Close() {
        if (null == mCamera)
            return;
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
        Log.i("CAM", " closed");
    }


    private Camera.PictureCallback mCall = new Camera.PictureCallback() {

        public void onPictureTaken(final byte[] data, Camera camera) {

            FileOutputStream outStream = null;
            try {

                String intStorageDirectory = context.getFilesDir().toString();
                File folder = new File(intStorageDirectory, case_id + "/" + "Image");

                if (!folder.exists()) {
                    boolean wassuccessful = folder.mkdirs();

                    if (!wassuccessful) {
                        onError("Error while creating directory");
                    }

                }

                SimpleDateFormat simpleDateFormat =
                        new SimpleDateFormat("MMddyyhhmss");
                String currentDateTimeString = simpleDateFormat.format(new Date());
                String tar = folder + "/" + currentDateTimeString + ".jpg";

                outStream = new FileOutputStream(tar);
                outStream.write(data);
                outStream.close();
                Close();

                onSuccess(tar);


            } catch (IOException e) {
                Log.d("CAM", e.getMessage());
                onError(e.getMessage());
            }
        }
    };

    public abstract void onSuccess(String imagepath);

    public abstract void onError(String msg);


    private Camera.Size getSmallestPictureSize(Camera.Parameters parameters) {
        Camera.Size result = null;

        for (Camera.Size size : parameters.getSupportedPictureSizes()) {


            if (result == null) {
                result = size;
            } else {
                int resultArea = result.width * result.height;
                int newArea = size.width * size.height;

                if (newArea < resultArea) {
                    result = size;
                }
            }
        }
        Log.d("mysize", result.width + "\t" + result.height);
        return (result);
    }


    private Camera.Size maximumpictureSize(Camera.Parameters param) {
        List<Camera.Size> sizes = param.getSupportedPictureSizes();
        Camera.Size size = sizes.get(0);
        for (int i = 0; i < sizes.size(); i++) {
            if (sizes.get(i).width > size.width)
                size = sizes.get(i);
        }

        return (size);
    }


}