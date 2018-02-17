package com.hoyo.imagemanager.v2;

/**
 * Created by Praba on 2/17/2018.
 */
public class ImageManagerException extends Exception {

    public ImageManagerException(String message){
        super("ImageManagerException: "+message);
    }
}
