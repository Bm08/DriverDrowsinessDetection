package com.project.driverdrowsinessdetectionsystem;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import android.app.Activity;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FaceTracker extends Tracker<Face> {
    private static final float THRESHOLD = 0.4f;
    private static final int INTERVAL = 1000; // 1 second
    private static final int MAX = 3; // 3 seconds
    private Handler mHandler;
    private String id = "manthan";
    private static final int EYES_OPEN = 1;
    private static final int EYES_CLOSED = 2;
    private int currentStatus = 0;
    private int count = 0;
    private Context context;
    private boolean isAlarmSent = false;
    private Ringtone ringtone;

    private boolean isSmsSent = false;


    FaceTracker(Context context) {
        this.context = context;
        mHandler = new Handler();
        Uri alarmPath = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtone = RingtoneManager.getRingtone(context, alarmPath);
        startRepeatingTask();
    }

    void stopThread() {
        stopRepeatingTask();
    }

    @Override
    public void onUpdate(FaceDetector.Detections<Face> detectionResults, Face face) {
        if (face.getIsLeftEyeOpenProbability() < THRESHOLD || face.getIsRightEyeOpenProbability() < THRESHOLD) {
            currentStatus = EYES_CLOSED;
            Log.i(TAG, "CLOSE");
        } else {
            currentStatus = EYES_OPEN;
            Log.i(TAG, "OPEN");
        }
    }

    @Override
    public void onMissing(Detector.Detections<Face> detections) {
        super.onMissing(detections);
    }

    @Override
    public void onDone() {
        super.onDone();
    }

    private Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                if (currentStatus == EYES_CLOSED) {
                    count++;
                    if (count >= MAX) {
                        if (!isAlarmSent) {
                            isAlarmSent = true;
                            playAlarm();
                        }
                    }
                } else {
                    isAlarmSent = false;
                    stopAlarm();
                    count = 0;
                }
            } finally {
                mHandler.postDelayed(mStatusChecker, INTERVAL);
            }
        }
    };

    private void startRepeatingTask() {
        mStatusChecker.run();
    }

    private void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }

    private void playAlarm() {
        if (ringtone != null && !ringtone.isPlaying()) {
            ringtone.play();
        }
        if (!isSmsSent) {
            isSmsSent = true;
            register();
        }
    }

    private void stopAlarm() {
        if (ringtone != null && ringtone.isPlaying()) {
            ringtone.stop();
        }
    }

    @SuppressLint("NewApi")
    private void register() {
        String type="notify";
        BackGroundWorker backGroundWorker = new BackGroundWorker(this.context);
        backGroundWorker.execute(type,id);

    }
}