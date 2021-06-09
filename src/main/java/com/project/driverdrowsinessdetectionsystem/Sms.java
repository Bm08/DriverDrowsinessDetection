package com.project.driverdrowsinessdetectionsystem;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;

import android.app.PendingIntent;
import android.content.Intent;
import android.telephony.SmsManager;
//import android.view.Menu;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

public class Sms extends Activity {

    final static String TAG = "Sms";

    SmsManager smsManager;

    private boolean isSmsSent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);

        smsManager = SmsManager.getDefault();


        //Performing action on button click

       // String no = mobileno.getText().toString();

        Intent intent = getIntent();
        String mobile = intent.getStringExtra("umobile");

        String msg = "Message From DDDS: drowsiness detected, Driver detected sleepy while driving";

        if (!isSmsSent) {
            isSmsSent = true;
            sendSMS(mobile, msg);
            Toast.makeText(getApplicationContext(), "Message Sent successfully!",
                    Toast.LENGTH_LONG).show();
        }



    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    protected void sendSMS(String mobile, String msg) {

        final String SENT = "SMS Sent";
        final String RECEIVED = "SMS Recieved";

        Intent intent = new Intent(SENT);
        Intent intentr = new Intent(RECEIVED);

        PendingIntent pisent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pireceived = PendingIntent.getBroadcast(this, 0, intentr, PendingIntent.FLAG_UPDATE_CURRENT);

        registerReceiver(new SMSSentReceiver(), new IntentFilter(SENT));
        registerReceiver(new SMSReceivedReceiver(), new IntentFilter(RECEIVED));

        smsManager.sendTextMessage(mobile, null, msg, pisent, pireceived);
    }

    class SMSSentReceiver  extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            switch(getResultCode()) {
                case Activity.RESULT_OK:
                    Log.d(TAG, "SMS was sent" );
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    Log.d(TAG, "Generic failure");
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    Log.d(TAG, "No service");
                    break;
            }
        }
    }

    class SMSReceivedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch(getResultCode()) {
                case Activity.RESULT_OK:
                    Log.d(TAG, "SMS Delivered");
                    break;
                case  Activity.RESULT_CANCELED:
                    Log.d(TAG, "SMS Sending cancelled");
                    break;
            }
        }
    }

}

