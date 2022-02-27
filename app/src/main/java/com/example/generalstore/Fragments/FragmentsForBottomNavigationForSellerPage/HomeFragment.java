package com.example.generalstore.Fragments.FragmentsForBottomNavigationForSellerPage;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.example.generalstore.Adapters.SellerAdapter;
import com.example.generalstore.Classes.RecyclerItemClickListener;
import com.example.generalstore.MainActivity;
import com.example.generalstore.Modules.ItemsAvailable;
import com.example.generalstore.Modules.SingleTapClick;
import com.example.generalstore.SellerPage;
import com.example.generalstore.ViewItemActivity;
import com.example.generalstore.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends Fragment {

    public static final String TAG = "";

    public HomeFragment() {
        // Required empty public constructor
    }

    FragmentHomeBinding binding;
    GestureDetector gestureDetector;
    FirebaseDatabase database;
    FirebaseAuth auth;
    ArrayList<ItemsAvailable> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        gestureDetector = new GestureDetector(getContext(), new SingleTapClick());
        database = FirebaseDatabase.getInstance();
        binding.shimmerLayout.startShimmer();
        auth = FirebaseAuth.getInstance();

        SellerAdapter adapter = new SellerAdapter(list, getContext());
        binding.recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerView.setLayoutManager(layoutManager);


        binding.recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), binding.recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // do whatever
                        Intent intent = new Intent(getActivity(), ViewItemActivity.class);
                        intent.putExtra("Name", list.get(position).getName());
                        intent.putExtra("Price", list.get(position).getPrice());
                        intent.putExtra("Image", list.get(position).getImage());
                        intent.putExtra("Description", list.get(position).getDescription());

                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View vierw, int position) {
                        // do whatever
                    }
                })
        );

        database.getReference().child("Items Available").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ItemsAvailable itemsAvailable = dataSnapshot.getValue(ItemsAvailable.class);
                    itemsAvailable.getName(dataSnapshot.getKey());
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

        binding.searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        if (auth.getCurrentUser() != null && Objects.equals(auth.getCurrentUser().getEmail(), "harshmange03@gmail.com")) {
           binding.itemImage.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent=new Intent(getActivity(),MainActivity.class);
                   startActivity(intent);
               }
           });
        }
            return binding.getRoot();
    }
}