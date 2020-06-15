package com.example.firenosqldatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AllCollection extends AppCompatActivity {

    private static final String TAG = "AllCollection";
    final ArrayList<String> mStrings = new ArrayList<>();
    final ArrayList<String> mImages = new ArrayList<>();
    FirebaseFirestore mFirestore;
    RecyclerView recyclerView;
    RecyclerViewAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_collection);

        mFirestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recycler_container);

        //call the recycler population method
        populateRecycler(this);
    }

    //gets the data from the collection and puts it into a RecyclerView
    public void populateRecycler(final Context context) {
        mFirestore.collection("menu")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                //extract each item from the document fields
                                String name = document.getString("name");
                                String url = document.getString("url");
                                mStrings.add(name);
                                mImages.add(url);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                        myAdapter = new RecyclerViewAdapter(context, mStrings, mImages);
                        recyclerView.setAdapter(myAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    }

                });
    }
}
