package com.example.videoview.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.videoview.R;
import com.example.videoview.databinding.ActivityDoctorBinding;
import com.example.videoview.databinding.ActivityDoctorProfileBinding;
import com.example.videoview.models.Doctor;

public class DoctorProfileActivity extends AppCompatActivity {
    ActivityDoctorProfileBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoctorProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Doctor doctor = getIntent().getParcelableExtra("doctor");
        if(doctor.getName().contains(" "))
        {
            String[] nameChar = doctor.getName().split(" ");

            if(doctor.getName().length()>12)
            {
                if(nameChar[0].toLowerCase().equals("md"))
                {
                    binding.docNameIdText.setText("Dr "+nameChar[1]);

                }
                else{
                    binding.docNameIdText.setText("Dr "+nameChar[0]);

                }

            }
            else{

                binding.docNameIdText.setText("Dr "+nameChar[0]+" "+nameChar[1]);

            }

        }
        else{
            binding.docNameIdText.setText("Dr "+doctor.getName());
        }




    }
}