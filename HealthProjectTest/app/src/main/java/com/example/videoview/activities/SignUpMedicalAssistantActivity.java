package com.example.videoview.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.videoview.R;
import com.example.videoview.databinding.ActivitySignUpMedicalAssistantBinding;
import com.example.videoview.databinding.ActivitySignupDoctorBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.Objects;

import me.shaohui.advancedluban.Luban;


public class SignUpMedicalAssistantActivity extends AppCompatActivity {

    private ActivitySignUpMedicalAssistantBinding binding;
    private String Name,Blood,Mobile, License,Email,Password, Image = " ";
    private String URL;
    private RequestQueue requestQueue;
    private StorageReference storageReference;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivitySignUpMedicalAssistantBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        dialog=  new ProgressDialog(this);
        dialog.setMessage("please wait...");

        storageReference = FirebaseStorage.getInstance().getReference().child("Images_Factory");
        requestQueue = Volley.newRequestQueue(this);


        binding.BtnBrowseImageMedAssistantReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(SignUpMedicalAssistantActivity.this);

            }
        });
        binding.btnBackMedAssistantReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpMedicalAssistantActivity.this,UserSelectionActivity.class));
            }
        });

        binding.btnNextMedAssistantReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.editTextNameMedAssistantReg.getText().toString().isEmpty()){
                    binding.editTextNameMedAssistantReg.setError("Name is empty");
                    binding.editTextNameMedAssistantReg.requestFocus();
                }
                else if (binding.editTextBloodGropMedAssistantReg.getText().toString().isEmpty()){
                    binding.editTextBloodGropMedAssistantReg.setError("NID No is empty");
                    binding.editTextBloodGropMedAssistantReg.requestFocus();
                }
                else if (binding.editTextLicenseNoMedAssistantReg.getText().toString().isEmpty()){
                    binding.editTextLicenseNoMedAssistantReg.setError("BloodGroup is empty ");
                    binding.editTextLicenseNoMedAssistantReg.requestFocus();
                }

                else if (binding.editTextMobileMedAssistantReg.getText().toString().isEmpty()){
                    binding.editTextMobileMedAssistantReg.setError("Mobile is empty");
                    binding.editTextMobileMedAssistantReg.requestFocus();
                }
                else if (binding.editTextEmailMedAssistantReg.getText().toString().isEmpty()){
                    binding.editTextEmailMedAssistantReg.setError("E-mail is empty");
                    binding.editTextEmailMedAssistantReg.requestFocus();
                }
                else if (binding.editTextPasswordMedAssistantReg.getText().toString().isEmpty()){
                    binding.editTextPasswordMedAssistantReg.setError("Password is empty");
                    binding.editTextPasswordMedAssistantReg.requestFocus();
                }
                else {
                    Name = binding.editTextNameMedAssistantReg.getText().toString().trim();
                    License = binding.editTextLicenseNoMedAssistantReg.getText().toString().trim();
                    Blood = binding.editTextBloodGropMedAssistantReg.getText().toString().trim();
                    Mobile = binding.editTextMobileMedAssistantReg.getText().toString().trim();
                    Email = binding.editTextEmailMedAssistantReg.getText().toString().trim();
                    Password = binding.editTextPasswordMedAssistantReg.getText().toString().trim();

                    URL = "https://healthproject101.000webhostapp.com/register_med_assistant.php?name="+Name+"&mobile_no="+Mobile+"&blood_group="+Blood+"&mail="+Email+"&password="+Password+"&license="+License+"&images="+Image;


                    MedicalAssistantSignUp();


                }


            }
        });

    }

    private void MedicalAssistantSignUp() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("1")) {

                            Toast.makeText(SignUpMedicalAssistantActivity.this, "User added to the database", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                        } else {
                            Toast.makeText(SignUpMedicalAssistantActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignUpMedicalAssistantActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri imageUri = result.getUri();

                if (imageUri != null) {
                    setImageUrl(imageUri);
                }


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void setImageUrl(Uri imageUri) {

        dialog.show();
        File imageFile = new File(Objects.requireNonNull(imageUri.getPath()));

        Luban.compress(SignUpMedicalAssistantActivity.this, imageFile)
                .setMaxSize(500)                // limit the final image size（unit：Kb）
                .setMaxHeight(1920)             // limit image height
                .setMaxWidth(1080)              // limit image width
                .putGear(Luban.CUSTOM_GEAR)     // use CUSTOM GEAR compression mode
                .asObservable();

        final StorageReference imageRef = storageReference.child("image_factory_"+System.currentTimeMillis()+".jpg");

        final UploadTask uploadTask = imageRef.putFile(Uri.fromFile(imageFile));
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(SignUpMedicalAssistantActivity.this, "uri to url conversion failed..", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri>imageUrlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        return imageRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {
                            Image  = task.getResult().toString();
                            Log.e("TAG", "onComplete: "+ Image );
                            Glide.with(SignUpMedicalAssistantActivity.this).load(Image)
                                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                    .placeholder(R.drawable.ic_nurse)
                                    .into(binding.CircularImageViewMedAssistantProfile);
                            dialog.dismiss();
                        }
                        else{
                            dialog.dismiss();
                            Toast.makeText(SignUpMedicalAssistantActivity.this, "Image loading failed...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });


    }

}