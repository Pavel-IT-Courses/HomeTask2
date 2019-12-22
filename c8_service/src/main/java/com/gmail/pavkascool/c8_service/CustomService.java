package com.gmail.pavkascool.c8_service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.TimeUnit;

public class CustomService extends Service {

    public static final String TAG = "MY_TAG";
    public CustomService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.d(TAG, "onBind");
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStart " + startId);
        doWork(startId);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    public void doWork(final int startId) {
        Log.d(TAG, "Start Id = " + startId);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int j = 0; j < 5; j++) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                stopSelf(startId);
            }
        });
        thread.run();

        stopSelf(startId);
    }

}
