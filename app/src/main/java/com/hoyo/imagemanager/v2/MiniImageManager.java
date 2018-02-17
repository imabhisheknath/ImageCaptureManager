package com.hoyo.imagemanager.v2;

import android.content.Context;
import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.SurfaceView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public abstract class MiniImageManager {

    private Context mContext;
    private Camera mCamera;
    private Parameters mParameters;

    public abstract void onSuccess(String imagepath);
    public abstract void onError(String msg);

    public class Parameters {
        private int height;
        private int width;
        private String folderName;

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }

        public Parameters setDimensions(int width, int height){
            this.width = width;
            this.height = height;
            return this;
        }

        public String getFolderName() {
            return folderName;
        }

        public Parameters setFolderName(@NonNull String folderName) {
            this.folderName = folderName;
            return this;
        }
    }

    public MiniImageManager(Context context) {
        this.mContext = context;
    }

    public MiniImageManager setupCameraParameters(String folderName){
        if(mParameters == null) {
            mParameters = getDefaultParameters();
        }
        mParameters.setFolderName(folderName);
        return this;
    }

    public MiniImageManager setupCameraParameters(int width, int height, @NonNull String folderName) {
        return setupCameraParameters(new Parameters().setDimensions(width,height).setFolderName(folderName));
    }

    public MiniImageManager setupCameraParameters(@NonNull Parameters imageParams) {
        this.mParameters = imageParams;
        return this;
    }

    private MiniImageManager initCamera() throws ImageManagerException {

        if(mParameters==null){
            mParameters = getDefaultParameters();
        }

        Camera.Parameters cameraParameters;
        mCamera = null;

        try {
            mCamera = Camera.open();
        } catch (Exception e){
            // ToDo - handle it here properly
            throw new ImageManagerException("Unable to Open Camera!\n"+e.toString());
        }

        SurfaceView sv = new SurfaceView(mContext);

        try {
            mCamera.setPreviewDisplay(sv.getHolder());
            cameraParameters = mCamera.getParameters();
            // parameters.setPreviewSize(640, 480);
            cameraParameters.setPictureSize(mParameters.getWidth(),
                    mParameters.getHeight());
            mCamera.setParameters(cameraParameters);
        } catch (Exception e) {
            e.printStackTrace();
            //onError(e.getMessage());
            close();
            throw new ImageManagerException("Unable to Set Camera Parameters!\n"+e.toString());
        }

        return this;
    }

    public MiniImageManager takePicture(@NonNull String folderName){
        this.setupCameraParameters(folderName);
        return takePicture();
    }

    public MiniImageManager takePicture() {

        try {
            this.initCamera();
        } catch (ImageManagerException e){
            onError(e.toString());
            return this;
        }

        try {
            mCamera.startPreview();
            mCamera.takePicture(null, null, mCall);
        } catch (Exception e){
            close();
            onError("Unable to take Picture!\n"+e.toString());
            return this;
        }

        return this;
    }


    private void close() {
        if (null == mCamera) {
            Log.w("ImageManager","Closing Camera on Null Camera Object");
            return;
        }

        try {
            mCamera.stopPreview();
        } catch (Exception e){
            Log.w("ImageManager","Stopping Preview of Camera which is already Stopped!\n"+e.toString());
        }

        try {
            mCamera.release();
        } catch (Exception e){
            Log.w("ImageManager","Release Camera Failed. May be Already Released!\n"+e.toString());
        }
        mCamera = null;

        Log.i("CAM", " closed");
    }

    public void onPause(){
        close();
    }

    private Camera.PictureCallback mCall = new Camera.PictureCallback()  {

        public void onPictureTaken(final byte[] data, Camera camera) {

            try {
                String filePath = getFilePath();

                if (filePath == null) {
                    onError("Error While Creating Image File");
                    return;
                }

                try {
                    FileOutputStream outStream = new FileOutputStream(filePath);
                    outStream.write(data);
                    outStream.close();
                    close();
                    onSuccess(filePath);

                } catch (IOException e) {
                    Log.d("CAM", e.getMessage());
                    onError(e.getMessage());
                }

            }catch (ImageManagerException e){
                onError(e.toString());
            }
        }
    };


    public Parameters getDefaultParameters() {
        // ToDo Update this 640x480 with smallest Size
        return new Parameters().setDimensions(640,480).setFolderName(getCurrentDataTimeString());
    }

    public String getCurrentDataTimeString(){
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("MMddyyhhmss", Locale.getDefault());
        return simpleDateFormat.format(new Date());
    }

    public String getFilePath() throws ImageManagerException {

        try {
            String intStorageDirectory = mContext.getFilesDir().toString();
            File folder = new File(intStorageDirectory, mParameters.getFolderName() + "/" + "Image");
            if (!folder.exists()) {
                if (!folder.mkdirs()) {
                    return null;
                }
            }
            return folder + "/" + getCurrentDataTimeString() + ".jpg";
        } catch (Exception e){
            throw new ImageManagerException("Unable to Create File Path!\n"+e.toString());
        }
    }


   /* // FOR FUTURE REFERENCE

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
            Log.d("mysize", result.width + "\t" + result.height);
        }

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
    }*/

}