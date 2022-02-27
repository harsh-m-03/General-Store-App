package com.example.generalstore.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.generalstore.Modules.OnPressUI;
import com.example.generalstore.Modules.SingleTapClick;
import com.example.generalstore.databinding.FragmentForgotPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ForgotPassword extends BottomSheetDialogFragment {
    FragmentForgotPasswordBinding binding;
    GestureDetector gestureDetector;
    private FirebaseAuth mAuth;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false);
        gestureDetector = new GestureDetector(getContext(), new SingleTapClick());
        mAuth = FirebaseAuth.getInstance();

        binding.generateLink.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new OnPressUI().onPressUi(view, motionEvent);
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    if (binding.email.getText().toString().isEmpty())
                        binding.email.setError("Required!");
                    else{
                        FirebaseAuth.getInstance().sendPasswordResetEmail(binding.email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getContext(), "Link has been sent via Gmail, Please check your Mail!", Toast.LENGTH_SHORT).show();
                                    FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
                                        @Override
                                        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                            if (firebaseAuth.getCurrentUser() == null) {
                                            }
                                        }
                                    };
                                    dismiss();
                                }else{
                                    Toast.makeText(getContext(), "Error: "+task.getException(), Toast.LENGTH_SHORT).show();
                                }
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