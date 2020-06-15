package com.example.logindemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class Authenticated extends AppCompatActivity {


    Button btnLogOut;
    FirebaseAuth mAuth;
    //create an arrayList of the new ArrayAdapter object
    private ArrayList<UserAdapter> mUserAdapter = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticated);

        btnLogOut = findViewById(R.id.btnLogOut);
        mAuth = FirebaseAuth.getInstance();

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent Backout = new Intent(Authenticated.this, MainActivity.class);
                startActivity(Backout);
            }
        });


    }

    /**
     * Checking here to makes sure the user hasn't already logged in
     */
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is already signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //get the user details.
        getUser(currentUser);

        //send the email string to the button
        String emailDetail = "Logout \n" + mUserAdapter.get(0).getEmail();
        btnLogOut.setText(emailDetail);
    }


    /**
     * Method takes the Firebase User details and extracts the user details for use later
     * @param user user data
     */
    public void getUser(FirebaseUser user) {

        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified not using this currently
            boolean emailVerified = user.isEmailVerified();

            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();

            //populate our own userAdapter object ArrayList
            mUserAdapter.add(new UserAdapter(name, email, photoUrl, emailVerified, uid));
        }
    }
}
