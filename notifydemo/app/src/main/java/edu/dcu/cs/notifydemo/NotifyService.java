package edu.dcu.cs.notifydemo;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.IBinder;

public class NotifyService extends Service {

	final static String ACTION = "NotifyServiceAction";
	final static String STOP_SERVICE_BROADCAST_KEY="StopServiceBroadcastKey";
	final static int RQS_STOP_SERVICE = 1;
	//use a vibration pattern
	private long[] mVibratePattern = { 0, 200, 200, 300 };
	private Uri mSoundURI = null;

	NotifyServiceReceiver notifyServiceReceiver;


	//uncomment 1
	private final String dcuURL = "https://www.dcu.ie";


	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		notifyServiceReceiver = new NotifyServiceReceiver();
		super.onCreate();
		// Notification Sound on Arrival
		mSoundURI = Uri
				.parse("android.resource://" + getPackageName() + "/" + R.raw.alarm_rooster);
	}


	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ACTION);
		registerReceiver(notifyServiceReceiver, intentFilter);

		// Send Notification
		Context context = getApplicationContext();
		String notificationTitle = "Demo of Notification!";
		String notificationText = "Notification  and Feedback Unit: DCU Web Site";


		//uncomment to get app to go from drawer to Notifydemo app
		/*Intent myIntent= new Intent(getApplicationContext(),
				NotifyActivity.class);
		PendingIntent pendingIntent  = PendingIntent.getActivity(getApplicationContext(), 0,
				myIntent, Intent.FLAG_ACTIVITY_NEW_TASK);*/


		Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(dcuURL));
		myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pendingIntent
				= PendingIntent.getActivity(getBaseContext(),
				0, myIntent,
				0);


		//initialise a notification
		Notification notification = null;

		//start the notification manager
		NotificationManager notificationManager =
				(NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		// The user-visible name of the channel.
		CharSequence channelName = "new notification channel";

		// The user-visible description of the channel.
		String channelDesc = "A new channel containing a notification";

		if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
			// The id of the channel.
			String  CHANNEL_ID = "my_channel_01";
			//add a new channel required for anything higher than 26.
			NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, channelName,NotificationManager.IMPORTANCE_HIGH);

			AudioAttributes myAudioAttribute = new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
					.setLegacyStreamType(AudioManager.STREAM_ALARM)
					.setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT).build();

			// Configure the notification channel.
			mChannel.setDescription(channelDesc);
			mChannel.enableLights(true);
			// colour of lights
			mChannel.setLightColor(Color.BLUE);
			mChannel.enableVibration(true);
			mChannel.setVibrationPattern(mVibratePattern);
			mChannel.setSound(mSoundURI,myAudioAttribute);

			if (notificationManager != null) {
				notificationManager.createNotificationChannel(mChannel);
			}

			// Create a notification and set the notification channel.
			notification = new Notification.Builder(this, CHANNEL_ID)
					.setTicker("Service Started")
					.setWhen(System.currentTimeMillis())
					.setContentTitle(notificationTitle)
					.setAutoCancel(true)
					.setContentText(notificationText)
					.setSmallIcon(R.drawable.ic_stat_access_alarm)
					.setChannelId(CHANNEL_ID)
					.setContentIntent(pendingIntent).build();

		} else {
			notification = new Notification.Builder(this)
					.setTicker("Service Started")
					.setWhen(System.currentTimeMillis())
					.setContentTitle(notificationTitle)
					.setLights(Color.BLUE, 5000, 5000)
					.setAutoCancel(true)
					.setSound(mSoundURI)
					.setVibrate(mVibratePattern)
					.setContentText(notificationText)
					.setSmallIcon(R.drawable.ic_stat_access_alarm)
					.setContentIntent(pendingIntent).build();

		}

		notification.flags = notification.flags
				| Notification.FLAG_ONGOING_EVENT | Notification.FLAG_SHOW_LIGHTS;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		if (notificationManager != null) {
			notificationManager.notify(0, notification);
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public class NotifyServiceReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			int rqs = arg1.getIntExtra(STOP_SERVICE_BROADCAST_KEY, 0);
			
			if (rqs == RQS_STOP_SERVICE){
				stopSelf();
				((NotificationManager) getSystemService(NOTIFICATION_SERVICE))
				.cancelAll();
			}
		}
	}
	
}




