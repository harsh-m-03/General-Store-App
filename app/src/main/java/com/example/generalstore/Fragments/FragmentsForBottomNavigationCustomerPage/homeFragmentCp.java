package com.example.generalstore.Fragments.FragmentsForBottomNavigationCustomerPage;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.generalstore.Adapters.ItemsAvailableForCustomerAdapter;
import com.example.generalstore.Classes.RecyclerItemClickListener;
import com.example.generalstore.MainActivity;
import com.example.generalstore.Modules.ItemsAvailable;
import com.example.generalstore.Modules.SingleTapClick;
import com.example.generalstore.Modules.Users;
import com.example.generalstore.R;
import com.example.generalstore.SellerPage;
import com.example.generalstore.ViewActivityForCustomer;
import com.example.generalstore.ViewItemActivity;
import com.example.generalstore.databinding.FragmentCustomerCartBinding;
import com.example.generalstore.databinding.FragmentHomeCpBinding;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class homeFragmentCp extends Fragment {
    FragmentHomeCpBinding binding;
    GestureDetector gestureDetector;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    private FirebaseUser user;
    GoogleSignInClient mGoogleSignInClient;
    DatabaseReference myRef;
    ArrayList<ItemsAvailable> list = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeCpBinding.inflate(inflater, container, false);

        gestureDetector = new GestureDetector(getContext(), new SingleTapClick());
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Customers").child(mAuth.getUid());
        binding.shimmerLayout.startShimmer();

        ItemsAvailableForCustomerAdapter adapter = new ItemsAvailableForCustomerAdapter(list, getContext());
        binding.recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.scheduleLayoutAnimation();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);
                if (!user.getProfilePic().equals("xd"))
                    Picasso.get().load(user.getProfilePic()).placeholder(R.drawable.avatar).into(binding.profileImage);
                binding.dashboardName.setText(user.getUserName());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SellerPage.class);
                startActivity(intent);
            }
        });
        database.getReference().child("Items Available").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ItemsAvailable itemsAvailable = dataSnapshot.getValue(ItemsAvailable.class);
                    itemsAvailable.getName(dataSnapshot.getKey());
                    if (Integer.parseInt(itemsAvailable.getQuantity()) > 0)
                        list.add(itemsAvailable);
                }
                adapter.notifyDataSetChanged();
                binding.shimmerLayout.stopShimmer();
                binding.shimmerLayout.setVisibility(View.GONE);
                binding.recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (!Objects.equals(tab.getText(), "All"))
                    database.getReference().child("Items Available").addValueEventListener(new ValueEventListener() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            binding.animationView.setVisibility(View.GONE);
                            binding.shimmerLayout.startShimmer();
                            list.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                ItemsAvailable itemsAvailable = dataSnapshot.getValue(ItemsAvailable.class);
                                itemsAvailable.getName(dataSnapshot.getKey());
                                if (Integer.parseInt(itemsAvailable.getQuantity()) > 0 && itemsAvailable.getCategory().contentEquals(tab.getText()))
                                    list.add(itemsAvailable);
                            }
                            adapter.notifyDataSetChanged();
                            binding.shimmerLayout.stopShimmer();
                            binding.shimmerLayout.setVisibility(View.GONE);
                            binding.recyclerView.setVisibility(View.VISIBLE);
                            if (list.isEmpty())
                                binding.animationView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                else
                    database.getReference().child("Items Available").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            binding.animationView.setVisibility(View.GONE);
                            list.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                ItemsAvailable itemsAvailable = dataSnapshot.getValue(ItemsAvailable.class);
                                itemsAvailable.getName(dataSnapshot.getKey());
                                if (Integer.parseInt(itemsAvailable.getQuantity()) > 0)
                                    list.add(itemsAvailable);
                            }
                            adapter.notifyDataSetChanged();
                            binding.shimmerLayout.stopShimmer();
                            binding.shimmerLayout.setVisibility(View.GONE);
                            binding.recyclerView.setVisibility(View.VISIBLE);
                            if (list.isEmpty())
                                binding.animationView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        binding.recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), binding.recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // do whatever
                        Intent intent = new Intent(getActivity(), ViewActivityForCustomer.class);
                        intent.putExtra("Name", list.get(position).getName());
                        intent.putExtra("Price", list.get(position).getPrice());
                        intent.putExtra("Category",list.get(position).getCategory());
                        intent.putExtra("Image", list.get(position).getImage());
                        intent.putExtra("Stock",list.get(position).getQuantity());
                        intent.putExtra("Description", list.get(position).getDescription());
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        return binding.getRoot();
    }
}