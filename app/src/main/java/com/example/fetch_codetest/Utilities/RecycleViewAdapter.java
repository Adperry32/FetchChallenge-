package com.example.fetch_codetest.Utilities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fetch_codetest.R;

import java.util.ArrayList;
import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ItemViewHolder> {

    private List<Items> _list = new ArrayList<>();
    public void apiList(List<Items> items){
        _list = items;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapter.ItemViewHolder holder, int position) {

        Items item = _list.get(position);
        holder.listIdTextView.setText(String.valueOf(item.getListId()));
        holder.nameTextView.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return _list.size();
    }
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final TextView listIdTextView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            listIdTextView = itemView.findViewById(R.id.itemListId);
            nameTextView = itemView.findViewById(R.id.itemName);
        }

    }
}


