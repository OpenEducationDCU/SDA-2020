package com.example.simpleasynctask;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

/**
 *  Source: https://www.journaldev.com/9708/android-asynctask-example-tutorial
 */
public class MainActivity extends AppCompatActivity {
    private EditText time;
    private TextView finalResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get the time editText view
        time = findViewById(R.id.in_time);
        //get the button view
        Button button = findViewById(R.id.btn_run);
        //get the display result Textview
        finalResult = findViewById(R.id.tv_result);

        //set a listener on the button.
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //this runs the Asynchronous task we created below.
                AsyncTaskRunner runner = new AsyncTaskRunner();
                String sleepTime = time.getText().toString();
                runner.execute(sleepTime);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

@Override
protected String doInBackground(String... params) {
    publishProgress("Sleeping..."); // Calls onProgressUpdate()
    String resp;
    try {
        int time = Integer.parseInt(params[0])*1000;

        Thread.sleep(time);
        resp = "Slept for " + params[0] + " seconds";
    } catch (Exception e) {
        e.printStackTrace();
        resp = e.getMessage();
    }
    return resp;
}

@Override
protected void onPostExecute(String result) {
    // execution of result of Long time consuming operation
    progressDialog.dismiss();
    finalResult.setText(result);
}

@Override
protected void onPreExecute() {
    progressDialog = ProgressDialog.show(MainActivity.this,
            "ProgressDialog",
            "Wait for "+time.getText().toString()+ " seconds");
}

@Override
protected void onProgressUpdate(String... text) {
    finalResult.setText(text[0]);
}
    }
}