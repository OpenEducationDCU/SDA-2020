package com.example.recyclerview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //add array for each item\
    private ArrayList<String> mStrings = new ArrayList<>();
    private ArrayList<Integer> mImages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //add the items to the two arrays.
        mStrings.add("this is a rose");
        mImages.add(R.drawable.rose);
        mStrings.add("this is a daisy");
        mImages.add(R.drawable.daisy);
        mStrings.add("this is a lilly");
        mImages.add(R.drawable.waterlily);

        //(and so on)
        mStrings.add("this is a rose"); mImages.add(R.drawable.rose);
        mStrings.add("this is a daisy"); mImages.add(R.drawable.daisy);
        mStrings.add("this is a lilly"); mImages.add(R.drawable.waterlily);
        mStrings.add("this is a rose"); mImages.add(R.drawable.rose);
        mStrings.add("this is a daisy"); mImages.add(R.drawable.daisy);
        mStrings.add("this is a lilly"); mImages.add(R.drawable.waterlily);
        mStrings.add("this is a rose"); mImages.add(R.drawable.rose);
        mStrings.add("this is a daisy"); mImages.add(R.drawable.daisy);
        mStrings.add("this is a lilly"); mImages.add(R.drawable.waterlily);

        RecyclerView recyclerView = findViewById(R.id.recycler_container);
        RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(this, mStrings, mImages);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

    }
}
