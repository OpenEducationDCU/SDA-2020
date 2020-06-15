package com.example.toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int myItem = item.getItemId();
        switch (myItem) {
            case R.id.action_setting:
                // code block
                Toast.makeText(this, "This is the settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_favorite:
                // code block
                Toast.makeText(this, "This is my favourites", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_first:
                Toast.makeText(this, "This is dropdown item1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_second:
                Toast.makeText(this, "This is dropdown item2", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}