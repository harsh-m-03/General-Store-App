package com.example.generalstore;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.example.generalstore.Adapters.ItemsAvailableForCustomerAdapter;
import com.example.generalstore.Adapters.SellerAdapter;
import com.example.generalstore.Fragments.ForgotPassword;
import com.example.generalstore.Fragments.FragmentsForBottomNavigationCustomerPage.customerCart;
import com.example.generalstore.Fragments.FragmentsForBottomNavigationCustomerPage.homeFragmentCp;
import com.example.generalstore.Fragments.FragmentsForBottomNavigationForSellerPage.AddItemsToYourStore;
import com.example.generalstore.Fragments.FragmentsForBottomNavigationForSellerPage.CurrentOrders;
import com.example.generalstore.Fragments.FragmentsForBottomNavigationForSellerPage.HomeFragment;
import com.example.generalstore.Modules.ItemsAvailable;
import com.example.generalstore.Modules.OnPressUI;
import com.example.generalstore.Modules.Seller;
import com.example.generalstore.Modules.SingleTapClick;
import com.example.generalstore.Modules.Users;
import com.example.generalstore.databinding.ActivityAlreadyHaveAnAccountCustomerBinding;
import com.example.generalstore.databinding.ActivityAlreadyHaveAnAccountForSellerBinding;
import com.example.generalstore.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
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

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    GestureDetector gestureDetector;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    private FirebaseUser user;
    GoogleSignInClient mGoogleSignInClient;
    DatabaseReference myRef;
    ArrayList<ItemsAvailable> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        gestureDetector = new GestureDetector(this, new SingleTapClick());
        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new homeFragmentCp(), "Back").addToBackStack(null).commit();

        binding.bottomNavigationBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.orders:
                        selectedFragment = new customerCart();
                        break;
                    case R.id.settings:
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setCancelable(true);
                        builder.setTitle("Confirmation");
                        builder.setMessage("Are you sure you want to Log out?");
                        builder.setPositiveButton("Confirm",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mAuth.signOut();
                                        Intent intent = new Intent(MainActivity.this, AskForLoginMethod.class);
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
                        selectedFragment = new homeFragmentCp();
                        break;
                    default:
                        selectedFragment = new homeFragmentCp();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, selectedFragment, "Back").addToBackStack(null).commit();

                return true;
            }
        });
    }

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation);

        recyclerView.setLayoutAnimation(controller);
        Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}