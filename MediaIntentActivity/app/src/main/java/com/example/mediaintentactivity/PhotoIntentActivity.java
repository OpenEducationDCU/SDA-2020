package com.example.mediaintentactivity;

/*
	Copyright [2019] [DCU.ie]

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
 */


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * This class takes photos and video using camera intents, saves media to package data in external storage
 * Then Displays photos and video in UI
 *  <h3>References</h3>
 *  https://www.techotopia.com/index.php/Video_Recording_and_Image_Capture_using_Camera_Intents_-_An_Android_Studio_Example<br>
 *  http://developer.android.com/shareables/training/PhotoIntentActivity.zip<br>
 *  https://developer.android.com/training/camera/photobasics<br>
 *  https://developer.android.com/training/camera/videobasics
 *
 * @author Adapted from various sources see references above.
 * @author chris coughlan 2019
 * @version 1.0 2019
 */
public class PhotoIntentActivity extends AppCompatActivity {

    //setting final variables
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_THUMB_CAPTURE = 2;
    private static final int REQUEST_VIDEO_CAPTURE = 3;
    static final int REQUEST_TAKE_PHOTO = 1;
    private static final String TAG = "PhotoIntentActivity";

    //initialising widgets (views) for use in this class
    private static boolean mSetSize = false;
    private ImageView mPictureTaken;
    private ImageView mSetPictureTaken;
    private VideoView mVideoViewer;

    //same media file is shared by the createMediaFile and the galleryAddPic methods
    private File mMediaFile = null;

    //values are  used to pass the thumbnail variable to the lifecycle.
    private static final String BITMAP_STORAGE_KEY = "imageKey";
    private static final String THUMBVIEW_VISIBILITY_STORAGE_KEY = "ThumbVisible";
    private Bitmap mSmallBitMap;

    //values are used to pass the path to the larger files into the lifecycle.
    private static final String PATH_STORAGE_KEY = "PathKey";
    private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "ImageVisible";
    private String mCurrentPhotoPath;

    //values are used to pass the path to the video file into the lifecycle.
    private static final String VIDEO_STORAGE_KEY = "VideoKey";
    private static final String VIDEOVIEW_VISIBILITY_STORAGE_KEY = "VideoVisible";
    private Uri mVideoURI;

    /**
     * Overrides onCreate lifecycle method, inflates the UI from photo_intent.xml and sets the views (widgets for this activity)
     * Sets Four Button onClick listeners, three image and one video capture button in the UI
     * On each button it adds a {@link #setBtnListenerOrDisable} method that disables the button if the intent cannot be called
     * Each button opens one of the dispatch* methods and the {@link #setVisibility}
     * @param savedInstanceState bundles the activity state for the application
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_intent);

        mPictureTaken = findViewById(R.id.imageView);
        mSetPictureTaken = findViewById(R.id.setView);
        mVideoViewer = findViewById(R.id.videoView);
        final Button takeBigPicture = findViewById(R.id.bigPicButton);
        final Button takeSmallPicture = findViewById(R.id.smallPicButton);
        final Button takeSetPicture = findViewById(R.id.setButton);
        final Button takeVideo = findViewById(R.id.vidButton);

        //what to do if someone clicks a full picture
        Button.OnClickListener mTakeBigPicture =
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setVisibility(1);
                        dispatchTakePictureIntent();
                    }
                };

        //check the image capture is available if not disable the button
        setBtnListenerOrDisable(
                takeBigPicture,
                mTakeBigPicture,
                MediaStore.ACTION_IMAGE_CAPTURE
        );


        //what to do if someone clicks a thumbnail
        Button.OnClickListener mTakeSmallPicture =
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setVisibility(1);
                        dispatchTakeThumbIntent();
                    }
                };

        //check the image capture is available if not disable the button
        setBtnListenerOrDisable(
                takeSmallPicture,
                mTakeSmallPicture,
                MediaStore.ACTION_IMAGE_CAPTURE
        );

        //what to do if someone takes a picture destined for a ImageView with a set size value
        Button.OnClickListener mTakeSetPicture =
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setVisibility(2);
                        dispatchTakePictureIntent();
                    }
                };

        //check the image capture is available if not disable the button
        setBtnListenerOrDisable(
                takeSetPicture,
                mTakeSetPicture,
                MediaStore.ACTION_IMAGE_CAPTURE
        );


        //what to do if someone selects make a video
        Button.OnClickListener mTakeVideo =
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setVisibility(3);
                        dispatchTakeVideoIntent();
                    }
                };

        //check the video capture is available if not disable the button
        setBtnListenerOrDisable(
                takeVideo,
                mTakeVideo,
                MediaStore.ACTION_VIDEO_CAPTURE
        );
    }

    /**
     * This method contains a switch that controls the visibility of the views (Image or video) in the UI
     * @param state integer controlling the state of visibility on views in photo_intent.xml UI
     */
    public void setVisibility(int state)
    {
        switch (state) {
            case 1:
                mSetSize = false;
                mSetPictureTaken.setVisibility(View.INVISIBLE);
                mVideoViewer.setVisibility(View.INVISIBLE);
                mPictureTaken.setVisibility(View.VISIBLE);
                break;
            case 2:
                mSetSize = true;
                mPictureTaken.setVisibility(View.INVISIBLE);
                mVideoViewer.setVisibility(View.INVISIBLE);
                mSetPictureTaken.setVisibility(View.VISIBLE);
                break;
            case 3:
                mSetSize = false;
                mPictureTaken.setVisibility(View.INVISIBLE);
                mSetPictureTaken.setVisibility(View.INVISIBLE);
                mVideoViewer.setVisibility(View.VISIBLE);
                break;
        }

    }

    /**
     * This Method creates and returns two file types with date-time added to the unique name value,
     * it is called by the {@link #dispatchTakePictureIntent} and the {@link #dispatchTakeVideoIntent}
     * @param requestCode to tell it what type of file to create
     * @return image file object that can be used by the Camera intent to populate when it takes images/videos
     * @throws IOException
     */
    private File createMediaFile(int requestCode) throws IOException {

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        //Return either a video file or an image file depending on the requestCode.
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE: {
                String imageFileName = "JPEG_" + timeStamp + "_";
                String exState = Environment.getExternalStorageState();
                if (exState.equals(Environment.MEDIA_MOUNTED))
                {
                    //save it to the pictures directory in the package installed with the application on external storage.
                    File extStorageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    mMediaFile = File.createTempFile(
                            imageFileName,  /* prefix */
                            ".jpg",  /* suffix */
                            extStorageDir /* directory */
                    );
                } else {
                    //we should do something here if there is no storage
                    //for example use the local directory where the application is installed instead
                    Log.e(TAG, "createImageFile: External Storage is not available");
                }

                // Save a file: path for use with ACTION_VIEW intents
                if(mMediaFile != null){mCurrentPhotoPath = mMediaFile.getAbsolutePath();}
                Log.i(TAG, "createImageFile: "+ mCurrentPhotoPath);

                break;
            } // REQUEST_IMAGE_CAPTURE

            case REQUEST_VIDEO_CAPTURE: {

                if(getExternalFilesDir(Environment.DIRECTORY_PICTURES) != null)
                {
                    mMediaFile = new
                            File(Objects.requireNonNull(getExternalFilesDir(Environment.DIRECTORY_PICTURES)).getAbsolutePath()
                            + "/"+timeStamp+"_myvideo");
                }
                break;
            } // REQUEST_VIDEO_CAPTURE
        } //endSwitch


        return mMediaFile;
    }

    /**
     * This Method resizes an image file so it fits into an ImageView of pre-determined size
     * After resizing it converts to a bitmap and inserts the image into the ImageView so it displays in the UI
     * @param path path to the image file that needs resizing
     */
    private void setPic(String path) {

        // Get the dimensions of the View
        int targetW = mSetPictureTaken.getWidth();
        int targetH = mSetPictureTaken.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        // Get the dimensions of the image
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = 1;
        // Determine how much to scale down the image
        if(targetW > 0 || targetH > 0)
        {
            scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        }else
        {
            Log.e(TAG, "setPic: scaleFactor is 1 continuing ...");
        }

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        Bitmap mBitMap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        mSetPictureTaken.setImageBitmap(mBitMap);
    }

    /**
     * This Method is launched from two of the take image Button onClickListeners in the onCreate Method, it launches the camera Intent so user can take a photo
     * The method then replaces an already prepared image file using {@link #createMediaFile} saved to package data in external storage
     * Returns onActivityResult with Activity Request
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createMediaFile(REQUEST_TAKE_PHOTO);
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e(TAG, "dispatchTakePictureIntent: Could not create Image file: " +ex);
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.mediaintentactivity.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    /**
     * This Method is launched from Thumbnail Button onClick in the onCreate Method, it launches the camera Intent so user can take an image
     * This method then places the location of the temporary image into data extras
     * Returns onActivityResult, the Activity Request and the Extra data with a thumbnail
     */
    private void dispatchTakeThumbIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_THUMB_CAPTURE);
        }
    }

    /**
     * This Method is launched from video Button onClick in the onCreate Method, it launches the camera Intent so user can take a Video
     * The method then replaces an already prepared Video file using {@link #createMediaFile} saved to package data in external storage
     * Returns onActivityResult with Activity Request.
     */
    private void dispatchTakeVideoIntent() {

        //we call the createMediaFile method to create a new video file we can update with the capture content.
        File mediaFile = null;
        try {
            mediaFile = createMediaFile(REQUEST_VIDEO_CAPTURE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            if(mediaFile != null){
                mVideoURI = FileProvider.getUriForFile(this,
                        "com.example.mediaintentactivity.fileprovider",
                        mediaFile);
            }
            takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mVideoURI);
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    /**
     * Adds a media player to the video view we are displaying the recorded video in
     * @param videoUri is a Uri with the video location returned from onActivityResult
     */
    private void handleCameraVideo(Uri videoUri)
    {
        //little handling here if no Uri is generated.
        if(videoUri != null)
        {
            Toast.makeText(this, R.string.toaster, Toast.LENGTH_SHORT).show();
            Log.i(TAG, "handleCameraVideo: "+videoUri.toString());
        }else
        {
            Log.e(TAG, "handleCameraVideo: No video Uri returned");
        }

        // Add a Media controller to allow forward/reverse/pause/resume
        final MediaController mMediaController = new MediaController(
                PhotoIntentActivity.this, true);

        mMediaController.setEnabled(false);
        mVideoViewer.setMediaController(mMediaController);
        mVideoViewer.setVideoURI(videoUri);
        // Add an OnPreparedListener to enable the MediaController once the video is ready
        mVideoViewer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                mMediaController.setEnabled(true);
            }
        });
    }

    /**
     * Overrides the onPause, we need this to clean up and release video resources
     */
    @Override
    protected void onPause() {

        if (mVideoViewer != null && mVideoViewer.isPlaying()) {
            mVideoViewer.stopPlayback();
            mVideoViewer = null;
        }
        super.onPause();
    }

    /**
     * Updates the media scanner to tell the system a media file exists
     * assumption is the {@link #createMediaFile} method was run,
     * so this <b>cannot</b> be called from the {@link #dispatchTakeThumbIntent} Intent method.
     */
    public void galleryAddPic()
    {
        // Tell the media scanner about the new file so that it is
        // immediately available to the user.
        MediaScannerConnection.scanFile(this,
                new String[] { mMediaFile.toString() }, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
    }


    // Some lifecycle callbacks so that the image can survive orientation change

    /**
     * Overrides the onSaveInstanceState so we can send values from memory to be used when the instance is restored
     * Keeps values we need in memory when the view gets torn down on screen rotation
     * @param outState bundle of values returned from memory for use when the instance is restored
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //send entire thumbnail to save instance state
        outState.putParcelable(BITMAP_STORAGE_KEY, mSmallBitMap);
        outState.putBoolean(THUMBVIEW_VISIBILITY_STORAGE_KEY, (mSmallBitMap != null) );

        //send path to larger images to save instance state
        outState.putString(PATH_STORAGE_KEY, mCurrentPhotoPath);
        outState.putBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY, mSetSize);

        //send the Uri for the video
        outState.putParcelable(VIDEO_STORAGE_KEY, mVideoURI);
        outState.putBoolean(VIDEOVIEW_VISIBILITY_STORAGE_KEY, (mVideoURI != null) );

        super.onSaveInstanceState(outState);
    }

    /**
     * Overrides the onRestoreInstanceState so we can do something with the values we saved during the
     * onSaveInstanceState ensures the ImageViews and the VideoView re-initialises with the images/video on screen rotation
     * @param savedInstanceState current values saved by the application when instance is destroyed
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mSmallBitMap = savedInstanceState.getParcelable(BITMAP_STORAGE_KEY);
        mCurrentPhotoPath = savedInstanceState.getString(PATH_STORAGE_KEY);
        mVideoURI = savedInstanceState.getParcelable(VIDEO_STORAGE_KEY);

        if(savedInstanceState.getBoolean(THUMBVIEW_VISIBILITY_STORAGE_KEY)) {
            mPictureTaken.setImageBitmap(mSmallBitMap);
            mPictureTaken.setVisibility(ImageView.VISIBLE);
        } else if (mCurrentPhotoPath != null) {
            if (savedInstanceState.getBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY)){
                //send the path back to the imageView.
                setPic(mCurrentPhotoPath);
                mSetPictureTaken.setVisibility(ImageView.VISIBLE);
            } else {
                Bitmap mBitMap = BitmapFactory.decodeFile(mCurrentPhotoPath);
                mPictureTaken.setImageBitmap(mBitMap);
                mPictureTaken.setVisibility(ImageView.VISIBLE);
            }
        } else {
            if(savedInstanceState.getBoolean(VIDEOVIEW_VISIBILITY_STORAGE_KEY)) {
                handleCameraVideo(mVideoURI);
                mVideoViewer.setVisibility(VideoView.VISIBLE);
            }
        }
    }


    /**
     *  Overrides onActivityResult, contains a switch, checks what Action is taken in the above class
     *  and processes the return result data as required
     *  REQUEST_IMAGE_CAPTURE - on OkResult, checks for full size or predetermined size image based on mSetSize value:
     *  1) on full size sets imageView with image converted to bitmap
     *  2) on predetermined imageView size runs {@link #setPic(String path)}
     *  REQUEST_THUMB_CAPTURE - on OkResult gets the location of the thumbnail from Extra data and sets the imageView
     *  REQUEST_VIDEO_CAPTURE - on OkResult runs {@link #galleryAddPic()} and {@link #handleCameraVideo(Uri)}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE: {
                if (resultCode == RESULT_OK) {
                    //clean up any unwanted items currently in memory.
                    mSmallBitMap = null;
                    mVideoURI = null;

                    //check the Size, constraint or full size image.
                    if (mSetSize) {
                        Log.i(TAG, "onActivityResult: Set file size " + mCurrentPhotoPath);
                        galleryAddPic();
                        setPic(mCurrentPhotoPath);
                    } else {
                        Log.i(TAG, "onActivityResult: large file size " + mCurrentPhotoPath);
                        galleryAddPic();
                        Bitmap mBitMap = BitmapFactory.decodeFile(mCurrentPhotoPath);
                        mPictureTaken.setImageBitmap(mBitMap);
                    }
                }
                break;
            } // REQUEST_IMAGE_CAPTURE

            case REQUEST_THUMB_CAPTURE: {
                if (resultCode == RESULT_OK) {

                    //clean up any unwanted items currently in memory.
                    mCurrentPhotoPath = null;
                    mVideoURI = null;
                    Log.i(TAG, "onActivityResult: Tumbnail ");
                    //set a thumbnail to the ImageView
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        mSmallBitMap = (Bitmap) extras.get("data");
                        mPictureTaken.setImageBitmap(mSmallBitMap);
                    }
                }
                break;
            } // REQUEST_THUMB_CAPTURE

            case REQUEST_VIDEO_CAPTURE: {
                if (resultCode == RESULT_OK) {
                    //clean up any items currently in memory.
                    mSmallBitMap = null;
                    mCurrentPhotoPath = null;
                    Log.i(TAG, "onActivityResult: Video taken");

                    //Video file is handled here.
                    galleryAddPic();
                    handleCameraVideo(mVideoURI);
                }
                break;
            }

        }
    }

    /**
     * Indicates whether the specified action can be used as an intent This
     * method queries the package manager for installed packages that can
     * respond to an intent with the specified action If no suitable package is
     * found, this method returns false
     * http://android-developers.blogspot.com/2009/01/can-i-use-this-intent.html
     *
     * @param context The application's environment.
     * @param action The Intent action to check for availability.
     *
     * @return True if an Intent with the specified action can be sent and
     *         responded to, false otherwise.
     */
    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**
     * This runs the isIntentAvailable method, if the intent is not available (phone does not have video capability for example,
     * this method disables the associated button so the user does not crash the application
     * @param btn the button to be disabled
     * @param onClickListener associated onClick the on button
     * @param intentName name of intent being called by the buttons child method
     */
    private void setBtnListenerOrDisable(
            Button btn,
            Button.OnClickListener onClickListener,
            String intentName
    ) {
        if (isIntentAvailable(this, intentName)) {
            btn.setOnClickListener(onClickListener);
        } else {
            btn.setBackgroundColor(getColor(R.color.colorButtonGrey));
            String buttonText = getText(R.string.cannot).toString() + " " + btn.getText().toString();
            btn.setText(buttonText);
            btn.setClickable(false);
        }
    }
}
