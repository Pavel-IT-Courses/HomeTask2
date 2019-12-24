package com.gmail.pavkascool.hw6_service_and_receiver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyService extends Service {

    public final static String FILE_NAME = "MyLogFile";


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MY TAG", "Service Started, id = " + startId + " " + getFilesDir());
        File file = new File(getFilesDir(), FILE_NAME);

        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            String logTime = new SimpleDateFormat("dd.MM.yy hh:mm:ss").format(new Date());
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            bw.append(logTime + ": " + intent.getStringExtra("action") + "\n");
            bw.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        stopSelf(startId);
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
