package com.example.mstbullion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MetalListAdapter extends RecyclerView.Adapter<MetalListAdapter.ViewHolder> {

    private final List<String> bullionList;

    public MetalListAdapter(List<String> bullionList) {
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
        holder.MetalLabel.setText(bullionList.get(position));
        holder.Price.setText("49100");
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
