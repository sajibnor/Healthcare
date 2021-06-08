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
import com.example.videoview.databinding.ActivitySignupAmbulanceBinding;
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


public class SignUpAmbulanceActivity extends AppCompatActivity {

    private ActivitySignupAmbulanceBinding binding;
    private String Name, License, Nid_no, Mobile,Email,Password, Image = " ";
    private String URL;
    private RequestQueue requestQueue;
    private StorageReference storageReference;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivitySignupAmbulanceBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        dialog=  new ProgressDialog(this);
        dialog.setMessage("please wait...");

        storageReference = FirebaseStorage.getInstance().getReference().child("Images_Factory");
        requestQueue = Volley.newRequestQueue(this);


        binding.BtnBrowseImageAmbulanceReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(SignUpAmbulanceActivity.this);

            }
        });
        binding.btnBackAmbulanceReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpAmbulanceActivity.this,UserSelectionActivity.class));
            }
        });

        binding.btnNextAmbulanceReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.editTextNameAmbulanceReg.getText().toString().isEmpty()){
                    binding.editTextNameAmbulanceReg.setError("Name is empty");
                    binding.editTextNameAmbulanceReg.requestFocus();
                }
                else if (binding.editTextLicenseNoAmbulanceReg.getText().toString().isEmpty()){
                    binding.editTextLicenseNoAmbulanceReg.setError("License No is empty ");
                    binding.editTextLicenseNoAmbulanceReg.requestFocus();
                }
                else if (binding.editTextNIDnoAmbulanceReg.getText().toString().isEmpty()){
                    binding.editTextNIDnoAmbulanceReg.setError("NID No is empty");
                    binding.editTextNIDnoAmbulanceReg.requestFocus();
                }
                else if (binding.editTextMobileNoAmbulanceReg.getText().toString().isEmpty()){
                    binding.editTextMobileNoAmbulanceReg.setError("Mobile is empty");
                    binding.editTextMobileNoAmbulanceReg.requestFocus();
                }
                else if (binding.editTextEmailAmbulanceReg.getText().toString().isEmpty()){
                    binding.editTextEmailAmbulanceReg.setError("E-mail is empty");
                    binding.editTextEmailAmbulanceReg.requestFocus();
                }
                else if (binding.editTextPasswordAmbulanceReg.getText().toString().isEmpty()){
                    binding.editTextPasswordAmbulanceReg.setError("Password is empty");
                    binding.editTextPasswordAmbulanceReg.requestFocus();
                }
                else {
                    Name = binding.editTextNameAmbulanceReg.getText().toString().trim();
                    License = binding.editTextLicenseNoAmbulanceReg.getText().toString().trim();
                    Nid_no = binding.editTextNIDnoAmbulanceReg.getText().toString().trim();
                    Mobile = binding.editTextMobileNoAmbulanceReg.getText().toString().trim();
                    Email = binding.editTextEmailAmbulanceReg.getText().toString().trim();
                    Password = binding.editTextPasswordAmbulanceReg.getText().toString().trim();

                    URL ="https://healthproject101.000webhostapp.com/register_ambulance.php?name="+Name+"&mobile_no="+Mobile+"&nid="+Nid_no+"&license_no="+License+"&blood_group="+License+"&mail="+Email+"&password="+Password+"&image="+Image;


                    AmbulanceSignUp();


                }


            }
        });

    }

    private void AmbulanceSignUp() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("1")) {

                            Toast.makeText(SignUpAmbulanceActivity.this, "User added to the database", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), AmbulanceActivity.class));
                        } else {
                            Toast.makeText(SignUpAmbulanceActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignUpAmbulanceActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
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

        Luban.compress(SignUpAmbulanceActivity.this, imageFile)
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
                Toast.makeText(SignUpAmbulanceActivity.this, "uri to url conversion failed..", Toast.LENGTH_SHORT).show();
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
                            Glide.with(SignUpAmbulanceActivity.this).load(Image)
                                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                    .placeholder(R.drawable.ic_ambulance)
                                    .into(binding.ImageViewAmbulancneREG);
                            dialog.dismiss();
                        }
                        else{
                            dialog.dismiss();
                            Toast.makeText(SignUpAmbulanceActivity.this, "Image loading failed...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });


    }

}