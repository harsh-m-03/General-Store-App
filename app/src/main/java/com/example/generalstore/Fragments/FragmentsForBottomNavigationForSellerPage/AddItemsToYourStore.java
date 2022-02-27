package com.example.generalstore.Fragments.FragmentsForBottomNavigationForSellerPage;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.generalstore.Modules.ItemsAvailable;
import com.example.generalstore.Modules.OnPressUI;
import com.example.generalstore.Modules.SingleTapClick;
import com.example.generalstore.databinding.FragmentAddItemsToYourStoreBinding;
import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import org.jetbrains.annotations.NotNull;

import java.util.Date;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;


@RequiresApi(api = Build.VERSION_CODES.O)
public class AddItemsToYourStore extends Fragment {


    public AddItemsToYourStore() {
        // Required empty public constructor
    }

    FragmentAddItemsToYourStoreBinding binding;
    GestureDetector gestureDetector;
    private FirebaseAuth mAuth;
    String[] languages = {"Grains", "Stationary", "Drinks", "Milk Products", "Snacks & Biscuit", "Oil", "Others"};
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    FirebaseStorage firebaseStorage;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddItemsToYourStoreBinding.inflate(inflater, container, false);
        gestureDetector = new GestureDetector(getContext(), new SingleTapClick());
        mAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Process");
        progressDialog.setMessage("Uploading Data");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_item, languages);
        binding.category.setThreshold(1);
        binding.category.setAdapter(adapter);

        binding.uploadImage.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new OnPressUI().onPressUi(view, motionEvent);
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    if (binding.itemName.getText().toString().isEmpty())
                        binding.itemName.setError("Required");
                    else {
                        ImagePicker.Companion.with(getActivity())
                                .crop().cropSquare().cropOval()
                                .maxResultSize(512, 512, true)
                                .createIntentFromDialog((Function1) (new Function1() {
                                    public Object invoke(Object var1) {
                                        this.invoke((Intent) var1);
                                        return Unit.INSTANCE;
                                    }

                                    public final void invoke(@NotNull Intent it) {
                                        Intrinsics.checkNotNullParameter(it, "it");
                                        launcher.launch(it);
                                    }
                                }));
                    }
                }
                return true;
            }
        });
        binding.Done.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new OnPressUI().onPressUi(view, motionEvent);
                if (gestureDetector.onTouchEvent(motionEvent)) {
//                    if (!internetIsConnected())
//                        Toast.makeText(getContext(), "Connect to Internet", Toast.LENGTH_SHORT).show();
                    if (binding.itemName.getText().toString().isEmpty())
                        binding.itemName.setError("Required");
                    else if (binding.quantity.getText().toString().isEmpty())
                        binding.quantity.setError("Required");
                    else if (binding.description.getText().toString().isEmpty())
                        binding.description.setError("Required");
                    else if (binding.price.getText().toString().isEmpty())
                        binding.price.setError("Required");
                    else if (binding.category.getText().toString().isEmpty())
                        binding.category.setError("Required");
                    else {
                        progressDialog.show();
                        ItemsAvailable items = new ItemsAvailable();

                        items.setName(binding.itemName.getText().toString());
                        items.setQuantity(binding.quantity.getText().toString());
                        items.setDescription(binding.description.getText().toString());
                        items.setPrice(binding.price.getText().toString());
                        items.setCategory(binding.category.getText().toString());
                        items.setTime(new Date().toString());

                        if (uri != null) {
                            StorageReference storageReference = firebaseStorage.getReference().child("Item Images").child(items.getName());
//                            items.setImage(uri.toString());
                            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            database.getReference().child("Items Available").child(items.getName()).child("image").setValue(uri.toString());
                                        }
                                    });
                                }
                            });
                        }
                        database.getReference().child("Items Available").child(binding.itemName.getText().toString()).setValue(items).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog.dismiss();
                                Snackbar.make(binding.mainFragment, "Data Uploaded Successfully", Snackbar.LENGTH_LONG).show();
                            }
                        });

                        binding.itemName.setText("");
                        binding.quantity.setText("");
                        binding.description.setText("");
                        binding.price.setText("");
                        binding.category.setText("");

                    }
                }
                return true;
            }
        });


        return binding.getRoot();
    }

    public boolean internetIsConnected() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }

    Uri uri = null;
    ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (ActivityResult result) -> {
                if (result.getResultCode() == RESULT_OK) {
                    uri = result.getData().getData();
                    Toast.makeText(getContext(), "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    // Use the uri to load the image
                } else if (result.getResultCode() == ImagePicker.RESULT_ERROR) {
                    Toast.makeText(getContext(), ImagePicker.Companion.getError(result.getData()), Toast.LENGTH_SHORT).show();
                }
            });


}