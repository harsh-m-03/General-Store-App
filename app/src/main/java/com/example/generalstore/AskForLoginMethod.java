package com.example.generalstore;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.generalstore.Modules.OnPressUI;
import com.example.generalstore.Modules.SingleTapClick;
import com.example.generalstore.Modules.Users;
import com.example.generalstore.databinding.ActivityAskForLoginMethodBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class AskForLoginMethod extends AppCompatActivity {
    GestureDetector gestureDetector;
    ActivityAskForLoginMethodBinding binding;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    GoogleSignInClient mGoogleSignInClient;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAskForLoginMethodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        gestureDetector = new GestureDetector(this, new SingleTapClick());
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        progressDialog = new ProgressDialog(AskForLoginMethod.this);
        progressDialog.setTitle("Setting Account");
        progressDialog.setMessage("Creating Account");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1089047323428-gpgp3stt18use1433uolejqe5dfb3a9h.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        if (mAuth.getCurrentUser() != null && mAuth.getCurrentUser().getEmail().equals("harshmange03@gmail.com")) {
            Intent intent = new Intent(AskForLoginMethod.this, SellerPage.class);
            startActivity(intent);
        } else if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(AskForLoginMethod.this, MainActivity.class);
            startActivity(intent);
        }


        binding.google.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new OnPressUI().onPressUi(view, motionEvent);
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                    startActivityForResult.launch(signInIntent);
                }
                return true;
            }
        });
        binding.email.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new OnPressUI().onPressUi(view, motionEvent);
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    Intent intent = new Intent(AskForLoginMethod.this, SignUpUsingEmail.class);
                    startActivity(intent);
                }
                return true;
            }
        });
        binding.alreadyHaveAnAcc.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new OnPressUI().onPressUi(view, motionEvent);
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    Intent intent = new Intent(AskForLoginMethod.this, AlreadyHaveAnAccountCustomer.class);
                    startActivity(intent);
                }
                return true;
            }
        });
        binding.BecomeASeller.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new OnPressUI().onPressUi(view, motionEvent);
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    Intent intent = new Intent(AskForLoginMethod.this, AlreadyHaveAnAccountForSeller.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    ActivityResultLauncher<Intent> startActivityForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    progressDialog.show();
                    firebaseAuthWithGoogle(account.getIdToken());
                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    Toast.makeText(AskForLoginMethod.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    });

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(AskForLoginMethod.this, "Sign-In Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AskForLoginMethod.this, MainActivity.class);
                            startActivity(intent);
                            FirebaseUser user = mAuth.getCurrentUser();
                            Users users = new Users();
                            users.setUserid(user.getUid());
                            users.setUserName(user.getDisplayName());
                            users.setProfilePic(user.getPhotoUrl().toString());
                            users.setTotal_spent(0);
                            database.getReference().child("Customers").child(user.getUid()).setValue(users);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(AskForLoginMethod.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

}