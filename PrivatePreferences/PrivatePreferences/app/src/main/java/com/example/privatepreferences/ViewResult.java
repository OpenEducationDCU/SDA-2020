package com.example.privatepreferences;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import static com.example.privatepreferences.MainActivity.MY_PREFS_NAME;

public class ViewResult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_result);

        TextView mySavedName = findViewById(R.id.nameView);
        SharedPreferences pref = getSharedPreferences(MainActivity.MY_PREFS_NAME, MODE_PRIVATE);
        String newName = pref.getString("name", "user_name"); // “user name" is a default value.
        mySavedName.setText(newName);
    }
}
