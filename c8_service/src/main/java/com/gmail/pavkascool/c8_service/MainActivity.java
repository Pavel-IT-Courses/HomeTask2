package com.gmail.pavkascool.c8_service;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button start, stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start = findViewById(R.id.b1);
        start.setOnClickListener(this);
        stop = findViewById(R.id.b2);
        stop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, CustomService.class);;
        switch(v.getId()) {
            case R.id.b1:
                startService(intent);
                break;
                case R.id.b2:
                    stopService(intent);
                break;
        }
    }
}
