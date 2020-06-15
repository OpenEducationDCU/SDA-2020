package com.example.firenosqldatabase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";
    FirebaseFirestore mFirestore;
    ImageView image;
    TextView foodname;
    TextView seating;
    Button next;
    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //start the database
        initFirestore();

        image = findViewById(R.id.foodImage);
        foodname = findViewById(R.id.name);
        seating = findViewById(R.id.seating);
        next = findViewById(R.id.nextButton);
        add = findViewById(R.id.addEntry);

        //next button
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextPage = new Intent(view.getContext(), AllCollection.class);
                startActivity(nextPage);
            }
        });

        //add button
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addPage = new Intent(view.getContext(), AddMeal.class);
                startActivity(addPage);
            }
        });
    }


    private void initFirestore() {
        mFirestore = FirebaseFirestore.getInstance();

        CollectionReference menuList = mFirestore.collection("menu");
        Map<String, Object> data1 = new HashMap<>();
        data1.put("name", "Shish Kebab");
        data1.put("url", "https://firebasestorage.googleapis.com/v0/b/sdafirebase01.appspot.com/o/shish-kebab-417994_640.jpg?alt=media&token=c7b49987-a07b-4c19-8fb1-5c4d4946b5bd");
        data1.put("course", "Starters");
        menuList.document("kebab").set(data1);

        Map<String, Object> data2 = new HashMap<>();
        data2.put("name", "Vanilla Pancakes");
        data2.put("url", "https://firebasestorage.googleapis.com/v0/b/sdafirebase01.appspot.com/o/pancakes-2020863_640.jpg?alt=media&token=43023cce-11cd-442b-82f1-d6554f71f450");
        data2.put("course", "Desserts");
        menuList.document("pancakes").set(data2);

        Map<String, Object> data3 = new HashMap<>();
        data3.put("name", "Pizza");
        data3.put("url", "https://firebasestorage.googleapis.com/v0/b/sdafirebase01.appspot.com/o/piza-3010062_640.jpg?alt=media&token=32a6d419-0583-4387-8d4c-be913371a732");
        data3.put("course", "Mains");
        menuList.document("pizza").set(data3);

        Map<String, Object> data4 = new HashMap<>();
        data4.put("name", "Salmon Steak");
        data4.put("url", "https://firebasestorage.googleapis.com/v0/b/sdafirebase01.appspot.com/o/salmon-518032_640.jpg?alt=media&token=c7cb2c50-10ff-4f04-ba4e-518af648e7ea");
        data4.put("course", "mains");
        menuList.document("salmon").set(data4);

        Map<String, Object> data5 = new HashMap<>();
        data5.put("name", "Hamburger");
        data5.put("url", "https://firebasestorage.googleapis.com/v0/b/sdafirebase01.appspot.com/o/hamburger-494706_640.jpg?alt=media&token=a5a411a6-af67-4cab-b73c-e1c22be6cae0");
        data5.put("course", "Mains");
        menuList.document("hamburger").set(data5);

        Map<String, Object> data6 = new HashMap<>();
        data6.put("name", "Frosted Cupcakes");
        data6.put("url", "https://firebasestorage.googleapis.com/v0/b/sdafirebase01.appspot.com/o/cupcakes-690040_640.jpg?alt=media&token=2a951f25-e712-4753-96a1-a571d502a8b4");
        data6.put("course", "Desserts");
        menuList.document("cupcakes").set(data6);

        Map<String, Object> data7 = new HashMap<>();
        data7.put("name", "Asparagus spears and lamb");
        data7.put("url", "https://firebasestorage.googleapis.com/v0/b/sdafirebase01.appspot.com/o/asparagus-2169305_640.jpg?alt=media&token=a80892e1-f59c-4934-a615-e8bdbee86911");
        data7.put("course", "Starters");
        menuList.document("asparagus").set(data7);

        //gets a single document reference from the database
        getRef("menu", "hamburger");

        // get a list of just mains.
        justMains();
    }

    //simple activity to retrieve a single document reference.
    public void getRef(String collection, String document) {
        DocumentReference docRef = mFirestore.collection(collection).document(document);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        //extract each item from the document fields
                        String name = document.getString("name");
                        String url = document.getString("url");
                        String course = document.getString("course");

                        Glide.with(getApplicationContext())
                                .load(url)
                                .into(image);

                        String specials = course + " " + getString(R.string.specials);
                        foodname.setText(name);
                        seating.setText(specials);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    //this method retrieves any entries where the field course equals "mains" in the menu collection
    public void justMains() {
        mFirestore.collection("menu")
                .whereEqualTo("course", "Mains")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}
