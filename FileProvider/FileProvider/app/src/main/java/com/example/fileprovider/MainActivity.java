package com.example.fileprovider;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.graphics.Bitmap.Config.ARGB_8888;
import static java.lang.Boolean.TRUE;
import static java.lang.Boolean.logicalAnd;
import static java.lang.System.out;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    File imageFile;
    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File createdMedia = null;
        Uri uri = null;

        //check if we can write and then create a new file
        if(isExternalStorageWritable()) {
            Log.i(TAG, "external storage is writable");

            try {
                createdMedia = createMediaFile();
                Log.i(TAG, "onCreate: "+createdMedia.getAbsolutePath());

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "external storage is not writable");
        }

        //here we use fileprovider to grant permission to other applications.
        if(createdMedia != null) {
            uri = FileProvider.getUriForFile(this,"com.example.fileprovider.fileprovider", createdMedia);
        }

        //create a new intent that shares the file with an application that handles (Action view)
        Intent intent = ShareCompat.IntentBuilder.from(this)
                .setStream(uri) // uri from FileProvider
                .setType("text/html")
                .getIntent()
                .setAction(Intent.ACTION_VIEW) //Change if needed
                .setDataAndType(uri, "image/*")
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);

    }

    //make a file we can overwrite and then overwrite with our bmw_bike.png.
    //note I am supressing the bitmap compress requirement for an asynchronous task)
    private File createMediaFile() throws IOException {

        //make a folder for the file
        File imagePath = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "external_files");
        Log.i(TAG, "onCreate: "+imagePath.toString());
        imagePath.mkdir();

        // make a temporary image file and copy in some content.
        String exState = Environment.getExternalStorageState();
        if (exState.equals(Environment.MEDIA_MOUNTED))
        {
            //create a temporary image file.
            imageFile = File.createTempFile("bmw_bike",".png", imagePath);
            //replace the image with the one we get from our drawable directory
            final Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.bmw_bike);
            FileOutputStream fos = new FileOutputStream(imageFile);
            bm.compress(Bitmap.CompressFormat.PNG,80,fos);
            out.flush();
            out.close();
        }

        Log.i(TAG, "createImageFile: "+ imageFile.getAbsolutePath());
        return imageFile;
    }


    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
}
