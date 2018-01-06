package com.horses.camera.library;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Date;

import app.horses.camera.CameraManager;
import app.horses.camera.CallbackManager;
import app.horses.camera.view.CallbackView;

public class MainActivity extends AppCompatActivity implements CallbackView {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Context context;
    private CallbackManager callbackManager = new CallbackManager();

    private ImageView image;

    private boolean QRCodeEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;

        // Default Use
        // CameraManager.openCamera(this);

        File root = Environment.getExternalStorageDirectory();
        File dirBase=new File(root, "horsesCamera");

        // Custom Path
        CameraManager.openCamera(this,dirBase.getPath());

        // Custom path and filename without extension
        // CameraManager.openCamera(this,dirBase.getPath(),"MyPhoto_"+new Date().getTime());

        // Open QR Code Scan with custom layout
        QRCodeEnabled = true;
        //CameraManager.openQRCamera(this, R.layout.qr_layout_test);

        //Open from gallery
        //CameraManager.openGallery(this);

        callbackManager.setCallback(this);

        image = (ImageView) findViewById(R.id.image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraManager.openCamera(MainActivity.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(this,requestCode, resultCode, data);
    }

    @Override
    public void successCamera(String path) {
        if(!QRCodeEnabled) {
            Log.i(TAG, "successCamera: " + path);

            path = "file:///" + path;

            Picasso.with(context).load(path).into(image);
        } else {
            Toast.makeText(context, "SCANNED QR: " + path, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void errorCamera() {
        Log.i(TAG, "errorCamera");

    }

    @Override
    public void cancelCamera() {
        Log.i(TAG, "cancelCamera");

    }
}
