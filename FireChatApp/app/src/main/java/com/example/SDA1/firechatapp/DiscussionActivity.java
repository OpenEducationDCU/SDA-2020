package com.example.SDA1.firechatapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static com.example.SDA1.firechatapp.TopicActivity.USER_NAME_KEY;

public class DiscussionActivity extends AppCompatActivity {


    MyRecyclerViewAdapter adapter;
    Button mSendButton;
    EditText mMessageEditText;
    TextView mChatdetails;
    ImageView mMessageImageView;
    private ImageView mAddMessageImageView;
    ArrayList<SetChatItem> mListConversation = new ArrayList<>();
    private DatabaseReference chatDb;
    String mUserName, mSelectTopic, mTempKey;
    private static final String TAG = "DiscussionForm";
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    //some imported stuff.
    private RecyclerView mMessageRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ProgressBar mProgressBar;
    Uri mImageUri = null;

    //Firebase storage initializers.
    FirebaseStorage mStorage;
    StorageReference mStorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_form);
        //call sharedpreferences
        SharedPreferences mUserDetails = getSharedPreferences("user_details", MODE_PRIVATE);

        Toolbar toolbar = findViewById(R.id.discus_toolbar);
        setSupportActionBar(toolbar);
        //adds a home button to the toolbar.
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Initialize ProgressBar and RecyclerView.
        mProgressBar = findViewById(R.id.progressBar);
        mMessageRecyclerView = findViewById(R.id.messageRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        mChatdetails = findViewById(R.id.chatdetails);
        mMessageImageView = findViewById(R.id.messageImageView);

        //use sharedPreferences if they exist.
        if (mUserDetails != null) {
            mUserName = mUserDetails.getString(USER_NAME_KEY, "");
        }

        //gets the data from the Extras when the activity Loads.
        mSelectTopic = Objects.requireNonNull(Objects.requireNonNull(getIntent()
                .getExtras())
                .get("selected topic"))
                .toString();
        setTitle("Topic : " + mSelectTopic);

        //gets the current instance of firebase database + child topic we received from the extras
        chatDb = FirebaseDatabase.getInstance().getReference().child(mSelectTopic);

        //get the current instance of firebase storage
        mStorage = FirebaseStorage.getInstance();
        mStorageReference = mStorage.getReference();

        mMessageEditText = findViewById(R.id.messageEditText);
        mSendButton = findViewById(R.id.sendButton);
        mAddMessageImageView = findViewById(R.id.addMessageImageView);

        //when the user selects the add + button to choose an image from the database.
        mAddMessageImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);
            }
        });

        //when the user selects the send button update the message section in the database.
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get the key for the current topic
                mTempKey = chatDb.push().getKey();

                DatabaseReference chatDB2 = null;
                if (mTempKey != null) {
                    chatDB2 = chatDb.child(mTempKey);
                }
                Map<String, Object> map2 = new HashMap<>();
                map2.put("msg", mMessageEditText.getText().toString());
                map2.put("image", "");
                map2.put("user", mUserName);
                if (chatDB2 != null) {
                    chatDB2.updateChildren(map2);
                }
            }
        });

        //implements the event listener on on the database when the data updates then automatically
        //update the conversation listview.
        chatDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //call the update Conversation method to update the list in this view.
                updateConversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //update the adapter.
        mMessageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(mListConversation, this);
        mMessageRecyclerView.setAdapter(adapter);
    }

    /**
     * Updates the recyclerView
     * @param dataSnapshot (current state of the database collection)
     */
    public void updateConversation(DataSnapshot dataSnapshot) {
        String msg, user, image;

        Iterator<DataSnapshot> i = dataSnapshot.getChildren().iterator();
        while (i.hasNext()) {
            image = (String) (i.next()).getValue();
            msg = (String) (i.next()).getValue();
            user = (String) (i.next()).getValue();

            mListConversation.add(new SetChatItem(user,msg,image));
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int resultCode, int result, Intent data) {
        super.onActivityResult(resultCode, result, data);
        if (resultCode == 1 && result == RESULT_OK) {
            mImageUri = data.getData();
            if (mImageUri != null) {
                Log.i(TAG, "onActivityResult: " +mImageUri.toString());

                //makes the progress bar visible
                mProgressBar.setVisibility(View.VISIBLE);
                //removes whitespaces from topic name
                String localDirectory = mSelectTopic.replaceAll("\\s","_");

                //send the image selected to storage from here.
                final StorageReference childRef = mStorageReference.child(localDirectory+"/"+ UUID.randomUUID().toString());
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
                                        //add the path to the image to the datbase.
                                        Map<String, Object> map = new HashMap<>();
                                        mTempKey = chatDb.push().getKey();
                                        chatDb.updateChildren(map);

                                        DatabaseReference chatDB2 = chatDb.child(mTempKey);
                                        Map<String, Object> map2 = new HashMap<>();
                                        map2.put("msg", "");
                                        map2.put("image", uri.toString());
                                        map2.put("user", mUserName);
                                        chatDB2.updateChildren(map2);
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
                            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                                mProgressBar.setVisibility(View.VISIBLE);
                            }
                        });
            }
        }else {
            Toast.makeText(this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Overrides the Menu option menu so I can add my own
     *
     * @param menu menu view Object.
     * @return true value on successful creation of a menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Overrides the action on the toolbar when someone selects one of the options, in this case
     * it launches the LoginActivity intent with an Extra instructing the login to log out of
     * the google account
     *
     * @param item menu item returned from the selection
     * @return a true value *(required if we increase the menus size later)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_Logout) {
            Intent intent = new Intent(this, LoginActivity.class);
            String message = "INIG_LOGOUT";
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_token) {
            // [START retrieve_current_token]
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "getInstanceId failed", task.getException());
                                return;
                            }

                            // Get new Instance ID token
                            String token = Objects.requireNonNull(task.getResult()).getToken();
                            Log.d(TAG, token);
                            Toast.makeText(DiscussionActivity.this, token, Toast.LENGTH_SHORT).show();
                        }
                    });
            // [END retrieve_current_token]
        }
        return super.onOptionsItemSelected(item);
    }

}