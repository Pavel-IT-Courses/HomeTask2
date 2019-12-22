package com.gmail.pavkascool.c7_broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    private final static String myAction = "com.gmail.pavkascool.MyReceiver.MY_ACTION";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if((Intent.ACTION_LOCALE_CHANGED).equals(action)) {
            Toast.makeText(context, "Locale is changed", Toast.LENGTH_SHORT).show();
        }
        else if((Intent.ACTION_TIMEZONE_CHANGED).equals(action)) {
            Toast.makeText(context, "TimeZone is changed", Toast.LENGTH_SHORT).show();
        }
        else if(myAction.equals(action)) {
            System.out.println("THIS CASE");
            String text = intent.getStringExtra("text");
            Toast.makeText(context, "My Action! + " + text, Toast.LENGTH_SHORT).show();
        }
    }
}
