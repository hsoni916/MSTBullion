package com.example.mstbullion;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.Transport;

public class BullionBooking extends AppCompatActivity {
    private Socket mSocket;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    PamperPackArray pamperPackArray;
    double clickprice,currentprice;
    TextView currentpricetextview,AmountTextView,StatusTextView;
    String label;
    Button book;
    BullionEntry bullionEntry;
    EditText Quantity;
    private DBManager dbManager;
    double ATP;
    BullionRecord bullionRecord = new BullionRecord();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbManager = new DBManager(this);
        dbManager.open();
        if(dbManager.check()) {
            if (dbManager.usersignedIn()) {
                Log.d("True reset", "");
                if (dbManager.getUser() != null) {
                    Log.d("User","Not Null");
                    //User Signed In.
                    setContentView(R.layout.booking_page);
                    book = findViewById(R.id.bookbullion);
                    clickprice = getIntent().getDoubleExtra("ClickPrice",0);
                    label = getIntent().getStringExtra("Selection");
                    bullionRecord.setLabel(label);
                    bullionRecord.setBuyer(dbManager.getUser()+":"+dbManager.getPhone());
                    {
                        try {
                            mSocket = IO.socket("http://34.131.126.241:5000");
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                    currentpricetextview = findViewById(R.id.currentprice);
                    Quantity = findViewById(R.id.Quantity_EditText);
                    AmountTextView = findViewById(R.id.AmountToPay);
                    StatusTextView = findViewById(R.id.bookingstatus);
                    Quantity.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if(!Quantity.getText().toString().isEmpty()){
                               if(!Quantity.getText().toString().equalsIgnoreCase("0")){
                                   Log.d("Current Price", String.valueOf(currentprice));
                                   if(!(Integer.parseInt(Quantity.getText().toString())>2)){
                                       if(currentprice!=0){
                                           bullionRecord.setQuantity(Double.parseDouble(Quantity.getText().toString()));
                                           ATP = Double.parseDouble(Quantity.getText().toString())*currentprice;
                                           AmountTextView.setText(String.valueOf(ATP));
                                           bullionRecord.setAmount(ATP);
                                           Log.d("Amount", String.valueOf(ATP));
                                       }
                                   }else{
                                       Quantity.setText(null);
                                   }
                               }else{
                                   AmountTextView.setText("0.0");
                               }
                            }else{
                                AmountTextView.setText("0.0");
                            }
                        }
                    });
                    mSocket.connect();
                    mSocket.io().on(Manager.EVENT_TRANSPORT, new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            Transport transport = (Transport) args[0];
                            transport.on(Transport.EVENT_ERROR, new Emitter.Listener() {
                                @Override
                                public void call(Object... args) {
                                    Exception e = (Exception) args[0];
                                    Log.e("TAG", "Transport error " + e);
                                    e.printStackTrace();
                                    e.getCause().printStackTrace();
                                }
                            });
                        }
                    });

                    mSocket.on("spotup", spotupdate);
                    mSocket.on("spotupi", spotupdatei);
                    SetFireStoreListener();
                    book.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Check if the quantity is not zero or empty.
                            Quantity.setEnabled(false);
                            bullionRecord.setBname(dbManager.getBusiness());
                            book.setEnabled(false);
                            if(!Quantity.getText().toString().isEmpty()){
                                if(!Quantity.getText().toString().equalsIgnoreCase("0")){
                                    //booking enabled.
                                    final Date currentdate = Calendar.getInstance().getTime();
                                    final SimpleDateFormat Date_Day_Time = new SimpleDateFormat("dd-MM-yyyy 'T' HH:mm:ss", Locale.getDefault());
                                    bullionRecord.setTimestamp(Date_Day_Time.format(currentdate));
                                    long time= System.currentTimeMillis();
                                    if(bullionRecord.getAmount() != 0){
                                        firebaseFirestore.collection("AdminOptions").document("Transaction")
                                                .collection("Sales")
                                                .document(String.valueOf(time))
                                                .set(bullionRecord)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            Log.d("Booking successfull","0");
                                                            StatusTextView.setText("Booking successful!");
                                                            Quantity.setText(null);
                                                            Quantity.setEnabled(true);
                                                            book.setEnabled(true);
                                                        }else{
                                                            Quantity.setText(null);
                                                            StatusTextView.setText("Booking unsuccessful!");
                                                            Quantity.setEnabled(true);
                                                            book.setEnabled(true);
                                                        }
                                                    }
                                                });
                                    }
                                }
                            }
                        }
                    });
                }
            }
        }else{
            Intent intent = new Intent(getBaseContext(),UserAccount.class);
            startActivity(intent);
        }

    }

    private void SetFireStoreListener(){
        firebaseFirestore.collection("AdminOptions").document("Margin").addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    Log.d("Error in snapshot","listener");
                    SetFireStoreListener();
                }else{
                    if(value!=null && value.exists()){
                        pamperPackArray = value.toObject(PamperPackArray.class);
                        assert pamperPackArray != null;
                        for(BullionEntry bullionEntry1:pamperPackArray.bullionEntries){
                            if(bullionEntry1.getLabel().equals(label)){
                                bullionEntry = bullionEntry1;
                                break;
                            }
                        }
                    }
                }

            }
        });
    }

    private final Emitter.Listener spotupdate = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String data = args[0].toString();
                    Log.d("Data:",data);
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject= new JSONObject(data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if(jsonObject.get("InsSymbol").toString().contains("GOLD")){
                            currentprice = Double.parseDouble(jsonObject.get("Sell").toString().replace(",",""));
                            if(bullionEntry!=null){
                                currentprice = (currentprice + bullionEntry.getPrice())*10;
                                bullionRecord.setPrice(currentprice);
                            }
                            currentpricetextview.setText(String.valueOf(currentprice));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    };

    private final Emitter.Listener spotupdatei = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(args[0]!=null){
                        String data = args[0].toString();
                        Log.d("Data:","data");
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject= new JSONObject(data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            if (!jsonObject.get("rows").toString().isEmpty()) {
                                jsonObject = jsonObject.getJSONObject("rows");
                                if(label.contains("Gold")||label.contains("GOLD")){
                                    currentprice = (Integer.parseInt(jsonObject.getJSONObject("GOLD").get("last_price").toString()) + bullionEntry.getPrice())*10;
                                    bullionRecord.setPrice(currentprice);
                                }
                                if(label.contains("Silver")||label.contains("SILVER")){
                                    currentprice = Integer.parseInt(jsonObject.getJSONObject("SILVER").get("last_price").toString()) + bullionEntry.getPrice();
                                    bullionRecord.setPrice(currentprice);
                                }
                            }
                            if((clickprice-currentprice)>0){
                                //Click price higher. Show text in green. Meaning, the current price has decreased.
                            }
                            if((clickprice-currentprice)<0){
                                 //Click price is lower. Show text in red. Meaning, the current price has increased.
                            }
                            currentpricetextview.setText(String.valueOf(currentprice));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    };
}
