package com.example.videoview.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.videoview.R;
import com.example.videoview.databinding.ActivityBloodProfileBinding;
import com.example.videoview.models.Blood;

public class BloodProfileActivity extends AppCompatActivity {
    ActivityBloodProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBloodProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Blood blood = getIntent().getParcelableExtra("blood");


        binding.bloodNameText.setText(blood.getName());


    }
}