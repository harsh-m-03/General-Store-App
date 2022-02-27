package com.example.generalstore.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.generalstore.R;
import com.example.generalstore.databinding.FragmentAddToCartBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddToCart extends BottomSheetDialogFragment {
    FragmentAddToCartBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddToCartBinding.inflate(inflater, container, false);


        return binding.getRoot();
    }
}