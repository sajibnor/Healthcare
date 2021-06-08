package com.example.videoview.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.videoview.databinding.ActivityUserSelectionBinding;


public class UserSelectionActivity extends AppCompatActivity {
    private ActivityUserSelectionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityUserSelectionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
            binding.doctorRegbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(UserSelectionActivity.this, SignupDoctorActivity.class);
                    intent.putExtra("key","doctor");
                    startActivity(intent);
                    Animatoo.animateDiagonal(UserSelectionActivity.this);

                    //finishAffinity();
                }
            });
        binding.gUserRegbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSelectionActivity.this, SignupGuser_Activity.class);
                startActivity(intent);
                Animatoo.animateDiagonal(UserSelectionActivity.this);

                ///finishAffinity();
            }
        });
        binding.PharmacyRegbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSelectionActivity.this, SignupPharmacyActivity.class);
                startActivity(intent);
                Animatoo.animateDiagonal(UserSelectionActivity.this);

                /// finishAffinity();
            }
        });
        binding.AmbulanceRegbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSelectionActivity.this, SignUpAmbulanceActivity.class);
                startActivity(intent);
                Animatoo.animateDiagonal(UserSelectionActivity.this);

                ///finishAffinity();
            }
        });
        binding.BloodBankRegbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSelectionActivity.this, SignUpBloodBankActivity.class);
                startActivity(intent);
                Animatoo.animateDiagonal(UserSelectionActivity.this);

                /// finishAffinity();
            }
        });
        binding.MedAssistanRegbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSelectionActivity.this, SignUpMedicalAssistantActivity.class);
                startActivity(intent);
                Animatoo.animateDiagonal(UserSelectionActivity.this);

                ///  finishAffinity();
            }
        });

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder.create();

        builder.setTitle("Would you like to close the application...")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        moveTaskToBack(true);

                    }
                }).setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();


            }
        });
        builder.show();


    }
}
