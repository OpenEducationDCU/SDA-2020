package com.example.preferencesfragmentmod;

//licensed Attribution 2.5 Generic (CC BY 2.5)

import androidx.appcompat.app.AppCompatActivity;

import android.app.backup.SharedPreferencesBackupHelper;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

/**
 * this class inflates a fragment in the MainActivity
 * sources: https://developer.android.com/guide/topics/ui/settings
 * @author chris coughlan 2019
 * @since 2020
 */
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
     * This class sets all the content of the xml to a SharedPreference.
     * @author chris 2019
     */
    public static class MySettingsFragment extends PreferenceFragmentCompat {

        SharedPreferences prefs;
        SharedPreferences.OnSharedPreferenceChangeListener listener;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

            //set the resources for the preferences here.
            setPreferencesFromResource(R.xml.peferences, rootKey);
        }
    }
    
    /*
    //this is a copy of the MySettingsFragment with an OnSharedPreferenceChangeListener
    public static class MySettingsFragment extends PreferenceFragmentCompat {

        SharedPreferences prefs;
        SharedPreferences.OnSharedPreferenceChangeListener listener;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

            //set the resources for the preferences here.
            setPreferencesFromResource(R.xml.peferences, rootKey);

            Context mContext = this.getContext();
            if (mContext != null) {
                prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
            }

            //first let's check to see if this was updated earlier and fix the UI.
            Preference EditTextPreference = findPreference("edittext_preference");
            String favAnimal = prefs.getString("edittext_preference", getString(R.string.summary_edittext_preference));

            //if the summary doesn't match the sharedPreference it was updated in the past so change it.
            if(!EditTextPreference.getSummary().equals(favAnimal))
            {
                if(!favAnimal.equals("")) {
                    EditTextPreference.setSummary(prefs.getString("edittext_preference", favAnimal));
                }else{
                    EditTextPreference.setSummary(favAnimal);
                }
            }

            //listen for any new updates to the Default Shared Preferences
            listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                    // Implementation
                    Preference pref = findPreference(key);
                    //get the current string from the SharedPreferences
                    String favAnimal = prefs.getString("edittext_preference", "default text");
                    //check if the key matches the pref and update the summary.
                    if (key.equals("edittext_preference")){
                        if(!favAnimal.equals("")) {
                            pref.setSummary(favAnimal);
                        }else{
                            pref.setSummary(getString(R.string.summary_edittext_preference));
                        }
                    }
                }
            };
        }

        //start the listener on resume
        @Override
        public void onResume() {
            super.onResume();
            //call it on Resume
            prefs.registerOnSharedPreferenceChangeListener(listener);
        }

        //stop the listener when the user pauses the activity
        @Override
        public void onPause() {
            super.onPause();
            //stop listening on pause
            prefs.unregisterOnSharedPreferenceChangeListener(listener);
        }
    }*/
}