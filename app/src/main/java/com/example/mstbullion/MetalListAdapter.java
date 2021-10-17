package com.example.mstbullion;


import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MetalListAdapter extends RecyclerView.Adapter<MetalListAdapter.ViewHolder> {

    private final List<BullionEntry> bullionList;

    public MetalListAdapter(List<BullionEntry> bullionList) {
        this.bullionList = bullionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.metalrow,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.MetalLabel.setText(bullionList.get(position).getLabel());
        holder.Price.setText(String.valueOf(bullionList.get(position).getPrice()));
        holder.Price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               DBManager dbManager = new DBManager(v.getContext());
               dbManager.open();
               if(dbManager.usersignedIn()){
                   Log.d("User","signed In"+":"+holder.Price.getText());
                    Intent intent = new Intent(v.getContext(),BullionBooking.class);
                    intent.putExtra("ClickPrice",Double.parseDouble(holder.Price.getText().toString()));
                    intent.putExtra("Selection",bullionList.get(holder.getAdapterPosition()).getLabel());
                    v.getContext().startActivity(intent);
               }else{
                   Toast.makeText(v.getContext(),"Not Signed In",Toast.LENGTH_LONG).show();
                   Intent useraccount = new Intent(v.getContext(), UserAccount.class);
                   v.getContext().startActivity(useraccount);
               }
            }
        });
    }

    @Override
    public int getItemCount() {
        return bullionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView MetalLabel,Price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            MetalLabel = mView.findViewById(R.id.Label);
            Price = mView.findViewById(R.id.Price);
        }
    }
}
