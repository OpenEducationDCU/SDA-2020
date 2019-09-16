package edu.dartmouth.cs.binddemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ServiceConnection {
    private static final String TAG = "MainActivity";
    private final Messenger mMessenger = new Messenger(new IncomingMessageHandler());
    // Messenger implements Parcelable, which is basically a container for a message (data and object references)
    // that can be sent through an IBinder.
    boolean mIsBound;
    private Button btnStart, btnStop, btnBind, btnUnbind, btnUpby1, btnUpby10;
    private TextView textStatus, textIntValue, textStrValue;
    private ServiceConnection mConnection = this; // as we implement ServiceConnection
    private Messenger mServiceMessenger = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "C:onCreate");
        setContentView(R.layout.main);
        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
        btnBind = findViewById(R.id.btnBind);
        btnUnbind = findViewById(R.id.btnUnbind);
        textStatus = findViewById(R.id.textStatus);
        textIntValue = findViewById(R.id.textIntValue);
        textStrValue = findViewById(R.id.textStrValue);
        btnUpby1 = findViewById(R.id.btnUpby1);
        btnUpby10 = findViewById(R.id.btnUpby10);

        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnBind.setOnClickListener(this);
        btnUnbind.setOnClickListener(this);
        btnUpby1.setOnClickListener(this);
        btnUpby10.setOnClickListener(this);

        mIsBound = false; // by default set this to unbound
        automaticBind();
    }

    private void automaticBind() {
        if (MyService.isRunning()) {
            Log.d(TAG, "C:MyService.isRunning: doBindService()");
            doBindService();
        }
    }

    private void doBindService() {
        Log.d(TAG, "C:doBindService()");

        // pass mConnection to tell the server it is this activity that is trying to bind to the server.
        //
        // For bindService(Intent, ServiceConnection, flag) if flag Context.BIND_AUTO_CREATE is used
        // it will bind the service and start the service, but if "0" is used, method will return true and
        // will not start service until a call like startService(Intent) is made to start the service.
        // One of the common use of "0" is in the case where an activity to connect to a local service if that
        // service is running, otherwise you can start the service.

        bindService(new Intent(this, MyService.class), mConnection, Context.BIND_AUTO_CREATE);// http://stackoverflow.com/questions/14746245/use-0-or-bind-auto-create-for-bindservices-flag
        mIsBound = true;
        textStatus.setText("Binding.");


    }

    private void doUnbindService() {
        Log.d(TAG, "C:doUnBindService()");
        if (mIsBound) {
            // If we have received the service, and hence registered with it,
            // then now is the time to unregister.
            if (mServiceMessenger != null) {
                try {
                    Message msg = Message.obtain(null, MyService.MSG_UNREGISTER_CLIENT);//  obtain (Handler h, int what) - 'what' is the tag of the message, which will be used in line 72 in MyService.java. Returns a new Message from the global message pool. More efficient than creating and allocating new instances.
                    //Log.d(TAG, "C: TX MSG_UNREGISTER_CLIENT");
                    msg.replyTo = mMessenger;
                    mServiceMessenger.send(msg);// need to use the server messenger to send the message to the server
                } catch (RemoteException e) {
                    // There is nothing special we need to do if the service has
                    // crashed.
                }
            }
            // Detach our existing connection.
            unbindService(mConnection);
            mIsBound = false;
            textStatus.setText("Unbinding.");
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.d(TAG, "C:onServiceConnected()");
        // this is the Messenger defined in line 49 of MyService.java
        mServiceMessenger = new Messenger(service);
        textStatus.setText("Attached.");
        try {
            Message msg = Message.obtain(null, MyService.MSG_REGISTER_CLIENT);
            msg.replyTo = mMessenger;
            Log.d(TAG, "C: TX MSG_REGISTER_CLIENT");
            // We use service Messenger to send the msg to the Server
            mServiceMessenger.send(msg);
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException", e);
        }
    }

    // This is called when the connection with the service has been
    // established, giving us the service object we can use to
    // interact with the service.

    // bindService(new Intent(this, MyService.class), mConnection,
    // Context.BIND_AUTO_CREATE) calls onbind in the service which
    // returns an IBinder to the client.

    // this class implements ServiceConnection so onServiceConnected needs to be implemented.
    // onServiceConnected() is called when binding to the server is successful.

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.d(TAG, "C:onServiceDisconnected()");
        // This is called when the connection with the service has been
        // unexpectedly disconnected - process crashed.
        mServiceMessenger = null;
        textStatus.setText("Disconnected.");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("textStatus", textStatus.getText().toString());
        outState.putString("textIntValue", textIntValue.getText().toString());
        outState.putString("textStrValue", textStrValue.getText().toString());
    }

    // save data across orientation changes.
    // comment them out and see what will happen (textviews will disappear and appear)

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            textStatus.setText(savedInstanceState.getString("textStatus"));
            textIntValue.setText(savedInstanceState.getString("textIntValue"));
            textStrValue.setText(savedInstanceState.getString("textStrValue"));
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * Send data to the service
     *
     * @param intvaluetosend The data to send
     */
    private void sendMessageToService(int intvaluetosend) {
        if (mIsBound) {
            if (mServiceMessenger != null) {
                try {
                    Message msg = Message.obtain(null, MyService.MSG_SET_INT_VALUE, intvaluetosend, 0);// http://developer.android.com/intl/es/reference/android/os/Message.html#obtain()
                    msg.replyTo = mMessenger;
                    // we use the server messenger to send msg to the server
                    mServiceMessenger.send(msg);
                } catch (RemoteException e) {
                }
            }
        }
    }

    /**
     * Handle button clicks
     */
    @Override
    public void onClick(View v) {
        if (v.equals(btnStart)) {
            Log.d(TAG, "C:startService()");
            startService(new Intent(MainActivity.this, MyService.class));
        } else if (v.equals(btnStop)) {
            Log.d(TAG, "C:stopService()");
            doUnbindService();
            stopService(new Intent(MainActivity.this, MyService.class));
        } else if (v.equals(btnBind)) {
            Log.d(TAG, "C:Bind");
            doBindService();
        } else if (v.equals(btnUnbind)) {
            Log.d(TAG, "C:Unbind");
            doUnbindService();
        } else if (v.equals(btnUpby1)) {
            sendMessageToService(1);
        } else if (v.equals(btnUpby10)) {
            sendMessageToService(10);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "C:onDestroy()");
        try {
            doUnbindService();
        } catch (Throwable t) {
            Log.e(TAG, "Failed to unbind from the service", t);
        }
    }

    private class IncomingMessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "C:IncomingHandler:handleMessage " + msg.replyTo);
            switch (msg.what) {
                case MyService.MSG_SET_INT_VALUE:
                    Log.d(TAG, "C: RX MSG_SET_INT_VALUE");
                    // msg.arg1 here as only arg1 was used to store data in the server class.
                    textIntValue.setText("Int Message: " + msg.arg1);
                    break;
                case MyService.MSG_SET_STRING_VALUE:
                    // getString(key) -> str1 is the key of the key value pair we used in the server side.
                    String str1 = msg.getData().getString("str1");
                    Log.d(TAG, "C:RX MSG_SET_STRING_VALUE");
                    textStrValue.setText("Str Message: " + str1);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    // Note, when client sends msg to the server, it needs to specify who it is by doing "msg.replyTo =
    // mMessenger". Otherwise the server won't be able to know where the return msg needs to be
    // sent to. However, when the server sends msg to the client the server does not need
    // to do the same (e.g. msg.replyTo = mMessenger) as the client always know who the
    // server is upon receiving the onServiceConnected() call.

}