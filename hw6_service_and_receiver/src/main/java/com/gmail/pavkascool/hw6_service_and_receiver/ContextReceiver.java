package com.gmail.pavkascool.hw6_service_and_receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class ContextReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent data = new Intent(context, MyService.class);
        data.putExtra("action", intent.getAction());
        context.startService(data);
    }
}
