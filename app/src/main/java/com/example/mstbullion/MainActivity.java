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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends Fragment {
    ImageView GDI,SDI;
    Animation increase,decrease;
    RecyclerView MetalList;
    MetalListAdapter metalListAdapter;
    List<String> bullionlist;
    private Context context;
    TextView GoldUsdTV,SilverUsdTV,InrUsdTV,GoldMcxTV,SilverMcxTV;

    MainActivity(){

    }

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://34.131.126.241");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        context = getContext();
        mSocket.connect();
        mSocket.on("XAUUSD", GoldUsd);
        mSocket.on("XAUUSDI", GoldUsdIncrease);
        mSocket.on("XAUUSDD", GoldUsdDecrease);
        mSocket.on("XAGUSD", SilverUsd);
        mSocket.on("XAGUSDI", SilverUsd);
        mSocket.on("XAGUSDD", SilverUsd);
        mSocket.on("INRSPOT", InrUsd);
        mSocket.on("INRSPOTI", InrUsd);
        mSocket.on("INRSPOTD", InrUsd);
        mSocket.on("spotup", spotupdate);
        GDI = view.findViewById(R.id.GDI);
        SDI = view.findViewById(R.id.SDI);
        GoldUsdTV = view.findViewById(R.id.GoldSpotPrices);
        SilverUsdTV = view.findViewById(R.id.SilverSpotPrices);
        InrUsdTV = view.findViewById(R.id.INRSpotPrices);
        GoldMcxTV = view.findViewById(R.id.GoldMCXPrices);
        SilverMcxTV = view.findViewById(R.id.SilverMCXPrices);
        increase = AnimationUtils.loadAnimation(context,R.anim.translateup);
        decrease = AnimationUtils.loadAnimation(context,R.anim.translatedown);
        MetalList = view.findViewById(R.id.MetalList);
        bullionlist = new ArrayList<>();
        bullionlist.add("Gold 999 IMP with GST");
        bullionlist.add("Gold 999 IND with GST");
        bullionlist.add("Gold 999 Ref");
        bullionlist.add("Gold 999 IMP with GST [ AMD ]");
        bullionlist.add("Gold 999 IMP with GST [ RAJ ]");
        bullionlist.add("Silver Peti 30 kg");
        metalListAdapter = new MetalListAdapter(bullionlist);
        MetalList.setAdapter(metalListAdapter);
        MetalList.setLayoutManager(new LinearLayoutManager(context));
        MetalList.setItemAnimator(new DefaultItemAnimator());
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mSocket.disconnect();
        mSocket.off("XAUUSD",GoldUsd);
        mSocket.off("XAUUSDI",GoldUsdIncrease);
        mSocket.off("XAUUSDD",GoldUsdDecrease);
        mSocket.off("XAGUSD",SilverUsd);
        mSocket.off("XAGUSDI",SilverUsdIncrease);
        mSocket.off("XAGUSDD",SilverUsdDecrease);
        mSocket.off("INRSPOT",InrUsd);
        mSocket.off("INRSPOTI",InrUsd);
        mSocket.off("INRSPOTD" ,InrUsd);
    }

    private final Emitter.Listener GoldUsd = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    GoldUsdTV.setText(args[0].toString());
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
                    SilverUsdTV.setText(args[0].toString());
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
                    InrUsdTV.setText(args[0].toString());
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
                    data = data.substring(2);
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject= new JSONObject(data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        Log.d("Spot:", jsonObject.get("InsSymbol").toString());
                        if(jsonObject.get("InsSymbol").toString().contains("GOLD")){
                            GoldMcxTV.setText(jsonObject.get("Sell").toString());
                        }
                        if(jsonObject.get("InsSymbol").toString().contains("SILVER")){
                            SilverMcxTV.setText(jsonObject.get("Sell").toString());
                        }
                        Log.d("Spot:", jsonObject.get("Buy").toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

}