package com.example.simpleroomexample;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Simple Room database example
 * @author chris coughlan 2019
 * @since 2020
 * @version 2.0
 */
public class MainActivity extends AppCompatActivity {
    private static volatile AppDatabase INSTANCE;
    private static final String TAG = "MainActivity";

    TextView myText;
    TextView myAsyncText;
    ExecutorService executor;
    Button mAsyncButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myText = findViewById(R.id.Executor);
        myAsyncText = findViewById(R.id.asyncView);
        mAsyncButton = findViewById(R.id.asyncButton);

        //note the allowMainThreadQueries, this lets us handle database queries directly on the main thread.
        //You must use an asynchronous task to contact the database.
        INSTANCE = Room.databaseBuilder(this, AppDatabase.class, "mydb")
                .addCallback(sRoomDatabaseCallback)
                .build();

        executor = Executors.newFixedThreadPool(3);

        mAsyncButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //post call to AsyncClass
                    new NewDbAsync(INSTANCE).execute();
                }
            });
    }

    //populate the first values using two asynchronous threads one with a Future task
    @Override
    protected void onStart() {
        //display values in a future task
        Future<Boolean> futureTask = executor.submit(new Callable<Boolean>() {
            //this returns the integer and calls a callable thread.
            @Override
            public Boolean call() {
                //execute the Async class file below with the new instance.
                ItemDao mItems = INSTANCE.getItemDAO();
                //insert some default values
                Item item = new Item();
                item.setName("Item001");
                item.setDescription("Item 001 from MainUI");
                item.setQuantity(1000);
                mItems.insert(item);
                Log.i(TAG, "calling future: ");
                return true;
            }
        });

        try {
            //call future task, timeout if it doesn't return a result within 10 seconds.
            Boolean result = futureTask.get(5, TimeUnit.SECONDS);
            if(result){
                //call the method to populate the UI with the database content
                accessValues();
            } else {
                Log.e(TAG, "onCreate: could not update UI");
            }
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
            Log.i(TAG, "onCreate: "+e);
        }

        super.onStart();
    }

    //this is simply used to clear the database for this example.
    @Override
    protected void onStop() {
        //clear the instance on each new run
        executor.submit(new Runnable() {
            @Override
            public void run() {
                INSTANCE.getItemDAO().deleteAll();
            }
        });
        Log.i(TAG, "onStop: clearing data");

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // The activity is about to be destroyed.
        Log.i(TAG, "onDestroy: clearing data");
        super.onDestroy();

    }

    /**
     * accesses database values directly using a new async task
     */
    private void accessValues() {
        //this second example of an async tasks that lets us pull values from the database and add them to a simple textview
        executor.submit(new Runnable() {
            @Override
            public void run() {
                ItemDao mValues = INSTANCE.getItemDAO();
                List<Item> items = mValues.getItems();

                final StringBuilder dbString = new StringBuilder();
                for (Item word:items)
                {
                    dbString.append(word.getName()+"\n");
                    dbString.append(word.getDescription());
                    dbString.append("\nQuantity ="+word.getQuantity());
                    dbString.append("\n");
                }

                // This will work in Android because we are calling the UI thread.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String preExe = "From Async method : \n" + dbString;
                        myText.setText(preExe);
                    }
                });
            }
        });
    }

    /*
     * Asynchronous Callback To delete all content and repopulate the database when the database is created.
     * you can also create a RoomDatabase.Callback and override onOpen().
     * call this to add values directly after the database is created
     */
    private RoomDatabase.Callback sRoomDatabaseCallback =
        new RoomDatabase.Callback(){

        @Override
        public void onCreate (@NonNull SupportSQLiteDatabase db){
            //note any functionality in here will only execute when the database installs.
            Log.i(TAG, "onCreate: logged when the database is first created");
            super.onCreate(db);
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db){
            //note any functionality in here will only execute when the database is opened
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast. makeText(getApplicationContext(), "This launches when the database is opened", Toast. LENGTH_SHORT).show();
                }
                });
            }
    };

    @SuppressLint("StaticFieldLeak")
    private class NewDbAsync extends AsyncTask<String, Void, String> {

        private final ItemDao mDao;
        List<Item> items;

        NewDbAsync(AppDatabase db) {
            mDao = db.getItemDAO();
        }

        @Override
        protected String doInBackground(String... params) {

            //insert some default values
            Item item = new Item();
            item.setName("Item002");
            item.setDescription("Item 002 in background");
            item.setQuantity(2000);
            mDao.insert(item);

            item.setName("Item003");
            item.setDescription("Item 003s in background");
            item.setQuantity(3000);
            mDao.insert(item);

            items = mDao.getItems();
            return "complete";
        }

        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            if(result.equals("complete")) {
                //do something here after doInBackground completes
                StringBuilder dbString = new StringBuilder();
                for (Item word:items)
                {
                    dbString.append(word.getName()+"\n");
                    dbString.append(word.getDescription());
                    dbString.append("\nQuantity ="+word.getQuantity());
                    dbString.append("\n");
                }
                String postEx = "\nFrom Post execute : \n" + dbString;
                myAsyncText.setText(postEx);
                mAsyncButton.setEnabled(false);
            }
        }
    }
}
