package com.app.securehaven.cam;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class CameraHandler  {
    String encodedImage;  //The image will be converted to base64 string, so that it can be sent as attachment
    volatile boolean picReady;
    Bitmap bmp;

    CameraHandler(){
        encodedImage="PlaceHolder";
        picReady=false;  //This will turn true once the encoded string is ready
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    String takePic(){
        Camera camera=Camera.open(1); //1 indicates front camera, open instance of camera
        try{
            camera.setPreviewTexture(new SurfaceTexture(10));
        }catch (Exception e){
            Log.e("SecHaven.CameraHandler",e.getMessage());
        }
        camera.startPreview();

        camera.takePicture(null, null, (data, camera1) -> {
            Log.d("SecHaven.CameraHandler","onPictureCalled");
            //
            bmp = BitmapFactory.decodeByteArray(data, 0, data.length); //Convert raw byte array to Bitmap format
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream); //Write the Bitmap to ByteArrayOutputStream
            byte[] byteArray = byteArrayOutputStream .toByteArray();  //Get byte array from ByteArrayOutputStream
            //
            encodedImage= Base64.getEncoder().encodeToString(byteArray);   //Convert the byte array to Base64 encoded string
            camera1.stopPreview();                                         //Stop the preview
            Log.d("EncodedImage","starthere#"+encodedImage+"###ENDED");
            picReady=true; //Indicates that encoded string is ready
            camera1.release();
        });
        while (!picReady){}
        camera.release();
        return encodedImage;
    }
}