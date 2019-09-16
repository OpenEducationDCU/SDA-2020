package com.example.preferencesfragment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //call the parent view.
        setContentView(R.layout.activity_main);

        //the fragment where we are inflating the preferences.xml must be a child of the parent view above.
        //we are replacing the setting_fragment in the activity_main.xml with the settings fragment.
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_fragment, new MySettingsFragment())
                .commit();
    }


    /**
     * This class is the nested class for the settings fragment we added to the MainActivity.
     * This class can be in it's own fragment class if you wish
     */
    public static class MySettingsFragment extends PreferenceFragmentCompat  {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

            //set the resources for the preferences here.
            setPreferencesFromResource(R.xml.peferences, rootKey);
        }
    }
}