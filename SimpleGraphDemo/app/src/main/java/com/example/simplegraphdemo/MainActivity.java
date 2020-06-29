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

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.Random;

/**
 * lineGrapg is modified from this tutorial
 * https://www.ssaurel.com/blog/create-a-real-time-line-graph-in-android-with-graphview/
 * Icon source:
 * https://material.io/resources/icons/?icon=addchart&style=baseline
 * GraphView code source:
 * https://github.com/jjoe64/GraphView
 */
public class MainActivity extends AppCompatActivity {

    private static final Random RANDOM = new Random();
    private LineGraphSeries<DataPoint> lineSeries;
    private int lastX = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //add a toolbar
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        setTitle("GraphView Charts");

        // we get graph view instance
        GraphView lineGraph = findViewById(R.id.graph);

        // add data for the line graph
        lineSeries = new LineGraphSeries<>();
        lineGraph.addSeries(lineSeries);

        // customize the viewport for the linegraph x axis from 0 to 10.
        Viewport viewport = lineGraph.getViewport();
        //set the bounds to manual so we can automatically scroll to the right
        viewport.setXAxisBoundsManual(true);
        viewport.setMinX(0);
        viewport.setMaxX(11);
        viewport.setBackgroundColor(getColor(R.color.colorGraphBack));
        //set it to a scrollable graph.
        viewport.setScrollable(true);


        // everything below here controls the static bar graph.
        GraphView barGraph = findViewById(R.id.graphBar);
        //style the barGraph viewport
        Viewport barView = barGraph.getViewport();
        barView.setBackgroundColor(getColor(R.color.colorGraphBack));
        barView.setScrollable(true);

        //add static content to the Bar Graph series
        BarGraphSeries<DataPoint> barSeries = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(0, -2),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });

        // sets a margin and a colour to the bargraph based on the values.
        // source https://stackoverflow.com/questions/53156648/graphview-bar-color-every-other
        barSeries.setSpacing(1);
        barSeries.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/2, (int) Math.abs(data.getY()*255/3), 100);
            }
        });
        barGraph.addSeries(barSeries);
    }

    /**
     * onResume lifecycle method is overridden to enter mock live data into a line chart.
     * When the application resumes; run a new thread, this thread touches the main
     * UI and updates the data point with a random number each time.
     * it also includes a sleep timer so the random number is generated slow enough to be read by a user
     */
    @Override
    protected void onResume() {
        super.onResume();
        // we're going to simulate real time with thread that append data to the graph
        new Thread(new Runnable() {

            @Override
            public void run() {
                // we add a maximum of 100 new entries
                for (int i = 0; i < 100; i++) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            addEntry();
                        }
                    });

                    // sleep to slow down the addition of entries
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // manage error ...
                    }
                }
            }
        }).start();
    }

    // add random data to graph
    private void addEntry() {
        // here, we choose to display max 10 points on the viewport and we scroll to end
        lineSeries.appendData(new DataPoint(lastX++, RANDOM.nextInt(9)), true, 15);
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
            Intent myIntent = new Intent(this, MPAndroidChart.class);
            startActivity(myIntent);
        }
        return super.onOptionsItemSelected(item);
    }

}