package com.example.shivam.notifyme.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.shivam.notifyme.R;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
