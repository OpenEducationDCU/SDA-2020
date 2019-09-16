package edu.dartmouth.cs.binddemo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class MyService extends Service {
	private NotificationManager mNotificationManager;
	private Timer mTimer = new Timer();
	private int counter = 0, incrementBy = 1;
	private static boolean isRunning = false;
	public static final String CHANNEL_ID = "notification channel";


	private List<Messenger> mClients = new ArrayList<Messenger>(); // Keeps
																	// track of
																	// all
																	// current
																	// registered
																	// clients.
	public static final int MSG_REGISTER_CLIENT = 1;
	public static final int MSG_UNREGISTER_CLIENT = 2;
	public static final int MSG_SET_INT_VALUE = 3;
	public static final int MSG_SET_STRING_VALUE = 4;
	private static final String TAG = "MyService";

	// Reference to a Handler, which others can use to send messages to it. This
	// allows for the implementation of message-based communication across
	// processes, by creating a Messenger pointing to a Handler in one process,
	// and handing that Messenger to another process.

	// Messenger is the channel which carries the Messages send between the server and app
    // ***there are two Messengers (server and client each has one), and this one is server's Messenger.
	// but why use a list?

	private final Messenger mMessenger = new Messenger(new IncomingMessageHandler()); // Target we publish for clients to
	// recall what is a Handler - A Handler allows you to send and process Message and Runnable objects
	// When posting or sending to a Handler, you can either allow the item to be processed as soon as
	// the message queue is ready to do so, or specify a delay before it gets processed or absolute time for it to be processed.
	// There are two main uses for a Handler: (1) to schedule messages and runnables to be executed as some point
	// in the future: post(Runnable), postAtTime(Runnable, long), postDelayed(Runnable, long), sendMessage(Message), sendMessageAtTime(Message, long)
	// (2) to enqueue an action (e.g. Runnable) to be performed on a different thread than your own.
	//http://developer.android.com/intl/es/reference/android/os/Handler.html
	private class IncomingMessageHandler extends Handler {
		// must implement this to receive messages.
        // here you get the Message from the Messenger recovered by msg.replyTo (or Messenger used by the client
        // or MainActivity.java). The Messenger from a certain client is stored in a list when the client binds to
        // the server, and is removed from the list when the bind is terminated.
        // *** the message receives from this Handler is also carried by the server's mMessenger defined in
        // line 49 in this file. See line 127 in MainActivity.java

		@Override
		public void handleMessage(Message msg) {
			Log.d(TAG, "S:handleMessage: " + msg.what + msg.replyTo);
       			switch (msg.what) {
				case MSG_REGISTER_CLIENT:
					Log.d(TAG, "S: RX MSG_REGISTER_CLIENT:mClients.add(msg.replyTo) ");
					mClients.add(msg.replyTo);//replyTo is the Messanger, that carrys the Message over.
					break;
				case MSG_UNREGISTER_CLIENT:
					Log.d(TAG, "S: RX MSG_REGISTER_CLIENT:mClients.remove(msg.replyTo) ");
					mClients.remove(msg.replyTo);// each client has a dedicated Messanger to communicae with ther server.
					break;
				case MSG_SET_INT_VALUE:
					incrementBy = msg.arg1;
					break;
				default:
					super.handleMessage(msg);
			}
		}
	}

    // Multiple clients can connect to the service at once. However, the system calls your service's onBind() method
     // to retrieve the IBinder only when the first client binds. The system then delivers the same IBinder to any
     //additional clients that bind, without calling onBind() again.

	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG, "S:onBind() - return mMessenger.getBinder()");
		// getBinder()
		// Return the IBinder that this Messenger is using to communicate with
		// its associated Handler; that is, IncomingMessageHandler().
        // *** onBind() is only called once when a bind is established (e.g. bindService()
        // is called by the client. onBind() wont be called again no matter how many times
        // bindService() is called unless the service is stopped (e.g. stopService()) and started again.
		return mMessenger.getBinder();// Retrieve the IBinder that this Messenger is using to communicate with its associated Handler.

	}

	//*********************************************************//
	//*********************************************************//
	//*********************************************************//

	// onCreate() will be executed only once no matter how many times the START SERVICE button is clicked
	// onStartCommand() will be executed every time the START SERVICE button is clicked

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "S:onCreate(): Service Started.");
		showNotification();
		mTimer.scheduleAtFixedRate(new MyTask(), 0, 1000L);
		isRunning = true;
	}

	// onCreate vs onStartCommand: http://stackoverflow.com/questions/14182014/android-oncreate-or-onstartcommand-for-starting-service
	// onCreate() is called when the Service object is instantiated (ie: when the service is created).
	// You should do things in this method that you need to do only once (ie: initialize some variables, etc.).
	//
	// onStartCommand() is called every time a client starts the service using startService(Intent intent).
	// This means that onStartCommand() can get called multiple times.

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "S:onStartCommand(): Received start id " + startId + ": " + intent);
		return START_STICKY; // Run until explicitly stopped.
	}

	@TargetApi(Build.VERSION_CODES.O)
	private void showNotification() {
		NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "channel name", NotificationManager.IMPORTANCE_DEFAULT);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, MainActivity.class), 0);// this is the main app page it will show by clicking the notification
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
				.setContentTitle(this.getString(R.string.service_label))
				.setContentText(getResources().getString(R.string.service_started))
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentIntent(contentIntent);
		Notification notification = notificationBuilder.build();
		notification.flags = notification.flags | Notification.FLAG_ONGOING_EVENT;

		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mNotificationManager.createNotificationChannel(notificationChannel);

		mNotificationManager.notify(0, notification);

	}

	private void sendMessageToUI(int intvaluetosend) {
		Log.d(TAG, "S:sendMessageToUI" + + mClients.size());
		Iterator<Messenger> messengerIterator = mClients.iterator();
		// after BIND TO SERVICE is clicked mClients.size() is 1; after UNBIND FROM SERVICE is
		// clicked, mClients.size() is 0. Messenger is used to send(Message)
		while (messengerIterator.hasNext()) {
			Messenger messenger = messengerIterator.next();
			try {
				// Send data as an Integer
				Log.d(TAG, "S:TX MSG_SET_INT_VALUE");

				// arg1 and arg2 are lower-cost alternatives to using setData() if you only need to store a few integer values.
				// public static Message obtain(Handler h, int what, int arg1, int arg2) what - User-defined message code so that the recipient can identify what this message is about.
				Message msg_int = Message.obtain(null, MSG_SET_INT_VALUE,intvaluetosend, 0);
				messenger.send(msg_int);

				Bundle bundle = new Bundle();//Bundle is generally used for passing data between various activities of android. It depends on you what type of values you want to pass, but bundle can hold all types of values, and pass to the new activity.
				bundle.putString("str1", "ab" + intvaluetosend + "cd");
                // you need to tell the client what type of data it receives.Here it is MSG_SET_STRING_VALUE type.
				Message msg_str = Message.obtain(null, MSG_SET_STRING_VALUE);
				msg_str.setData(bundle);
				Log.d(TAG, "S:TX MSG_SET_STRING_VALUE");
				messenger.send(msg_str);

			} catch (RemoteException e) {
				// The client is dead. Remove it from the list.
				mClients.remove(messenger);
			}
		}
	}

	public static boolean isRunning() {
		return isRunning;
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "S:onDestroy():Service Stopped");
		super.onDestroy();
		if (mTimer != null) {
			mTimer.cancel();
		}
		counter = 0;
		mNotificationManager.cancelAll(); // Cancel the persistent notification.
		isRunning = false;
	}

	// ////////////////////////////////////////
	// Nested classes
	// ///////////////////////////////////////

	/**
	 *  TimerTask implements Runnable and this explains why we need to Override run()
	 */
	private class MyTask extends TimerTask {
		@Override
		public void run() {
			Log.d(TAG, "T:MyTask():Timer doing work." + counter);
			try {
				counter += incrementBy;
				sendMessageToUI(counter);

			} catch (Throwable t) { // you should always ultimately catch all
									// exceptions in timer tasks.
				Log.e("TimerTick", "Timer Tick Failed.", t);
			}
		}
	}


}