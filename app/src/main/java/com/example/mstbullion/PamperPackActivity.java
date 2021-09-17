package com.example.mstbullion;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

public class PamperPackActivity extends Fragment {

    private Context context;
    RecyclerView pricelist;
    List<BullionEntry> BullionEntryList,BullionEntryList2;
    List<String> BullionLabelList, QuantityOption;
    BullionEntry bullionEntry = new BullionEntry();
    PricelistAdapter pricelistAdapter;
    Spinner BullionWeightSpinner, QuantitySpinner;
    Button ConfirmBook;
    FirebaseFirestore firebaseFirestore;
    PamperPackActivity(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pamperpack, container, false);
        context = getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        //ConfirmBook = view.findViewById(R.id.Book);
        BullionEntryList = new ArrayList<>();
        BullionEntryList2 = new ArrayList<>();
        pricelist = view.findViewById(R.id.PriceList);
        GetCurrentPrices();
        GetPamperPackDetails();
        BullionEntryList.addAll(BullionEntryList2);
        BullionWeightSpinner = view.findViewById(R.id.Options);
        QuantitySpinner = view.findViewById(R.id.Quantity);
        QuantityOption = new ArrayList<>();
        QuantityOption.add("0");
        QuantityOption.add("1");
        QuantityOption.add("2");
        QuantityOption.add("3");
        QuantityOption.add("4");
        QuantityOption.add("5");
        QuantityOption.add("6");
        QuantityOption.add("7");
        QuantityOption.add("8");
        QuantityOption.add("9");
        QuantityOption.add("10");
        ArrayAdapter<String> PriceAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, QuantityOption);
        PriceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        QuantitySpinner.setAdapter(PriceAdapter);
       /* ConfirmBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(QuantitySpinner.getSelectedItem().toString().contentEquals("0")
                        ||BullionWeightSpinner.getSelectedItem().equals("Select Weight")){
                    Toast.makeText(context,"Invalid Selection!",Toast.LENGTH_LONG).show();
                    BullionWeightSpinner.setSelection(0);
                    QuantitySpinner.setSelection(0);
                }else{
                    Toast.makeText(context,"Attempting to book!",Toast.LENGTH_LONG).show();
                    //Firebase Method
                }
            }
        });*/
        return view;
    }

    private void GetCurrentPrices() {
        //WebSocket integration here.
    }

    private void GetPamperPackDetails() {
        firebaseFirestore.collection("AdminOptions").document("PamperPackDetails").addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    Log.d("Error in snapshot","listener");
                }else{
                    if(value!=null && value.exists()){
                        BullionLabelList = new ArrayList<>();
                        BullionLabelList.add("Select Weight");
                        PamperPackArray pamperPackArray = value.toObject(PamperPackArray.class);
                        assert pamperPackArray != null;
                        for(BullionEntry bullionEntry:pamperPackArray.bullionEntries){
                            BullionLabelList.add(bullionEntry.Label);
                        }
                        pricelistAdapter = new PricelistAdapter(pamperPackArray.bullionEntries);
                        pricelist.setAdapter(pricelistAdapter);
                        pricelist.setLayoutManager(new LinearLayoutManager(context));
                        pricelist.setItemAnimator(new DefaultItemAnimator());
                        ArrayAdapter<String> LabelAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item,BullionLabelList);
                        LabelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        BullionWeightSpinner.setAdapter(LabelAdapter);
                    }
                }
            }
        });
    }
}
