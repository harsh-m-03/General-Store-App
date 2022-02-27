package com.example.generalstore;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.generalstore.Modules.ItemsAvailable;
import com.example.generalstore.Modules.OnPressUI;
import com.example.generalstore.Modules.SingleTapClick;
import com.example.generalstore.databinding.ActivityViewItemBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class ViewItemActivity extends AppCompatActivity {
    ActivityViewItemBinding binding;
    GestureDetector gestureDetector;
    FirebaseDatabase database;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        gestureDetector = new GestureDetector(this, new SingleTapClick());
        progressDialog = new ProgressDialog(ViewItemActivity.this);
        progressDialog.setTitle("Deleting Item");
        progressDialog.setMessage("Processing");

        String Name = getIntent().getStringExtra("Name");
        String Price = getIntent().getStringExtra("Price");
        String Image = getIntent().getStringExtra("Image");
        String Description = getIntent().getStringExtra("Description");


        binding.name.setText(Name);
        binding.price.setText(Price);
        binding.description.setText(Description);
        Picasso.get().load(Image).placeholder(R.drawable.no_image2).into(binding.image);

        binding.edit.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new OnPressUI().onPressUi(view, motionEvent);
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    Toast.makeText(ViewItemActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
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
                    Intent intent = new Intent(ViewItemActivity.this, SellerPage.class);
                    startActivity(intent);
                }
                return true;
            }
        });
        binding.delete.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new OnPressUI().onPressUi(view, motionEvent);
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ViewItemActivity.this);
                    builder.setCancelable(true);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Are you sure you want to delete current Item?");
                    builder.setPositiveButton("Confirm",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    progressDialog.show();
                                    databaseReference.child("Items Available").child(Name).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Intent intent = new Intent(ViewItemActivity.this, SellerPage.class);
                                            startActivity(intent);
                                            progressDialog.dismiss();
                                            Toast.makeText(ViewItemActivity.this, "Item Deleted Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    

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
                return true;
            }
        });
    }
}