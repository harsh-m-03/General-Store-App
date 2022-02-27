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
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.generalstore.Fragments.ForgotPassword;
import com.example.generalstore.Modules.OnPressUI;
import com.example.generalstore.Modules.Seller;
import com.example.generalstore.Modules.SingleTapClick;
import com.example.generalstore.databinding.ActivityAlreadyHaveAnAccountForSellerBinding;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class AlreadyHaveAnAccountForSeller extends AppCompatActivity {
    ActivityAlreadyHaveAnAccountForSellerBinding binding;
    GestureDetector gestureDetector;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    GoogleSignInClient mGoogleSignInClient;
    DatabaseReference myRef;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlreadyHaveAnAccountForSellerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        gestureDetector = new GestureDetector(this, new SingleTapClick());
        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("adminKey");
        final String[] Value = {"Test"};

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Value[0] = value;
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(AlreadyHaveAnAccountForSeller.this, "Failed to read value", Toast.LENGTH_SHORT).show();
            }
        });

        progressDialog = new ProgressDialog(AlreadyHaveAnAccountForSeller.this);
        progressDialog.setTitle("Finding Account");
        progressDialog.setMessage("Logging Account");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1089047323428-gpgp3stt18use1433uolejqe5dfb3a9h.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        binding.google.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new OnPressUI().onPressUi(view, motionEvent);
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    if (binding.adminKey.getText().toString().equals(Value[0])) {
                        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                        startActivityForResult.launch(signInIntent);
                    } else binding.adminKey.setError("Invalid");
                }
                return true;
            }
        });
        binding.login.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new OnPressUI().onPressUi(view, motionEvent);
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    if (binding.email.getText().toString().isEmpty())
                        binding.email.setError("Required!");
                    else if (binding.password.getText().toString().isEmpty())
                        binding.password.setError("Required!");
                    else if (binding.adminKey.getText().toString().isEmpty())
                        binding.adminKey.setError("Required!");
                    else if (!binding.adminKey.getText().toString().equals(Value[0]))
                        binding.adminKey.setError("Invalid");
                    else {
                        progressDialog.show();
                        mAuth.signInWithEmailAndPassword(binding.email.getText().toString(), binding.password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(AlreadyHaveAnAccountForSeller.this, SellerPage.class);
                                    startActivity(intent);
                                    binding.password.setText("");
                                    Toast.makeText(AlreadyHaveAnAccountForSeller.this, "Logging Successful - Seller", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(AlreadyHaveAnAccountForSeller.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
                return true;
            }
        });
        binding.forgotPassword.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new OnPressUI().onPressUi(view, motionEvent);
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    ForgotPassword forgotPassword=new ForgotPassword();
                    forgotPassword.show(getSupportFragmentManager(), forgotPassword.getTag());

                }
                return true;
            }
        });

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
                    Toast.makeText(AlreadyHaveAnAccountForSeller.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(AlreadyHaveAnAccountForSeller.this, "Sign-In Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AlreadyHaveAnAccountForSeller.this, SellerPage.class);
                            startActivity(intent);
                            FirebaseUser user = mAuth.getCurrentUser();
                            Seller users = new Seller();
                            users.setUserid(user.getUid());
                            users.setUserName(user.getDisplayName());
                            users.setProfilePic(user.getPhotoUrl().toString());
                            users.setTotal_earn(0);
                            database.getReference().child("Seller").child("Google SignUp").child(user.getUid()).setValue(users);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(AlreadyHaveAnAccountForSeller.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AlreadyHaveAnAccountForSeller.this, AskForLoginMethod.class);
        startActivity(intent);
        super.onBackPressed();
    }
}