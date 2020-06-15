package com.example.arrayadapteractivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //create an arrayList example
    private ArrayList<String> mStringArray = new ArrayList<>();

    //create an arrayList of the new ArrayAdapter object
    private ArrayList<ArrayAdapter> mArrayAdapterList = new ArrayList<>();

    //declare the widgets in view
    TextView mtextOne;
    TextView mtextTwo;
    ImageView mImageOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //populate the regular array
        mStringArray.add("String one");
        mStringArray.add("String two");
        mStringArray.add("String three");

        //populate our own arrayAdapter object ArrayList
        mArrayAdapterList.add(new ArrayAdapter("adapter string rose", R.drawable.rose));
        mArrayAdapterList.add(new ArrayAdapter("adapter string daisy", R.drawable.daisy));
        mArrayAdapterList.add(new ArrayAdapter("adapter string water lilly", R.drawable.waterlily));

        //find the views in our UI
        mtextOne = findViewById(R.id.textView);
        mtextTwo = findViewById(R.id.textView2);
        mImageOne = findViewById(R.id.imageView);

        //here we grab a string from our String array above and add it to the TextView.
        //get is a built-in method for ArrayList.
        String arrayString = mStringArray.get(0);
        mtextOne.setText(arrayString);

        //we can also iterate over the arrayList using
        for (String m : mStringArray) {
            mtextOne.setText(m);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        //here we use the arrayAdapter to find the values at a particular position in the array.
        String firstString = mArrayAdapterList.get(0).getString();
        int resourceId = mArrayAdapterList.get(0).getImageResourceId();
        mtextTwo.setText(firstString);
        mImageOne.setImageResource(resourceId);

        //here is how to iterate over the ArrayAdapter array
        for(ArrayAdapter item : mArrayAdapterList) {
            mtextTwo.setText(item.getString());
            mImageOne.setImageResource(item.getImageResourceId());
        }
    }
}
