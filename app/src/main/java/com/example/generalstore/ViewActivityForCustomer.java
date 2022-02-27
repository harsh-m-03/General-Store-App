package com.example.generalstore;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.generalstore.Modules.AddToCartForCustomer;
import com.example.generalstore.Modules.OnPressUI;
import com.example.generalstore.Modules.SingleTapClick;
import com.example.generalstore.databinding.ActivityViewForCustomerBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class ViewActivityForCustomer extends AppCompatActivity {
    ActivityViewForCustomerBinding binding;
    GestureDetector gestureDetector;
    FirebaseDatabase database;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewForCustomerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        gestureDetector = new GestureDetector(this, new SingleTapClick());
        progressDialog = new ProgressDialog(ViewActivityForCustomer.this);
        progressDialog.setTitle("Deleting Item");
        progressDialog.setMessage("Processing");
        databaseReference = FirebaseDatabase.getInstance().getReference();

        String Stock = getIntent().getStringExtra("Stock");
        String Name = getIntent().getStringExtra("Name");
        String Price = getIntent().getStringExtra("Price");
        String Image = getIntent().getStringExtra("Image");
        String Description = getIntent().getStringExtra("Description");
        String Category = getIntent().getStringExtra("Category");

        binding.name.setText(Name);
        binding.price.setText(Price);
        if (!Description.equals(" "))
            binding.description.setText(Description);
        Picasso.get().load(Image).placeholder(R.drawable.no_image2).into(binding.image);

        ArrayList<String> qty = new ArrayList<>();
        for (int i = 1; i <= Integer.parseInt(Stock); i++) qty.add(String.valueOf(i));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, qty);
        binding.quantity.setThreshold(1);
        binding.quantity.setAdapter(adapter);

        binding.addToCart.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new OnPressUI().onPressUi(view, motionEvent);
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    AddToCartForCustomer addToCartForCustomer = new AddToCartForCustomer();
                    addToCartForCustomer.setCategory(Category);
                    addToCartForCustomer.setItemName(Name);
                    addToCartForCustomer.setPrice(Price);
                    addToCartForCustomer.setId(auth.getUid());
                    addToCartForCustomer.setQuantity(binding.quantity.getText().toString());
                    addToCartForCustomer.setItemImage(Image);
                    database.getReference().child("Customer Cart").child(auth.getUid()).child(Name).setValue(addToCartForCustomer).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Snackbar snackbar = Snackbar.make(binding.mainLayout, "Added to Cart", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    progressDialog.show();
                                    databaseReference.child("Customer Cart").child(addToCartForCustomer.getId()).child(addToCartForCustomer.getItemName()).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            progressDialog.dismiss();
                                            Toast.makeText(ViewActivityForCustomer.this, "Item Deleted Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });

                            snackbar.show();
                        }
                    });
                }
                return true;
            }
        });

        binding.backBtn.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new OnPressUI().onPressUi(view, motionEvent);
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    Intent intent = new Intent(ViewActivityForCustomer.this, MainActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });


    }
}