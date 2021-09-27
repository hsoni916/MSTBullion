package com.example.mstbullion;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.auth.User;

public class UserAccount extends AppCompatActivity {
    EditText Business,Name,PhoneNumber,Password;
    Button CreateAccount;
    private DBManager dbManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbManager = new DBManager(this);
        dbManager.open();
        if(!dbManager.check()){
            setContentView(R.layout.user_account);
            Business = findViewById(R.id.Jewellers);
            Name = findViewById(R.id.User);
            PhoneNumber = findViewById(R.id.Phone);
            Password = findViewById(R.id.Password);
            CreateAccount = findViewById(R.id.CreateAccount);
            CreateAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Validate Input
                    UserProfile userProfile = new UserProfile();
                    String BName, UName, Phone, PasswordString;
                    BName = Business.getText().toString();
                    if(!BName.matches("^[a-zA-Z]+\\s?+[a-zA-Z]+$")){
                        Business.setError("No Spaces & No Numbers!");
                    }else{
                        userProfile.setBusiness(BName);
                        Business.setError(null);
                    }
                    UName = Name.getText().toString();
                    if(!UName.matches("^[a-zA-Z]+\\s?+[a-zA-Z]+$")){
                        Name.setError("No Spaces & No Numbers!");
                    }else{
                        userProfile.setName(UName);
                        Name.setError(null);
                    }
                    Phone = PhoneNumber.getText().toString();
                    if(!Phone.matches("^[0-9]{10}$")){
                        PhoneNumber.setError("Only Indian Mobile Numbers!");
                    }else{
                        userProfile.setPhone(Phone);
                        PhoneNumber.setError(null);
                    }
                    PasswordString = Password.getText().toString();
                    if(!PasswordString.isEmpty()){
                        dbManager.insert(UName, PasswordString, 0);
                        //0 signed in
                        //1 logged out
                    }
                    //Check for password.
                    if(Business.getError()!=null||Name.getError()!=null||PhoneNumber.getError()!=null){
                        //Set User Profile.
                    }
                }
            });
        }else{
            if(dbManager.usersignedIn()){
                Log.d("True reset","");
                if(dbManager.getUser()!=null) {
                    Log.d("True reset","2");
                    Log.d("User",dbManager.getUser());
                    resetLayoutSignIn(dbManager.getUser());
                }
            }else{
                setContentView(R.layout.user_account_signin);
                Name = findViewById(R.id.User);
                Password = findViewById(R.id.Password);
                CreateAccount = findViewById(R.id.LogIn);
                CreateAccount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String UserName,PasswordString;
                        UserName = Name.getText().toString();
                        PasswordString = Password.getText().toString();
                        if(dbManager.usersignin(UserName,PasswordString)){
                            Log.d("Login Successfull","0");
                            Toast.makeText(v.getContext(),"Login Successful",Toast.LENGTH_LONG).show();
                            resetLayoutSignIn(UserName);
                        }
                    }
                });
            }
        }
    }

    private void resetLayoutSignIn(String userName) {
        setContentView(R.layout.user_account_logout);
        TextView username;
        Button logout;
        username = findViewById(R.id.User);
        logout = findViewById(R.id.LogIn);
        username.setText(userName);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbManager.logout(userName);
                startActivity(getIntent());
                finish();
                overridePendingTransition(0, 0);
            }
        });
    }
}
