package com.example.simpleroomexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

import javax.security.auth.login.LoginException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static volatile AppDatabase INSTANCE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView myText = findViewById(R.id.textView1);


        //note the allowMainThreadQueries, this lets us handle database queries directly on the main thread.
        //You must use an asynchronous task to contact the database.
        INSTANCE = Room.databaseBuilder(this, AppDatabase.class, "mydb")
                .addCallback(sRoomDatabaseCallback)
                .build();

        //this first async tasks lets us add additional values to the database by running an asynchronous task in the main activity.
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                ItemDao mItems = INSTANCE.getItemDAO();

                //insert some default values
                Item item = new Item();
                item.setName("Item001");
                item.setDescription("Item 001 from MainUI");
                item.setQuantity(1000);
                mItems.insert(item);
            }
        });

        //this second example of an async tasks that lets us pull values from the database and add them to a simple textview
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                ItemDao mValues = INSTANCE.getItemDAO();
                List<Item> items = mValues.getItems();

                StringBuilder dbString = new StringBuilder();
                for (Item word:items)
                {
                    dbString.append(word.getName()+"\n");
                    dbString.append(word.getDescription());
                    dbString.append("\nQuantity ="+word.getQuantity());
                    dbString.append("\n");
                }
                myText.setText(dbString);
            }
        });
    }

    /*
     * Asynchronous Callback To delete all content and repopulate the database when the database is created.
     * you can also create a RoomDatabase.Callback and override onOpen().
     * creates and executes an AsyncTask to add content to the database.
     */
    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onCreate (@NonNull SupportSQLiteDatabase db){
                    super.onCreate(db);
                    new NewDbAsync(INSTANCE).execute();
                }
            };

    private static class NewDbAsync extends AsyncTask<Void, Void, Void> {

        private final ItemDao mDao;

        NewDbAsync(AppDatabase db) {
            mDao = db.getItemDAO();
        }

        @Override
        protected Void doInBackground(final Void... params) {

            //added to reset the database
            mDao.deleteAll();

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

            return null;

        }
    }
}
