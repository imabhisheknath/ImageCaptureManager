# ImageCaptureManager
## ImageCaptureManager
----------------------
How To Use
----------

```xml
//Add Permission

<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.CAMERA" />

```






```java


//Add to your Module:app
compile 'com.github.imabhisheknath:ImageCaptureManager:v1.0-beta'


 // Initialize  ImageManager
  
  //here "123" is directory name where image will saved.
  
    ImageManager imageManager = new ImageManager(MainActivity.this, "123") {
            @Override
            public void onSuccess(String imagepath) {
                Log.d("mypath", imagepath);
              
              //here imagepath is image  saved url

            }

            @Override
            public void onError(String msg) {
                Log.d("myerror", msg);
            }
        };
        
        //start capture image by
        
           imageManager.StartCamera();
        

```
