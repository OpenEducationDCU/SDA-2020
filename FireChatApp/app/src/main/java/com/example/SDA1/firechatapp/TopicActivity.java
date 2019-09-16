package com.example.SDA1.firechatapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TopicActivity extends AppCompatActivity {

    private static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    ListView mNewView;
    ArrayList<String> mlistOfTopics = new ArrayList<>();
    ArrayAdapter<String> mArrayadapt;
    String mUserName;
    String mUserEmail;
    String mUserUri;
    String mUser_login;

    private DatabaseReference chatDB = FirebaseDatabase.getInstance().getReference().getRoot();
    private static final String TAG = "TopicActivity";

    //initialise sharedpreferenecs in this activity
    SharedPreferences mUserDetails = null;
    public static final String USER_NAME_KEY = "user_name";
    public  static final String USER_EMAIL_KEY = "user_email";
    public  static final String USER_URI_KEY = "user_pic";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        //set the toolbar and the home button
        Toolbar toolbar = findViewById(R.id.topic_toolbar);
        setSupportActionBar(toolbar);

        //call sharedpreferences if we are here we should be logged in.
        SharedPreferences mUserDetails = getSharedPreferences("user_details", MODE_PRIVATE);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            // User is signed in
            mUserName = user.getDisplayName();
            mUserEmail = user.getEmail();
            Uri userUri = user.getPhotoUrl();

            // iterate through the provider data ignoring null values
            // source https://stackoverflow.com/questions/37661747/firebaseauth-getcurrentuser-return-null-displayname
            for (UserInfo userInfo : user.getProviderData()) {
                if (mUserName == null && userInfo.getDisplayName() != null) {
                    mUserName = userInfo.getDisplayName();
                }
                if (mUserEmail == null && userInfo.getEmail() != null) {
                    mUserEmail = userInfo.getEmail();
                }
                if (userUri == null && userInfo.getPhotoUrl() != null) {
                    userUri = userInfo.getPhotoUrl();
                }
            }
            if(userUri !=null) {
                mUserUri = userUri.toString();
            }

            Log.i(TAG, "onCreate: "+mUserName+mUserEmail+mUserUri);

            //Insert this information int sharedPreferences so we can use it elsewhere. (See Unit 13)
            SharedPreferences.Editor editor = mUserDetails.edit();
            editor.putString(USER_NAME_KEY, mUserName);
            editor.putString(USER_EMAIL_KEY, mUserEmail);
            if (mUserUri != null) {
                editor.putString(USER_URI_KEY, mUserUri);
            }
            editor.apply();
        }

        //this adds the users avatar to the Topic selection page.
        ImageView myAvatar = findViewById(R.id.imageAvatar);
        TextView myAvatarText = findViewById(R.id.user_welcome);

        //use sharedPreferences if they exist.
        if (mUserDetails != null) {
            mUserName = mUserDetails.getString(USER_NAME_KEY, "");
            mUserEmail = mUserDetails.getString(USER_EMAIL_KEY,"");
            mUserUri =  mUserDetails.getString(USER_URI_KEY,"");

            String welcomeUser = getString(R.string.username_desc) + mUserName;
            String welcomeEmail = getString(R.string.email_desc)+ mUserEmail;
            String welcomeInst = getString(R.string.topic_welcome);
            mUser_login = welcomeUser+'\n'+welcomeEmail+'\n'+welcomeInst;
            myAvatarText.setText(mUser_login);

            if(mUserUri != null)
            {
                Glide.with(this)
                        .asBitmap()
                        .load(mUserUri)
                        .error(R.drawable.avatar_default)
                        .override(200, 200)
                        .into(myAvatar);
            } else {
                Glide.with(this)
                        .asBitmap()
                        .load(R.drawable.avatar_default)
                        .override(200, 200)
                        .into(myAvatar);
            }
        }


        //updates the arrayList (simple adapter) when the database is updated.
        chatDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Set<String> set = new HashSet<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    set.add((snapshot).getKey());
                }

                mArrayadapt.clear();
                mArrayadapt.addAll(set);
                mArrayadapt.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        //Below inserts the values into the listview
        mNewView = findViewById(R.id.listOne);
        mArrayadapt = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mlistOfTopics);
        mNewView.setAdapter(mArrayadapt);

        //listens for a selection on adapterView and launches the DiscussionForm activity with the users name
        mNewView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mUserName != null){
                    Intent intent = new Intent(getApplicationContext(), DiscussionActivity.class);
                    intent.putExtra("selected topic", ((TextView)view).getText().toString());
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * Overrides the Menu option menu so I can add my own
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
                            String token = task.getResult().getToken();
                            Log.d(TAG, token);
                            Toast.makeText(TopicActivity.this, token, Toast.LENGTH_SHORT).show();
                        }
                    });
            // [END retrieve_current_token]
        }
        return super.onOptionsItemSelected(item);
    }
}
