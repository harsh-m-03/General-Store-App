package com.example.generalstore.Adapters;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.generalstore.Modules.AddToCartForCustomer;
import com.example.generalstore.Modules.ItemsAvailable;
import com.example.generalstore.R;
import com.example.generalstore.SellerPage;
import com.example.generalstore.ViewItemActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class AddToCartAdapter extends RecyclerView.Adapter<AddToCartAdapter.ViewHolder> {

    private ArrayList<AddToCartForCustomer> fullList = new ArrayList<>();

    public AddToCartAdapter(ArrayList<AddToCartForCustomer> list, Context context) {
        this.list = list;
        this.context = context;
    }

    ArrayList<AddToCartForCustomer> list;
    Context context;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_cart, parent, false);
        return new ViewHolder(view);
    }

    DatabaseReference databaseReference;
    ProgressDialog progressDialog;

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Deleting Item");
        progressDialog.setMessage("Processing");
        databaseReference = FirebaseDatabase.getInstance().getReference();

        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);
        AddToCartForCustomer itemsAvailable = list.get(position);
        for (AddToCartForCustomer i : list)
            if (!fullList.contains(itemsAvailable))
                fullList.add(itemsAvailable);
        if (!fullList.contains(itemsAvailable))
            fullList.add(itemsAvailable);
        holder.itemName.setText(itemsAvailable.getItemName());
        holder.quantity.setText("Quantity: " + itemsAvailable.getQuantity());
        holder.price.setText("₹ " + (Integer.parseInt(itemsAvailable.getPrice()) * Integer.parseInt(itemsAvailable.getQuantity())));
        Picasso.get().load(itemsAvailable.getItemImage()).placeholder(R.drawable.no_image).into(holder.image);
        holder.itemView.startAnimation(animation);
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to delete current Item?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog.show();
                                databaseReference.child("Customer Cart").child(itemsAvailable.getId()).child(itemsAvailable.getItemName()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                            Toast.makeText(context, "Deleted from Cart", Toast.LENGTH_SHORT).show();
                                        else
                                            Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                progressDialog.dismiss();

                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView price, itemName, quantity;
        Button btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.itemImage);
            itemName = itemView.findViewById(R.id.itemName);
            btn=itemView.findViewById(R.id.delete);
            quantity = itemView.findViewById(R.id.quantity);
            price = itemView.findViewById(R.id.price);

        }
    }

}
