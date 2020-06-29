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

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates a PieChart<br>
 * Created using information from these sources:<br>
 * https://weeklycoding.com/mpandroidchart/
 * https://javapapers.com/android/android-chart-example-app-using-mpandroidchart/ *(older version of MPAndroidChart)
 * https://inducesmile.com/android-programming/how-to-draw-barchart-using-mpandroidchart-in-android/
 * @author Chris Coughlan
 * @version 1.0
 * @since 2020
 */
public class MPAndroidPie extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie);

        //add a toolbar
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("MPAndroidChart PieChart");

        PieChart pieChart = findViewById(R.id.piechart);

        PieDataSet dataSet = new PieDataSet(getData(), "Monthly Result");
        //give the bars a border and a colour set.
        dataSet.setSliceSpace(0.9f);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        //create a new BarData object from our DataSet
        PieData pieData = new PieData(dataSet);

        pieChart.setData(pieData);
        pieChart.animateXY(5000, 5000);
    }

    private List<PieEntry> getData(){
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(20, "Jan"));
        entries.add(new PieEntry(15, "Feb"));
        entries.add(new PieEntry(20, "March"));
        entries.add(new PieEntry(30, "April"));
        entries.add(new PieEntry(15, "May"));
        return entries;
    }
}