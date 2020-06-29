/*
 * When "Load Icon" Button is pressed throws 
 * android.view.ViewRootImpl$CalledFromWrongThreadException: 
 * Only the original thread that created a view hierarchy can touch its views.
 */
package course.examples.threading.threadingJava;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * contains code adapted from https://mkyong.com/java/java-executorservice-examples/
 * Author Chris Coughlan 2020
 */
public class JavaThreadingExample extends Activity {

    private static final String TAG = "JavaThreadingExample";

    private Bitmap mBitmap;
    private ImageView mIView;
    private final int mDelay = 5000;
    TextView myTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mIView = findViewById(R.id.imageView);
        myTextView = findViewById(R.id.threadView);

    }

    //when the button is clicked a new thread is created that tries to add an image to our imageView.
    public void onClickLoadButton(@SuppressWarnings("unused") View v) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(mDelay);
                } catch (InterruptedException e) {
                    Log.e(TAG, e.toString());
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mIView.setImageBitmap(BitmapFactory.decodeResource(getResources(),
                                R.drawable.painter));
                    }
                });
            }
        });
        //close it
        executor.shutdown();
    }

    public void onClickAsyncButton(@SuppressWarnings("unused") View v) {
        //runs immediately clears the text before call any future task
        myTextView.setText("");

        //opens executor with 5 threads.
        ExecutorService executor = Executors.newFixedThreadPool(5);

        //Future thread
        Future<Boolean> futureTask = executor.submit(new Callable<Boolean>() {
            //this returns the integer and calls a callable thread.
            @Override
            public Boolean call() {
                try {
                    Thread.sleep(mDelay);
                } catch (InterruptedException e) {
                    Log.e(TAG, e.toString());
                }
                runToastOnUI("This is a callable thread");
                return true;
            }
        });

        //new thread code is before
        executor.submit(new Runnable() {
            @Override
            public void run() {
                runToastOnUI("before the future result");
            }
        });

        try {

            //call future task, timeout if it doesn't return a result within 10 seconds.
            final Boolean result = futureTask.get(10, TimeUnit.SECONDS);

            //everything below here will execute only after the Future is successful
            executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        runToastOnUI("This is the future result: " + result);
                    }
            });

            //new thread after
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    runToastOnUI("after the future result");
                }
            });

        } catch (InterruptedException | ExecutionException | TimeoutException e) {//interrupted, exception or timeout

            Log.i(TAG, "onClickAsyncButton: "+ e);
        }
        finally {
            // shut down the executor manually
            executor.shutdown();
        }
    }

    public void runToastOnUI(final String toastString){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                myTextView.append(toastString+"\n");
            }
        });
    }

    public void onClickOtherButton(@SuppressWarnings("unused") View v) {
        Toast.makeText(JavaThreadingExample.this, "I'm Working",
                Toast.LENGTH_SHORT).show();
    }

}