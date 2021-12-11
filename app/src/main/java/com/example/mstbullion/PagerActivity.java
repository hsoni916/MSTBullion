package com.example.mstbullion;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.firestore.auth.User;

import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class PagerActivity extends AppCompatActivity {
    ImageView BankDetails,Records,Account;
    ImageButton LogOut;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewPager viewPager;
        setContentView(R.layout.home);
        viewPager = findViewById(R.id.container1);
        BankDetails = findViewById(R.id.BankDetails);
        Records = findViewById(R.id.Records);
        Account = findViewById(R.id.Account);
        LogOut = findViewById(R.id.logout);
        DashboardPager dashboardPager = new DashboardPager(getSupportFragmentManager());
        viewPager.setAdapter(dashboardPager);
        viewPager.setCurrentItem(0);
        BankDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bankdetails = new Intent(v.getContext(),BankDetailsActivity.class);
                startActivity(bankdetails);
            }
        });
        Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start Account login activity.
                Intent useraccount = new Intent(v.getContext(), UserAccount.class);
                startActivity(useraccount);
            }
        });
        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }



}
