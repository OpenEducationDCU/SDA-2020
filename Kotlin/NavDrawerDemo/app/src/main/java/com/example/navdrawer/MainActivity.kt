package com.example.navdrawer

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private var mAppBarConfiguration: AppBarConfiguration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //call the toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        //find the floating action button
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { view -> //launches a simple snackbar, we can replace this with whatever we want, an intent for example
            Snackbar.make(view, "I am a snackbar, popup", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        //this finds the accompanying drawerLayout
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)

        //this simply finds the navigation view.
        val navigationView = findViewById<NavigationView>(R.id.nav_view)

        // Passing each menu ID to the app bar configuration
        mAppBarConfiguration = AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send, R.id.nav_cake)
                .setDrawerLayout(drawer)
                .build()
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration!!)
        NavigationUI.setupWithNavController(navigationView, navController)

        /*
*/
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        val mySettings = menu.findItem(R.id.action_settings)
        val mySave = menu.findItem(R.id.action_save)

        //listen for a selection on settings.
        mySettings.setOnMenuItemClickListener {
            val intent = Intent(this@MainActivity, PartyTime::class.java)
            startActivity(intent)
            false
        }

        //listen for a selection on save.
        mySave.setOnMenuItemClickListener {
            val fancy = Toast(this@MainActivity)
            fancy.duration = Toast.LENGTH_SHORT
            val offset = fancy.yOffset
            fancy.setGravity(Gravity.TOP, 0, offset)
            fancy.view = layoutInflater.inflate(R.layout.toast_demo, null)
            fancy.show()
            false
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        return (NavigationUI.navigateUp(navController, mAppBarConfiguration!!)
                || super.onSupportNavigateUp())
    }

    fun randomIntent(item: MenuItem?) {
        val intent = Intent(this, PartyTime::class.java)
        startActivity(intent)
    }
}