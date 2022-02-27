package com.example.generalstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.generalstore.Modules.OnPressUI;
import com.example.generalstore.Modules.SingleTapClick;
import com.example.generalstore.Modules.Users;
import com.example.generalstore.databinding.ActivitySignUpUsingEmailBinding;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignUpUsingEmail extends AppCompatActivity {
    GestureDetector gestureDetector;
    ActivitySignUpUsingEmailBinding binding;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    GoogleSignInClient mGoogleSignInClient;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpUsingEmailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        gestureDetector = new GestureDetector(this, new SingleTapClick());
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(SignUpUsingEmail.this);
        progressDialog.setTitle("Setting Account");
        progressDialog.setMessage("Creating Account");
        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding.createAccount.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new OnPressUI().onPressUi(view, motionEvent);
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    if (binding.firstName.getText().toString().isEmpty())
                        binding.firstName.setError("Required!");
                    else if (binding.lastName.getText().toString().isEmpty())
                        binding.lastName.setError("Required!");
                    else if (binding.phoneNumber.getText().toString().isEmpty())
                        binding.phoneNumber.setError("Required!");
                    else if (binding.email.getText().toString().isEmpty())
                        binding.email.setError("Required!");
                    else if (binding.password.getText().toString().isEmpty())
                        binding.password.setError("Required!");
                    else if (binding.confirmPass.getText().toString().isEmpty())
                        binding.confirmPass.setError("Required!");
                    else if (!binding.confirmPass.getText().toString().equals(binding.password.getText().toString())) {
                        binding.confirmPass.setError("Password Mismatched");
                        binding.password.setError("Password Mismatched");
                    } else {
                        progressDialog.show();
                        mAuth.createUserWithEmailAndPassword(binding.email.getText().toString(), binding.password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    Users users = new Users();
                                    users.setTotal_spent(0);
                                    String id = task.getResult().getUser().getUid();
                                    users.setUserid(id);
                                    users.setMail(binding.email.getText().toString());
                                    users.setFirstName(binding.firstName.getText().toString());
                                    users.setLastName(binding.lastName.getText().toString());
                                    users.setProfilePic("xd");
                                    users.setPassword(binding.password.getText().toString());
                                    users.setUserName(users.getFirstName() + " " + users.getLastName());
                                    users.setPhoneNumber(binding.phoneNumber.getText().toString());
                                    database.getReference().child("Customers").child(id).setValue(users);
                                    Toast.makeText(SignUpUsingEmail.this, "User Created", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignUpUsingEmail.this, MainActivity.class);
                                    startActivity(intent);
                                    binding.password.setText("");
                                    binding.confirmPass.setText("");
                                } else {
                                    Toast.makeText(SignUpUsingEmail.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
                return true;
            }
        });
        binding.otherSignUpMethods.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new OnPressUI().onPressUi(view, motionEvent);
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    Intent intent = new Intent(SignUpUsingEmail.this, AskForLoginMethod.class);
                    startActivity(intent);
                }
                return true;
            }
        });

    }
}