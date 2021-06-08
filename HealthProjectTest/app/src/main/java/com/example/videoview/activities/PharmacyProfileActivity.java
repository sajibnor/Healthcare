package com.example.videoview.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.videoview.R;
import com.example.videoview.databinding.ActivityPharmacyBinding;
import com.example.videoview.databinding.ActivityPharmacyProfileBinding;
import com.example.videoview.models.Pharmacy;

public class PharmacyProfileActivity extends AppCompatActivity {

    ActivityPharmacyProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPharmacyProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Pharmacy pharmacy = getIntent().getParcelableExtra("pharmacy");
        binding.pharmaNameText.setText(pharmacy.getName());

    }
}
