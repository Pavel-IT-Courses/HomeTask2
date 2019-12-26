package com.gmail.pavkascool.hw6_service_and_receiver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public final static String ACTION1 = "com.gmail.pavkascool.action1";
    public final static String ACTION2 = "android.intent.action.TIME_TICK";
    public final static String ACTION3 = "com.gmail.pavkascool.action3";
    public final static String ACTION4 = "android.intent.action.TIMEZONE_CHANGED";

    private ContextReceiver contextReceiver = new ContextReceiver();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IntentFilter intentFilter = new IntentFilter(ACTION1);
        intentFilter.addAction(ACTION2);
        registerReceiver(contextReceiver,intentFilter);
    }

    public void action1(View view) {
        sendBroadcast(new Intent(ACTION1));
    }

//    public void action2(View view) {
//        sendBroadcast(new Intent(ACTION2));
//    }

    public void action3(View view) {
        sendBroadcast(new Intent(ACTION3));
    }

//    public void action4(View view) {
//        sendBroadcast(new Intent(ACTION4));
//    }

    public void unregister(View view) {
        try {
            unregisterReceiver(contextReceiver);
        } catch (Exception e) {
            Toast.makeText(this, "Already Unregistered", Toast.LENGTH_SHORT);
        }
    }
}
