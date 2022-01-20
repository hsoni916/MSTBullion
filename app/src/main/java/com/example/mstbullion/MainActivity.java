package com.example.mstbullion;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.Transport;

public class MainActivity extends Fragment {
    ImageView GDI,SDI,GDIMCX,SDIMCX,IDI;
    Animation increase,decrease;
    RecyclerView MetalList;
    MetalListAdapter metalListAdapter;
    BullionEntry bullionEntry = new BullionEntry();
    List<BullionEntry> adapterbullionlist;
    List<Double> margins;
    private Context context;
    TextView GoldUsdTV,SilverUsdTV,InrUsdTV,GoldMcxTV,SilverMcxTV;
    int goldspot,silverspot;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    PamperPackArray pamperPackArray;

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://34.131.126.241:5000");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void updatePriceList(){
        for(int i=0;i<adapterbullionlist.size();i++){
            if(adapterbullionlist.get(i).getLabel().contains("Gold")){
                adapterbullionlist.get(i).setPrice(margins.get(i)+goldspot);
            }else{
                adapterbullionlist.get(i).setPrice(margins.get(i)+silverspot);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        context = getContext();
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
        mSocket.on("XAUUSD", GoldUsd);
        mSocket.on("XAUUSDI", GoldUsdIncrease);
        mSocket.on("XAUUSDD", GoldUsdDecrease);
        mSocket.on("XAGUSD", SilverUsd);
        mSocket.on("XAGUSDI", SilverUsdIncrease);
        mSocket.on("XAGUSDD", SilverUsdDecrease);
        mSocket.on("INRUSD", InrUsd);
        mSocket.on("INRUSDI", InrUsdIncrease);
        mSocket.on("INRUSDD", InrUsdDecrease);
        mSocket.on("spotup", spotupdate);
        mSocket.on("spotupi", spotupdatei);
        GDI = view.findViewById(R.id.GDI);
        SDI = view.findViewById(R.id.SDI);
        GDIMCX = view.findViewById(R.id.GDIMCX);
        SDIMCX = view.findViewById(R.id.SDIMCX);
        IDI = view.findViewById(R.id.IDI);
        GoldUsdTV = view.findViewById(R.id.GoldSpotPrices);
        SilverUsdTV = view.findViewById(R.id.SilverSpotPrices);
        InrUsdTV = view.findViewById(R.id.INRSpotPrices);
        GoldMcxTV = view.findViewById(R.id.GoldMCXPrices);
        SilverMcxTV = view.findViewById(R.id.SilverMCXPrices);
        increase = AnimationUtils.loadAnimation(context,R.anim.translateup);
        decrease = AnimationUtils.loadAnimation(context,R.anim.translatedown);
        MetalList = view.findViewById(R.id.MetalList);

        adapterbullionlist = new ArrayList<>();
        firebaseFirestore.collection("AdminOptions").document("Margin").addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    Log.d("Error in snapshot","listener");
                }else{
                    if(value!=null && value.exists()){
                        pamperPackArray = value.toObject(PamperPackArray.class);
                        assert pamperPackArray != null;
                        adapterbullionlist.addAll(pamperPackArray.bullionEntries);
                        margins = new ArrayList<>();
                        for(int i=0;i<adapterbullionlist.size();i++){
                            margins.add(adapterbullionlist.get(i).getPrice());
                        }
                    }
                }
            }
        });
        metalListAdapter = new MetalListAdapter(adapterbullionlist);
        MetalList.setAdapter(metalListAdapter);
        MetalList.setLayoutManager(new LinearLayoutManager(context));
        MetalList.setItemAnimator(new DefaultItemAnimator());
        return view;
    }

    public void setfirebaselistener(){


        /* PamperPackArray pamperPackArray = new PamperPackArray();
        pamperPackArray.bullionEntries = new ArrayList<>();
        bullionlist = new ArrayList<>();
        bullionEntry.setLabel("Gold 999 IND GST");
        bullionEntry.setPrice(1650);
        bullionlist.add(bullionEntry);
        bullionEntry = new BullionEntry();
        bullionEntry.setLabel("Gold 999 IMP GST");
        bullionEntry.setPrice(1750);
        bullionlist.add(bullionEntry);
        bullionEntry = new BullionEntry();
        bullionEntry.setLabel("Silver 30 kg GST");
        bullionEntry.setPrice(850);
        bullionlist.add(bullionEntry);
        bullionEntry = new BullionEntry();
        bullionEntry.setLabel("Gold Retail");
        bullionEntry.setPrice(1650);
        bullionlist.add(bullionEntry);
        Log.d("Array", String.valueOf(bullionlist));
        pamperPackArray.bullionEntries.addAll(bullionlist);
        firebaseFirestore.collection("AdminOptions").document("Margin").set(pamperPackArray).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d("Task","Success");
                }
            }
        });*/

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mSocket.disconnect();
        mSocket.off("XAUUSD", GoldUsd);
        mSocket.off("XAUUSDI", GoldUsdIncrease);
        mSocket.off("XAUUSDD", GoldUsdDecrease);
        mSocket.off("XAGUSD", SilverUsd);
        mSocket.off("XAGUSDI", SilverUsdIncrease);
        mSocket.off("XAGUSDD", SilverUsdDecrease);
        mSocket.off("INRUSD", InrUsd);
        mSocket.off("INRUSDI", InrUsd);
        mSocket.off("INRUSDD", InrUsd);
        mSocket.off("spotup", spotupdate);
        mSocket.off("spotupi", spotupdatei);
    }

    private final Emitter.Listener GoldUsd = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("XAU,USD","True");
                    if(args[0]!=null){
                        GoldUsdTV.setText(args[0].toString());
                    }
                }
            });
        }
    };

    private final Emitter.Listener GoldUsdIncrease = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    GoldUsdTV.setText(args[0].toString());
                    GDI.setImageResource(R.drawable.down);
                    GDI.startAnimation(decrease);
                }
            });
        }
    };

    private final Emitter.Listener GoldUsdDecrease = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    GoldUsdTV.setText(args[0].toString());
                    GDI.setImageResource(R.drawable.up);
                    GDI.startAnimation(increase);
                }
            });
        }
    };

    private final Emitter.Listener SilverUsd = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(args[0]!=null){
                        SilverUsdTV.setText(args[0].toString());
                    }
                }
            });
        }
    };

    private final Emitter.Listener SilverUsdIncrease = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SilverUsdTV.setText(args[0].toString());
                    SDI.setImageResource(R.drawable.down);
                    SDI.startAnimation(decrease);
                }
            });
        }
    };

    private final Emitter.Listener SilverUsdDecrease = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SilverUsdTV.setText(args[0].toString());
                    SDI.setImageResource(R.drawable.up);
                    SDI.startAnimation(increase);
                }
            });
        }
    };

    private final Emitter.Listener InrUsd = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                if (args[0] != null){
                    InrUsdTV.setText(args[0].toString());
                }
                }
            });
        }
    };

    private final Emitter.Listener InrUsdIncrease = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    InrUsdTV.setText(args[0].toString());
                    IDI.setImageResource(R.drawable.up);
                    IDI.startAnimation(increase);
                }
            });
        }
    };

    private final Emitter.Listener InrUsdDecrease = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    InrUsdTV.setText(args[0].toString());
                    IDI.setImageResource(R.drawable.down);
                    IDI.startAnimation(decrease);

                }
            });
        }
    };

    private final Emitter.Listener spotupdate = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
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
                        Log.d("Spot:", jsonObject.get("InsSymbol").toString());
                        if(jsonObject.get("InsSymbol").toString().contains("GOLD")){
                            if(goldspot==0) {
                                String sell = jsonObject.get("Sell").toString().replace(",","");
                                goldspot = Integer.parseInt(sell);
                                GoldMcxTV.setText(jsonObject.get("Sell").toString());
                            }else{
                                int currentprice = Integer.parseInt(jsonObject.get("Sell").toString().replace(",",""));
                                if(goldspot==currentprice){
                                    GoldMcxTV.setText(String.valueOf(currentprice));
                                }
                                if(goldspot>currentprice){
                                    goldspot = currentprice;
                                    GoldMcxTV.setText(String.valueOf(currentprice));
                                    GDIMCX.setImageResource(R.drawable.down);
                                    GDIMCX.startAnimation(decrease);
                                }
                                if(goldspot<currentprice){
                                    goldspot = currentprice;
                                    GoldMcxTV.setText(String.valueOf(currentprice));
                                    GDIMCX.setImageResource(R.drawable.up);
                                    GDIMCX.startAnimation(increase);
                                }
                            }
                        }
                        if(jsonObject.get("InsSymbol").toString().contains("SILVER")){
                            if(silverspot==0){
                                String sell = jsonObject.get("Sell").toString().replace(",","");
                                silverspot = Integer.parseInt(sell);
                                SilverMcxTV.setText(sell);
                            }else{
                                int currentprice = Integer.parseInt(jsonObject.get("Sell").toString().replace(",",""));
                                if(silverspot==currentprice){
                                    SilverMcxTV.setText(String.valueOf(currentprice));
                                }
                                if(silverspot>currentprice){
                                    silverspot = currentprice;
                                    SilverMcxTV.setText(String.valueOf(currentprice));
                                    SDIMCX.setImageResource(R.drawable.down);
                                    SDIMCX.startAnimation(decrease);
                                }
                                if(silverspot<currentprice){
                                    silverspot = currentprice;
                                    SilverMcxTV.setText(String.valueOf(currentprice));
                                    SDIMCX.setImageResource(R.drawable.up);
                                    SDIMCX.startAnimation(increase);
                                }
                            }
                        }
                        Log.d("Spot:", jsonObject.get("Buy").toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    updatePriceList();
                    metalListAdapter.notifyDataSetChanged();
                }

            });

        }
    };

    private final Emitter.Listener spotupdatei = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(args[0]!=null){
                        Log.d("spotupi","True");
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject = new JSONObject(args[0].toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            if (!jsonObject.get("rows").toString().isEmpty()) {
                                jsonObject = jsonObject.getJSONObject("rows");
                                goldspot = Integer.parseInt(jsonObject.getJSONObject("GOLD").get("last_price").toString());
                                silverspot = Integer.parseInt(jsonObject.getJSONObject("SILVER").get("last_price").toString());
                                GoldMcxTV.setText(jsonObject.getJSONObject("GOLD").get("last_price").toString());
                                SilverMcxTV.setText(jsonObject.getJSONObject("SILVER").get("last_price").toString());
                                updatePriceList();
                                metalListAdapter.notifyItemRangeChanged(0, adapterbullionlist.size());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    };

}