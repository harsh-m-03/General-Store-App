package com.example.generalstore.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.generalstore.Modules.ItemsAvailable;
import com.example.generalstore.R;
import com.example.generalstore.ViewItemActivity;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SellerAdapter extends RecyclerView.Adapter<SellerAdapter.ViewHolder> implements Filterable {

    private ArrayList<ItemsAvailable> fullList = new ArrayList<>();

    public SellerAdapter(ArrayList<ItemsAvailable> list, Context context) {
        this.list = list;
        this.context = context;
    }

    ArrayList<ItemsAvailable> list;
    Context context;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_for_seller, parent, false);
        return new ViewHolder(view);
    }

    GestureDetector gestureDetector;

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);
        ItemsAvailable itemsAvailable = list.get(position);
        for (ItemsAvailable i : list)
            if (!fullList.contains(itemsAvailable))
                fullList.add(itemsAvailable);
        if (!fullList.contains(itemsAvailable))
            fullList.add(itemsAvailable);
        holder.itemName.setText(itemsAvailable.getName());
        holder.price.setText("₹ " + itemsAvailable.getPrice());
        Picasso.get().load(itemsAvailable.getImage()).placeholder(R.drawable.no_image).into(holder.image);
        holder.itemView.startAnimation(animation);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return listFilter;
    }

    private Filter listFilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<ItemsAvailable> filterList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filterList.addAll(fullList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (ItemsAvailable item : fullList) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filterList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filterList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list.clear();
            list.addAll((ArrayList) results.values);
            notifyDataSetChanged();


        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView price, itemName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.itemImage);
            itemName = itemView.findViewById(R.id.itemName);
            price = itemView.findViewById(R.id.price);

        }
    }

}
