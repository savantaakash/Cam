package com.example.cameraandgallery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_CODE =1000 ;
    private static final int IMAGE_CAPTURE_CODE =1001 ;
    Button capture_img;
    ImageView imgView;

    Uri image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



         imgView = findViewById(R.id.imgView);
         capture_img=findViewById(R.id.capture_img);

         capture_img.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 //if system is >= than marshmallaw
                 if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                     if (checkSelfPermission(Manifest.permission.CAMERA)==PackageManager.PERMISSION_DENIED||
                             checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
                         //permission not enabled request it
                         String[] permission = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        //Show pop up to request permission
                        requestPermissions(permission,PERMISSION_CODE);

                     }
                     else {
                         //permission already granted
                         openCamera();
                     }
                 }
                 else{
                     //system os< marshmallow
                    openCamera();
                 }
             }
         });


    }
    private void openCamera(){

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"New Pick");
        values.put(MediaStore.Images.Media.DESCRIPTION,"From Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        //Camera intent
        Intent cameraIntent =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent,IMAGE_CAPTURE_CODE);



    }
//handling pemission results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    switch (requestCode){
        case PERMISSION_CODE:{
            if (grantResults.length>0 && grantResults[0]==
            PackageManager.PERMISSION_GRANTED){

                //permission from popup was granted
                openCamera();

            }
            else {
               //permission from popup was denied
                Toast.makeText(this, "permission denied......", Toast.LENGTH_SHORT).show();
            }
        }
    }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
       //call when image was captured from camera

        if (requestCode == RESULT_OK){
            //set captured image to image view
            imgView.setImageURI(image_uri);
        }

    }

}
