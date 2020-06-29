package com.example.preferencesfragmentmod;

import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * This gets the preferences from shared preferences and displays information on it's UI.
 * @author chris coughlan 2019
 */
public class GetPreferences extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_preferences);

        TextView mText = findViewById(R.id.favAnimal);
        TextView mSwitch = findViewById(R.id.switchResult);

        //get the values from the shared preferences fragment
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication());

        //get the string from the textEdit preference
        String favAnimal=sharedPreferences.getString("edittext_preference", getString(R.string.summary_edittext_preference));
        mText.setText(favAnimal);

        //get the selection from the switch.
        boolean newSwitch =sharedPreferences.getBoolean("Notifications",false);
        if(newSwitch) {
            mSwitch.setText(getResources().getString(R.string.switchOn));
        } else {
            mSwitch.setText(getResources().getString(R.string.switchOff));
        }

    }
}
