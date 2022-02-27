package com.example.generalstore.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.generalstore.Fragments.AddToCart;
import com.example.generalstore.Fragments.ForgotPassword;
import com.example.generalstore.Modules.ItemsAvailable;
import com.example.generalstore.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ItemsAvailableForCustomerAdapter extends RecyclerView.Adapter<ItemsAvailableForCustomerAdapter.ViewHolder> {
    ArrayList<ItemsAvailable> list;
    Context context;

    public ItemsAvailableForCustomerAdapter(ArrayList<ItemsAvailable> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_ui, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemsAvailable itemsAvailable = list.get(position);
        holder.name.setText(itemsAvailable.getName());
        holder.category.setText(itemsAvailable.getCategory());
        holder.price.setText("₹ " + itemsAvailable.getPrice());
        Picasso.get().load(itemsAvailable.getImage()).placeholder(R.drawable.no_image2).into(holder.image);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, category, price;
        FloatingActionButton btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.uiImage);
            name = itemView.findViewById(R.id.uiName);
            category = itemView.findViewById(R.id.uiCategory);
            price = itemView.findViewById(R.id.uiPrice);
            btn = itemView.findViewById(R.id.uiAddToCart);


        }
    }

}
