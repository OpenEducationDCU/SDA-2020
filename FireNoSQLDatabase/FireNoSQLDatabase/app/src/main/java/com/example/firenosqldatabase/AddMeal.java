package com.example.firenosqldatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddMeal extends AppCompatActivity {

    final public int PHOTO_PICKER = 1;
    private ProgressBar mProgressBar;
    Uri mImageUri = null;
    private static final String TAG = "AddMeal";

    //bunch of widgets and strings we need for later.
    EditText mealName, course, group;
    String entryOne, entryTwo, entryThree;

    //Firebase storage initializers.
    FirebaseStorage mStorage;
    StorageReference mStorageReference;
    FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);
        Button getPhoto = findViewById(R.id.getPhoto);
        mProgressBar = findViewById(R.id.progressBar);

        //get the two textviews.
        mealName = findViewById(R.id.addname);
        course = findViewById(R.id.addcourse);
        group = findViewById(R.id.document);

        //get the current instance of firebase storage
        mStorage = FirebaseStorage.getInstance();
        mStorageReference = mStorage.getReference();

        //set a listener on the upload button
        getPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entryOne = mealName.getText().toString();
                entryTwo = course.getText().toString();
                entryThree = group.getText().toString();
                //check if nothing is filled out OR.
                if(entryOne.equals("") || entryTwo.equals("") || entryThree.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter a meal and a course", Toast.LENGTH_LONG).show();
                }else {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                    startActivityForResult(Intent.createChooser(intent,"Complete action using"),PHOTO_PICKER);
                }
            }
        });
    }

    //gets the image and uploads it.
    @Override
    protected void onActivityResult(int resultCode, int result, Intent data) {
        super.onActivityResult(resultCode, result, data);
        if (resultCode == PHOTO_PICKER && result == RESULT_OK) {
            mImageUri = data.getData();
            if (mImageUri != null) {
                Log.i(TAG, "onActivityResult: " +mImageUri.toString());

                //makes the progress bar visible
                mProgressBar.setVisibility(View.VISIBLE);

                //send the image selected to storage from here.
                final StorageReference childRef = mStorageReference.child("/"+ UUID.randomUUID().toString());
                childRef.putFile(mImageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                mProgressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();

                                //add a second on success listener to make sure we can get the image path back then push it straight into storage as a string reference.
                                childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Log.d(TAG, "onSuccess: "+uri.toString());

                                        mFirestore = FirebaseFirestore.getInstance();

                                        CollectionReference menuList = mFirestore.collection("menu");
                                        Map<String, Object> data1 = new HashMap<>();
                                        data1.put("name", entryOne);
                                        data1.put("url", uri.toString());
                                        data1.put("course", entryTwo);
                                        menuList.document(entryThree).set(data1);

                                        //clear the editText.
                                        mealName.setText("");
                                        course.setText("");
                                        group.setText("");
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                mProgressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                mProgressBar.setVisibility(View.VISIBLE);
                            }
                        });
            }
        }else {
            Toast.makeText(this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }



}
