package com.example.mstbullion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PricelistAdapter extends RecyclerView.Adapter<PricelistAdapter.ViewHolder>{


    private final List<BullionEntry> PriceList;

    public PricelistAdapter(List<BullionEntry> PriceList) {
        this.PriceList = PriceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pamperpackrow,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.MetalLabel.setText(PriceList.get(position).getLabel());
        holder.Price.setText(String.valueOf(PriceList.get(position).getPrice()));
    }

    @Override
    public int getItemCount() {
        return PriceList.size();
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
