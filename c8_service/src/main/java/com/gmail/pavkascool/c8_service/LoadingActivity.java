package com.gmail.pavkascool.c8_service;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoadingActivity extends AppCompatActivity implements View.OnClickListener {

    private Button start, stop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        start = findViewById(R.id.b4);
        stop = findViewById(R.id.b5);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, CustomIntentService.class);
        switch(v.getId()) {

            case R.id.b4:
                startService(intent);
                break;
            case R.id.b5:
                stopService(intent);
        }
    }
}
