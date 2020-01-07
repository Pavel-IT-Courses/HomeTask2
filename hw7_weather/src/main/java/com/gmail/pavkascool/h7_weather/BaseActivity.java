package com.gmail.pavkascool.h7_weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener{

    private Button add;
    private List<String> locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        add = findViewById(R.id.add);
        add.setOnClickListener(this);

        locations = (List<String>)getLastCustomNonConfigurationInstance();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, LocatorActivity.class);
        startActivity(intent);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return locations;
    }
}
