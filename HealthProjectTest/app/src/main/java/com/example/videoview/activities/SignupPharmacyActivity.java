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
import com.example.videoview.databinding.ActivitySignupPharmacyBinding;
import com.example.videoview.map_activities.PharmacyMapsActivity;
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


public class SignupPharmacyActivity extends AppCompatActivity {

    private ActivitySignupPharmacyBinding binding;
    private String Name, Licence_no,Mobile,Email,Password, PhaImage = " ";
    private String URL;
    private RequestQueue requestQueue;
    private StorageReference storageReference;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivitySignupPharmacyBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        dialog=  new ProgressDialog(this);
        dialog.setMessage("please wait...");

        storageReference = FirebaseStorage.getInstance().getReference().child("Images_Factory");
        requestQueue = Volley.newRequestQueue(this);


        binding.BtnBrowseImagePharmacyReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(SignupPharmacyActivity.this);

            }
        });
        binding.btnBackPharmacyReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupPharmacyActivity.this,UserSelectionActivity.class));
            }
        });

        binding.btnNextPharmacyReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.editTextNameRegPharmacy.getText().toString().isEmpty()){
                    binding.editTextNameRegPharmacy.setError("name is empty");
                    binding.editTextNameRegPharmacy.requestFocus();
                }
                else if (binding.editTextLicenseNoRegPharmacy.getText().toString().isEmpty()){
                    binding.editTextLicenseNoRegPharmacy.setError("license Number ");
                    binding.editTextLicenseNoRegPharmacy.requestFocus();
                }
                else if (binding.editTextMobileRegPharmacy.getText().toString().isEmpty()){
                    binding.editTextMobileRegPharmacy.setError("mobile No is empty");
                    binding.editTextMobileRegPharmacy.requestFocus();
                }
                else if (binding.editTextEmailRegPharmacy.getText().toString().isEmpty()){
                    binding.editTextEmailRegPharmacy.setError("email is empty");
                    binding.editTextEmailRegPharmacy.requestFocus();
                }
                else if (binding.editTextPasswordRegPharmacy.getText().toString().isEmpty()){
                    binding.editTextPasswordRegPharmacy.setError("password is empty");
                    binding.editTextPasswordRegPharmacy.requestFocus();
                }
                else {
                    Name = binding.editTextNameRegPharmacy.getText().toString().trim();
                    Licence_no = binding.editTextLicenseNoRegPharmacy.getText().toString().trim();
                    Mobile = binding.editTextMobileRegPharmacy.getText().toString().trim();
                    Email = binding.editTextEmailRegPharmacy.getText().toString().trim();
                    Password = binding.editTextPasswordRegPharmacy.getText().toString().trim();

                    URL = "https://healthproject101.000webhostapp.com/register_pharmacy.php?name=" +Name+ "&mobile_no=" +Mobile+ "&image=" +PhaImage+ "&license_no=" +Licence_no+ "&mail=" +Email+" &password="+Password;


                    PharmacySignup();


                }


            }
        });

    }

    private void PharmacySignup() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("1")) {

                            Toast.makeText(SignupPharmacyActivity.this, "User added to the database", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), PharmacyMapsActivity.class);
                            intent.putExtra("licence",Licence_no);
                            intent.putExtra("phone",Mobile);
                            intent.putExtra("name",Name);
                            startActivity(intent);
                        } else {
                            Toast.makeText(SignupPharmacyActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignupPharmacyActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
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

        Luban.compress(SignupPharmacyActivity.this, imageFile)
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
                Toast.makeText(SignupPharmacyActivity.this, "uri to url conversion failed..", Toast.LENGTH_SHORT).show();
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
                            PhaImage  = task.getResult().toString();
                            Log.e("TAG", "onComplete: "+ PhaImage );
                            Glide.with(SignupPharmacyActivity.this).load(PhaImage)
                                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                    .placeholder(R.drawable.ic_pharmacy)
                                    .into(binding.circularImageView);
                            dialog.dismiss();
                        }
                        else{
                            dialog.dismiss();
                            Toast.makeText(SignupPharmacyActivity.this, "Image loading failed...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });


    }

}