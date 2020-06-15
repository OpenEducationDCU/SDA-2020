/*
 * When "Load Icon" Button is pressed throws 
 * android.view.ViewRootImpl$CalledFromWrongThreadException: 
 * Only the original thread that created a view hierarchy can touch its views.
 */

package course.examples.threading.threadingsimple;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class SimpleThreadingExample extends Activity {

    private static final String TAG = "SimpleThreadingExample";

    private Bitmap mBitmap;
    private ImageView mIView;
    private final int mDelay = 5000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mIView = findViewById(R.id.imageView);
    }

    //when the button is clicked a new thread is created that tries to add an image to our imageView.
    public void onClickLoadButton(@SuppressWarnings("unused") View v) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(mDelay);
                } catch (InterruptedException e) {
                    Log.e(TAG, e.toString());
                }

                mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.painter);
                //sendToMainUI(mBitmap);

                // This doesn't work in Android because anything related to UI must run on the main thread
                mIView.setImageBitmap(mBitmap);
            }
        }).start();
    }

    //this method lets us send data from the thread to the mainUI thread.
    public void sendToMainUI(final Bitmap mBitmap) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mIView.setImageBitmap(mBitmap);
            }
        });
    }

    public void onClickOtherButton(@SuppressWarnings("unused") View v) {
        Toast.makeText(SimpleThreadingExample.this, "I'm Working",
                Toast.LENGTH_SHORT).show();
    }

}