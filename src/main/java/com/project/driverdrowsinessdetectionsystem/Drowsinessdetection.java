package com.project.driverdrowsinessdetectionsystem;

import android.content.Context;
import android.content.res.AssetManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class Drowsinessdetection {

   private  Context context;

    public void drowsiness() {
        Log.d(TAG,"Method started");

        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    Log.d("ERROR", e.toString());
                    Thread.currentThread().interrupt();
                } finally {
                    playAlarm(context);

                }
            }
        };

    }


    public void playAlarm(final Context context) {
        Log.d(TAG,"playAlarm starts");

            Uri path = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM, path);

            Ringtone r = RingtoneManager.getRingtone(context, path);
            r.play();
    }
}

