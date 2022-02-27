package com.example.generalstore.Fragments.FragmentsForBottomNavigationCustomerPage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.generalstore.Adapters.AddToCartAdapter;
import com.example.generalstore.Adapters.SellerAdapter;
import com.example.generalstore.Fragments.AddressPage;
import com.example.generalstore.MainActivity;
import com.example.generalstore.Modules.AddToCartForCustomer;
import com.example.generalstore.Modules.ItemsAvailable;
import com.example.generalstore.Modules.OnPressUI;
import com.example.generalstore.Modules.Seller;
import com.example.generalstore.Modules.SingleTapClick;
import com.example.generalstore.R;
import com.example.generalstore.ViewActivityForCustomer;
import com.example.generalstore.databinding.FragmentCustomerCartBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class customerCart extends Fragment {
    FirebaseAuth auth;
    FragmentCustomerCartBinding binding;
    GestureDetector gestureDetector;
    FirebaseDatabase database;
    ArrayList<AddToCartForCustomer> list = new ArrayList<>();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCustomerCartBinding.inflate(inflater, container, false);
        gestureDetector = new GestureDetector(getContext(), new SingleTapClick());
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        binding.shimmerLayout.startShimmer();

        AddToCartAdapter adapter = new AddToCartAdapter(list, getContext());
        binding.recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerView.setLayoutManager(layoutManager);


        database.getReference().child("Customer Cart").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                int total = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    AddToCartForCustomer itemsAvailable = dataSnapshot.getValue(AddToCartForCustomer.class);
                    itemsAvailable.getItemName(dataSnapshot.getKey());
                    list.add(itemsAvailable);
                    total += Integer.parseInt(itemsAvailable.getQuantity()) * Integer.parseInt(itemsAvailable.getPrice());
                }
                adapter.notifyDataSetChanged();
                if (list.isEmpty()) {
                    binding.bottomTotalPrice.setVisibility(View.GONE);
                    binding.animationView.setVisibility(View.VISIBLE);
                } else {
                    binding.bottomTotalPrice.setVisibility(View.VISIBLE);
                    binding.animationView.setVisibility(View.GONE);
                }
                binding.shimmerLayout.stopShimmer();
                binding.totalPrice.setText(String.valueOf(total));
                binding.shimmerLayout.setVisibility(View.GONE);
                binding.recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.next.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new OnPressUI().onPressUi(view, motionEvent);
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    AddressPage fragment = new AddressPage();
                    Bundle bundle = new Bundle();
                    bundle.putInt("Total", Integer.parseInt(binding.totalPrice.getText().toString()));
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment, "Back").addToBackStack(null).commit();
                }
                return true;
            }
        });
        return binding.getRoot();
    }
}