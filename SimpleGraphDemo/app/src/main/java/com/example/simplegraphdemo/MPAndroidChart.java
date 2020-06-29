package com.example.simplegraphdemo;

/*
 * Copyright Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

/**
 * Creates a BarChart<br>
 * Created using information from these sources:<br>
 * https://weeklycoding.com/mpandroidchart/
 * https://javapapers.com/android/android-chart-example-app-using-mpandroidchart/ *(older version of MPAndroidChart)
 * https://inducesmile.com/android-programming/how-to-draw-barchart-using-mpandroidchart-in-android/
 * @author Chris Coughlan
 * @version 1.0
 * @since 2020
 */
public class MPAndroidChart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        //add a toolbar
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("MPAndroidChart Barchart");

        //find the barchart in the xml UI.
        BarChart barChart = findViewById(R.id.chart);

        //declare a new BarDataSet object with the content of the entries array in the getData() method.
        BarDataSet barDataSet = new BarDataSet(getData(), "Monthly Result");
        //give the bars a border and a colour set.
        barDataSet.setBarBorderWidth(0.9f);
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        //create a new BarData object from our DataSet
        BarData barData = new BarData(barDataSet);

        //gat and move the xAxis labels to the bottom of the chart,
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        //create a string array containing months.
        final String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun"};

        //apply the Month string array to our xAxis.
        IndexAxisValueFormatter formatter = new IndexAxisValueFormatter(months);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(formatter);

        //add the barData animate them and then do some clean-up (invalidate clear memory)
        barChart.setData(barData);
        barChart.getDescription().setEnabled(false);
        barChart.setFitBars(true);
        barChart.animateXY(5000, 5000);
        barChart.invalidate();
    }

    private ArrayList<BarEntry> getData(){
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 30));
        entries.add(new BarEntry(1f, 80));
        entries.add(new BarEntry(2f, 60));
        entries.add(new BarEntry(3f, 50));
        entries.add(new BarEntry(4f, 70));
        entries.add(new BarEntry(5f, 45));
        return entries;
    }

    //create the new menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    //place a listener to activate an intent when the menu item is selected.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int myItem = item.getItemId();

        if (myItem == R.id.action_setting) {
            //single item so we use an if here
            Intent myIntent = new Intent(this, MPAndroidPie.class);
            startActivity(myIntent);
        }
        return super.onOptionsItemSelected(item);
    }

}

