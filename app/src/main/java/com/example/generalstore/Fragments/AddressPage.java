package com.example.generalstore.Fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.generalstore.Fragments.FragmentsForBottomNavigationCustomerPage.homeFragmentCp;
import com.example.generalstore.MainActivity;
import com.example.generalstore.Modules.AddToCartForCustomer;
import com.example.generalstore.Modules.ItemsAvailable;
import com.example.generalstore.Modules.OnPressUI;
import com.example.generalstore.Modules.SingleTapClick;
import com.example.generalstore.Modules.Users;
import com.example.generalstore.R;
import com.example.generalstore.ViewActivityForCustomer;
import com.example.generalstore.databinding.FragmentAddressPageBinding;
import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import org.jetbrains.annotations.NotNull;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public class AddressPage extends Fragment {
    FragmentAddressPageBinding binding;
    String[] pinCode = {"400060"};
    GestureDetector gestureDetector;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddressPageBinding.inflate(inflater, container, false);
        gestureDetector = new GestureDetector(getContext(), new SingleTapClick());
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Process");
        progressDialog.setMessage("Uploading Data");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_item, pinCode);
        binding.pinCode.setThreshold(1);
        binding.pinCode.setAdapter(adapter);

        binding.Done.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new OnPressUI().onPressUi(view, motionEvent);
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    if (binding.address.getText().toString().isEmpty())
                        binding.address.setError("Required");
                    else if (binding.pinCode.getText().toString().isEmpty())
                        binding.pinCode.setError("Required");
                    else {

//                        progressDialog.show();
//                        database.getReference().child("Customers").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                Users user = snapshot.getValue(Users.class);
//                                user.setAddress(binding.address.getText().toString() + ", " + binding.pinCode.getText().toString());
////                                user.setTotal_spent(user.getTotal_spent()+ (getArguments().getInt("Total", 0)));
//                                database.getReference().child("Customers").child(mAuth.getUid()).setValue(user);
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//                            }
//                        });
//                        database.getReference().child("Customers Cart").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                                    AddToCartForCustomer item = dataSnapshot.getValue(AddToCartForCustomer.class);
//
//                                    database.getReference().child("Current Orders").child(mAuth.getUid() + " " + new java.util.Date(System.currentTimeMillis())).child(item.getItemName()).setValue(item);
////                                    database.getReference().child("Items Available").child(item.getItemName()).addValueEventListener(new ValueEventListener() {
////                                        @Override
////                                        public void onDataChange(@NonNull DataSnapshot snapshot1) {
////                                            ItemsAvailable i=snapshot1.getValue(ItemsAvailable.class);
////                                            i.setQuantity(String.valueOf(Integer.parseInt(i.getQuantity())-Integer.parseInt(item.getQuantity())));
////                                            database.getReference().child("Items Available").child(item.getItemName()).setValue(i);
////                                        }
////
////                                        @Override
////                                        public void onCancelled(@NonNull DatabaseError error) {
////
////                                        }
////                                    });
//                                    database.getReference().child("Customer Cart").child(mAuth.getUid()).child(item.getItemName()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            if (!task.isSuccessful())
//                                                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//
//                                        }
//                                    });
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
//
//
//
//                        progressDialog.dismiss();
//                        Toast.makeText(getContext(), "Order Placed", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(getActivity(), MainActivity.class);
//                        startActivity(intent);


                        progressDialog.show();
                        database.getReference().child("Customers").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
                            Users user;

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                user = snapshot.getValue(Users.class);
                                user.setAddress(binding.address.getText().toString() + ", " + binding.pinCode.getText().toString());
                                user.setTotal_spent(getArguments().getInt("Total", 0));

                                database.getReference().child("Customers").child(user.getUserid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            database.getReference().child("Customer Cart").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                        AddToCartForCustomer itemsAvailable = dataSnapshot.getValue(AddToCartForCustomer.class);
                                                        itemsAvailable.getItemName(dataSnapshot.getKey());
                                                        database.getReference().child("Current Orders").child(mAuth.getUid()).child(itemsAvailable.getItemName()).setValue(itemsAvailable).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                databaseReference.child("Customer Cart").child(mAuth.getUid()).child(itemsAvailable.getItemName()).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        progressDialog.dismiss();
                                                                    }
                                                                });
                                                            }
                                                        });
                                                    }

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });

                                        } else {
                                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                Toast.makeText(getContext(), "Order Placed", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }


                        });

                    }
                }
                return true;
            }
        });


        return binding.getRoot();
    }
}