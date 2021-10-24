package com.example.mstbullion;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class bullionoptions extends AppCompatActivity {
    ImageButton Parity,Records,Accounts,Alerts;
    private PopupWindow popupWindow;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bullionoptions);
        Parity = findViewById(R.id.margin);
        Records = findViewById(R.id.CustomerRecords);
        Accounts = findViewById(R.id.Customer);
        Alerts = findViewById(R.id.alert);


        Parity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeMargin(v);
            }
        });

    }

    private void ChangeMargin(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(inflater!=null){
            final View marginView = inflater.inflate(R.layout.changemargin,null);
            popupWindow = new PopupWindow(marginView, LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            popupWindow.setAnimationStyle(R.style.popup_animation);
            popupWindow.showAtLocation(v, Gravity.CENTER,0,0);
            popupWindow.setFocusable(true);
            popupWindow.update();
            Button cancel = marginView.findViewById(R.id.cancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
        }
    }
}
