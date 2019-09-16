package com.example.networkurl;


import android.annotation.SuppressLint;
import android.os.Bundle;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class NetworkingURLActivity extends Activity {
    @SuppressLint("StaticFieldLeak")
    private static TextView mTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.textView1);

        final Button loadButton = (Button) findViewById(R.id.button1);
        loadButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new HttpGetTask().execute();
            }
        });
    }

    private static class HttpGetTask extends AsyncTask<Void, Void, String> {

        private static final String TAG = "HttpGetTask";

        // Get your own user name at http://www.geonames.org/login
        private static final String USER_NAME = "aporter";
        URL mUrl;
        {
            try {
                mUrl = new URL("http://api.geonames.org/earthquakesJSON?north=44.1&south=-9.9&east=-22.4&west=55.2&username=" + USER_NAME);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            String data = "";
            HttpURLConnection httpUrlConnection = null;
            try {

                httpUrlConnection = (HttpURLConnection) mUrl.openConnection();
                int responseCode = httpUrlConnection.getResponseCode();
                Log.i(TAG, "doInBackground: "+responseCode);

                //if the responsecode is good then send the data to the readstream
                if(responseCode == HttpURLConnection.HTTP_OK){
                    data = readStream(httpUrlConnection.getInputStream());
                    Log.i(TAG, "doInBackground: " + data);
                }else{
                    Log.i(TAG, "doInBackground:  failing to connect");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (null != httpUrlConnection)
                    httpUrlConnection.disconnect();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            mTextView.setText(result);
        }

        private String readStream(InputStream in) {
            BufferedReader reader = null;
            StringBuilder data = new StringBuilder("");
            try {
                reader = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    data.append(line);
                }
            } catch (IOException e) {
                Log.e(TAG, "IOException");
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return data.toString();
        }
    }
}