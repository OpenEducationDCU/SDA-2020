package com.example.simplegetlocation;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //debug tag and permissions request key
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 2;
    private static final String TAG = "MainActivity";

    //declare our lat/longiude and the new FusedLocationProviderClient
    private FusedLocationProviderClient mFusedLocationClient;
    private double wayLatitude, wayLongitude;

    //Ui elements
    TextView mCoordinate;
    private Button mGetLocButton, mSetLocButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //textView and buttons
        mCoordinate = findViewById(R.id.coordinate);
        mGetLocButton = findViewById(R.id.button);
        mGetLocButton.setEnabled(false);
        mSetLocButton = findViewById(R.id.button2);
        mSetLocButton.setEnabled(false);

        //initialise our fusedlocationproviderclient and location service
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //check the permissions before we do anything.
        checkPermissions();

        //now we put a listener on our get location button so it can call the locations api
        mGetLocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastLocation();
            }
        });

        //now we put a listener on our get location button so it can call the locations api
        mSetLocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastAddress();
            }
        });

    }

    /*this method gets the last location details and sets the values in our
    simple textView it is called by the button being pressed.*/
    public void getLastLocation() {
        Log.d(TAG, "onClick: get location selected");

        //runs a quick permission check to stop the IDE complaining
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        //not using a lambda here *(show full OnSuccessListener code).
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            Log.d(TAG, "onSuccess: last known location is known");
                            wayLatitude = location.getLatitude();
                            wayLongitude = location.getLongitude();
                            String locationString = "Latitude:" + String.valueOf(wayLatitude) + "\nLongitude:" + String.valueOf(wayLongitude);
                            mCoordinate.setText(locationString);
                        } else {
                            Log.e(TAG, "onSuccess: location is empty");
                        }
                    }
                });
    }

    /*this gets the last location with geocoded address content*/
    protected void getLastAddress() {
        Log.d(TAG, "onClick: get address selected");

        //runs a quick permission check to stop the IDE complaining
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        //note I am using a lambda here.
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        wayLatitude = location.getLatitude();
                        wayLongitude = location.getLongitude();
                        // start up the geocoder
                        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(wayLatitude, wayLongitude, 1);
                            String finalAddress = getStringVal(addresses);

                            if (!finalAddress.equals("")) {
                                mCoordinate.setText(finalAddress);
                            } else {

                                mCoordinate.setText(getString(R.string.no_address));
                            }

                        } catch (IOException e) {
                            Log.e(TAG, "getLastAddress: ", e);
                        }
                    }
                });
    }

    //handles manipulating the string value of the returned addresses from geocoder.
    protected String getStringVal(List<Address> addresses) {

        String resultMessage = "";
        // If an address is found, read it into resultMessage
        Address address = addresses.get(0);
        ArrayList<String> addressParts = new ArrayList<>();

        // Grab the address lines using getAddressLine,
        // join them, and send them to the thread
        for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
            addressParts.add(address.getAddressLine(i));
        }

        resultMessage = TextUtils.join(
                "\n",
                addressParts);

        return resultMessage;
    }


    //this checks for permissions on coarse and fine location access.
    private void checkPermissions(){

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "onCreate: Access not granted to fine Location");

            //ask for permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        } else {
            //we already have access
            Log.i(TAG, "onCreate: we already have access");

            //enable the button.
            mGetLocButton.setEnabled(true);
            mSetLocButton.setEnabled(true);
        }
    }


    //this handles the response from the user when they are prompted to give us access
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, we can access the application getlocation functionality
                Log.i(TAG, "onCreate: we have permission to access fine and course locations on this device");
                //enable the buttons.
                mGetLocButton.setEnabled(true);
                mSetLocButton.setEnabled(true);

            } else {
                // permission denied, boo! Disable the associated functionality.
                Log.i(TAG, "onCreate: permission was denied");
                mCoordinate.setText(getString(R.string.permission_denied));
            }
        }
    }

}
