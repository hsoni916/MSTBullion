package com.example.mstbullion;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class bullionoptions extends AppCompatActivity {
    ImageButton Parity,Records,Accounts,Alerts;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bullionoptions);
        Parity = findViewById(R.id.margin);
        Records = findViewById(R.id.CustomerRecords);
        Accounts = findViewById(R.id.Customer);
        Alerts = findViewById(R.id.alert);

    }
}
