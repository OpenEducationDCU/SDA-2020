package androidcourse.examples.Threads.NoThreading;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NoThreadExample extends Activity {
	private Bitmap mBitmap;
	private ImageView mIView;
	private static final String TAG = "NoThreadExample";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mIView = findViewById(R.id.imageView);
		
		//added as Android does not allow network operation 
		//(including HttpClient and HttpUrlConnection) to execute on UI thread. 
		//This is my way around the restriction
		//ONLY USED FOR demonstrations
		//DO NOT USE IN YOUR CODE
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		final Button loadButton = findViewById(R.id.loadButton);
		loadButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				//call the method
				loadIcon();
				if (null != mBitmap) {
					mIView.setImageBitmap(mBitmap);
				}
			}
		});

		final Button otherButton = findViewById(R.id.otherButton);
		otherButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(NoThreadExample.this, "Toast Working",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void loadIcon() {

		try
		{
			URL url = new URL("https://www.dcu.ie/sites/default/files/images/highlight-box/Oscail_image2b_0.jpg");

		  //added to ensure takes a while
			int mDelay = 4000;
			Thread.sleep(mDelay);
		 
		 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        mBitmap = BitmapFactory.decodeStream(input);

		}
		catch (Exception e)
		{
			Log.e(TAG, "loadIcon: failed to decode the bitmap", e );

		}
	}

}
