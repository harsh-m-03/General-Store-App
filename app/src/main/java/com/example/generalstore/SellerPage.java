package com.example.generalstore;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.generalstore.Fragments.FragmentsForBottomNavigationForSellerPage.AddItemsToYourStore;
import com.example.generalstore.Fragments.FragmentsForBottomNavigationForSellerPage.CurrentOrders;
import com.example.generalstore.Fragments.FragmentsForBottomNavigationForSellerPage.HomeFragment;
import com.example.generalstore.Modules.SingleTapClick;
import com.example.generalstore.databinding.ActivitySellerPageBinding;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SellerPage extends AppCompatActivity {
    ActivitySellerPageBinding binding;
    GestureDetector gestureDetector;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySellerPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        gestureDetector = new GestureDetector(this, new SingleTapClick());
        database = FirebaseDatabase.getInstance();

        getSupportFragmentManager().beginTransaction().replace(R.id.wholePage, new HomeFragment(), "Back").addToBackStack(null).commit();

        binding.bottomNavigationBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.addItems:
                        selectedFragment = new AddItemsToYourStore();
                        break;
                    case R.id.orders:
                        selectedFragment = new CurrentOrders();
                        break;
                    case R.id.settings:
                        AlertDialog.Builder builder = new AlertDialog.Builder(SellerPage.this);
                        builder.setCancelable(true);
                        builder.setTitle("Confirmation");
                        builder.setMessage("Are you sure you want to Log out?");
                        builder.setPositiveButton("Confirm",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        auth.signOut();
                                        Intent intent = new Intent(SellerPage.this, AskForLoginMethod.class);
                                        startActivity(intent);
                                    }
                                });
                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                        selectedFragment = new HomeFragment();
                        break;
                    default:
                        selectedFragment = new HomeFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.wholePage, selectedFragment, "Back").addToBackStack(null).commit();

                return true;
            }
        });
        if (auth.getCurrentUser() != null && !Objects.equals(auth.getCurrentUser().getEmail(), "harshmange03@gmail.com")) {
            Toast.makeText(SellerPage.this, "Private Page Access Rejected", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SellerPage.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        finishAffinity();
    }


}