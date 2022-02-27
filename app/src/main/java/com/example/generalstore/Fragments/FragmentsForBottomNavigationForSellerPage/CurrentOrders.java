package com.example.generalstore.Fragments.FragmentsForBottomNavigationForSellerPage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.generalstore.databinding.FragmentCurrentOrdersBinding;

public class CurrentOrders extends Fragment {

    public CurrentOrders() {
        // Required empty public constructor
    }
FragmentCurrentOrdersBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    binding=FragmentCurrentOrdersBinding.inflate(inflater,container,false);

    return  binding.getRoot();
    }
}