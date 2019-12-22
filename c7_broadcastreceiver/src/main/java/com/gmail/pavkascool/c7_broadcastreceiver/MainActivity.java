package com.gmail.pavkascool.c7_broadcastreceiver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private MyReceiver myReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction("com.gmail.pavkascool.MyReceiver.MY_ACTION");
//        myReceiver = new MyReceiver();
//        registerReceiver(myReceiver, intentFilter);
    }
}
