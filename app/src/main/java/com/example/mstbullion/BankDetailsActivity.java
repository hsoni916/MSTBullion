package com.example.mstbullion;

import static android.icu.text.DisplayContext.LENGTH_SHORT;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class BankDetailsActivity extends AppCompatActivity {
TextView BankName,AccountName,AccountNumber,IFSC,Branch;
ImageView ShareDetails;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_details);
        BankName = findViewById(R.id.BankNameHolder);
        AccountName = findViewById(R.id.AccountNameHolder);
        AccountNumber = findViewById(R.id.AccountNoHolder);
        IFSC = findViewById(R.id.IFSCHolder);
        Branch = findViewById(R.id.BranchHolder);
        ShareDetails = findViewById(R.id.ShareBankDetails);
        ShareDetails.setEnabled(false);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("AdminOptions").document("BankDetails")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            if(task.getResult()!=null){
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if(documentSnapshot.get("BankName")!=null){
                                    BankName.setText(String.valueOf(documentSnapshot.get("BankName")));
                                }
                                if(documentSnapshot.get("AccountName")!=null){
                                    AccountName.setText(String.valueOf(documentSnapshot.get("AccountName")));
                                }
                                if(documentSnapshot.get("AccountNumber")!=null){
                                    AccountNumber.setText(String.valueOf(documentSnapshot.get("AccountNumber")));
                                }
                                if(documentSnapshot.get("IFSC")!=null){
                                    IFSC.setText(String.valueOf(documentSnapshot.get("IFSC")));
                                }
                                if(documentSnapshot.get("Branch")!=null){
                                    Branch.setText(String.valueOf(documentSnapshot.get("Branch")));
                                }
                                ShareDetails.setEnabled(true);
                            }
                        }
                    }
                });

        ShareDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager pm=getPackageManager();
                try {
                    Intent waIntent = new Intent(Intent.ACTION_SEND);
                    waIntent.setType("text/plain");
                    String text = "Bank:"+BankName.getText()+"\n"
                            +"A.C:"+AccountNumber.getText()+"\n"
                            +"Name:"+AccountName.getText()+"\n"
                            +"IFSC:"+IFSC.getText()+"\n"
                            +"Branch:"+Branch.getText();
                    PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                    waIntent.setPackage("com.whatsapp");
                    waIntent.putExtra(Intent.EXTRA_TEXT, text);
                    startActivity(Intent.createChooser(waIntent, "Share with"));
                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(v.getContext(), "WhatsApp not Installed",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
