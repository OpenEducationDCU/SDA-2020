package com.example.lifecycle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String STATE_KEY = "paused";
    TextView mView;
    String mString = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mView = findViewById(R.id.new_text);
        Log.i(TAG, "onCreate: ");
        if(savedInstanceState != null)
        if(savedInstanceState != null) {
            mView.setText(savedInstanceState.getString(STATE_KEY));
            Log.i(TAG, "onRestoreInstanceState: restored the pause string");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
        mString = "I was paused";
        mView.setText(mString);
    }

   @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mString != null) {
            Log.i(TAG, "onSaveInstanceState: saved the pause string");
            outState.putString(STATE_KEY, mString);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState != null) {
            mView.setText(savedInstanceState.getString(STATE_KEY));
            Log.i(TAG, "onRestoreInstanceState: restored the pause string");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
    }


    public void newActivity(View view) {
        Intent myIntent = new Intent(view.getContext(), newActivity.class);
        startActivity(myIntent);
    }

}


