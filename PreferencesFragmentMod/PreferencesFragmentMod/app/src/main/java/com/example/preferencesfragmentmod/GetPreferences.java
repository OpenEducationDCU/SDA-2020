package com.example.preferencesfragmentmod;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class GetPreferences extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_preferences);

        TextView myText = findViewById(R.id.favAnimal);
        TextView myswitch = findViewById(R.id.switchResult);

        //get the values from the shared preferences fragment
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication());

        //get the string from the textEdit preference
        String favAnimal=sharedPreferences.getString("edittext_preference","nothing added yet");
        myText.setText(favAnimal);

        //get the selection from the switch.
        boolean mySwitch =sharedPreferences.getBoolean("Notifications",false);
        if(mySwitch) {
            myswitch.setText(getResources().getString(R.string.switchOn));
        } else {
            myswitch.setText(getResources().getString(R.string.switchOff));
        }

    }
}
