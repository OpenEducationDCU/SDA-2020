package com.example.privatepreferences;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.util.prefs.Preferences;

public class MainActivity extends AppCompatActivity {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    SharedPreferences.OnSharedPreferenceChangeListener mListener;
    EditText myNameText;
    TextView mySwitchState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //get the switch and the text views
        Switch mySwitch = findViewById(R.id.switch1);
        mySwitchState = findViewById(R.id.prefText);

        //check if the switch is already there and set it to true or false *default
        pref = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final boolean isSet = pref.getBoolean("switch", false); //where false is the default.
        mySwitch.setChecked(isSet);
        setSwitchText(isSet);

        //create our private preferences and initialize with default values
        myNameText = findViewById(R.id.myname);
        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("name", "default");
        editor.apply();

        //set a listener on the button and call the addName method
        Button mySave = findViewById(R.id.save);
        mySave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addName();
            }
        });

        //add items to shared preferences if the switch is selected.
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                editor.putBoolean("switch", b);
                setSwitchText(b);
            }
        });
    }

    private void addName() {
        String myEntry = myNameText.getText().toString();
        editor.putString("name", myEntry);
        editor.commit();

        Intent intent = new Intent(this, ViewResult.class);
        startActivity(intent);
    }

    private void setSwitchText(boolean isSet) {
        //first time the page opens
        if(isSet){
            mySwitchState.setText(getResources().getString(R.string.selected));
        } else {
            mySwitchState.setText(getResources().getString(R.string.notSelected));
        }
    }
}
