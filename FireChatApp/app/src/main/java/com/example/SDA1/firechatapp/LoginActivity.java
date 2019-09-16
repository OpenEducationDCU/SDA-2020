package com.example.SDA1.firechatapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Collections;
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    //onActivityResult Key.
    private static final int RC_SIGN_IN = 1;

    //initialise sharedpreferenecs in this activity
    SharedPreferences mUserDetails = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserDetails = getSharedPreferences("user_details", MODE_PRIVATE);
        setContentView(R.layout.activity_login);

        // clear shared preferences when this page loads.
        mUserDetails.edit().clear().apply();

        //using the Extras here to see if LoginActivity requires a logout
        Bundle extras = getIntent().getExtras();
        String isLoggedOut;
        if (extras != null) {
            isLoggedOut = extras.getString("EXTRA_MESSAGE");
            if(isLoggedOut != null &&isLoggedOut.equals("INIG_LOGOUT")) {
                signOut();
            }
        }

        // Choose authentication providers it will look like this.
        /*List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build());*/

        //we are only using one provider so sending it as a singletonList item
        final AuthUI.IdpConfig providers = new AuthUI.IdpConfig.GoogleBuilder().build();
        SignInButton googleSign = findViewById(R.id.sign_in_button);
        googleSign.setSize(SignInButton.SIZE_WIDE);

        googleSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create and launch sign-in intent
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(Collections.singletonList(providers))
                                .build(),
                        RC_SIGN_IN);
            }
        });

        Button sign_out = findViewById(R.id.sign_out);
        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }

    /**
     * Overrides the onActivityResult, if launching the firebase AuthUI login is successful returns\
     * The login data is returned and activated on firebase including user details.
     * User details are inserted into the applications SharedPreferences for use in other activities.
     * @param requestCode denotes the activity for result(we only have one here)
     * @param resultCode true if login successful
     * @param data data returned by the successful activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Intent TopicActivity = new Intent(this, TopicActivity.class);
                if (user != null) {
                    startActivity(TopicActivity);
                }
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                if (response != null) {
                    Log.i(TAG, "onActivityResult: "+response.getError());
                }
                Toast toast = Toast.makeText(this, "login failed please try again", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    /**
     * Simply signs us out of Firebase for the SDA chat application and removes any information
     * relating to the current user from user details in shared preferences, also generates a toast
     * to give the user feedback on successful log out of Firebase.
     */
    public void signOut() {
        // [START auth_fui_signout]
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        //log out of firebase generate a toast message
                        Toast.makeText(getApplication(), R.string.loggingOut, Toast.LENGTH_LONG)
                                .show();
                    }
                });
        // [END auth_fui_signout]
    }
}
