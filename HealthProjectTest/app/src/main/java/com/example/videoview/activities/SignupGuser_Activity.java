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
import com.example.videoview.databinding.ActivitySignupGUserBinding;
import com.example.videoview.map_activities.UserMapsActivity;
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


public class SignupGuser_Activity extends AppCompatActivity {

    private ActivitySignupGUserBinding binding;
    private String name, bloodgroup, mobile, email,password, gImage = " ";
    private String URL;
    private RequestQueue requestQueue;
    private StorageReference storageReference;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivitySignupGUserBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        dialog=  new ProgressDialog(this);
        dialog.setMessage("please wait...");

        storageReference = FirebaseStorage.getInstance().getReference().child("Images_Factory");
        requestQueue = Volley.newRequestQueue(this);

        binding.regGUserBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(SignupGuser_Activity.this,UserSelectionActivity.class);

                startActivity(intent);
            }
        });


        binding.btgUserImageBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(SignupGuser_Activity.this);

            }
        });

        binding.regGUserNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.editTextGuserName.getText().toString().isEmpty()){
                    binding.editTextGuserName.setError("Name is empty");
                    binding.editTextGuserName.requestFocus();
                }
                else if (binding.editTextGuserBloodGroup.getText().toString().isEmpty()){
                    binding.editTextGuserBloodGroup.setError("Blood Group is empty ");
                    binding.editTextGuserBloodGroup.requestFocus();
                }
                else if (binding.editTextGuserMobile.getText().toString().isEmpty()){
                    binding.editTextGuserMobile.setError("Mobile is empty");
                    binding.editTextGuserMobile.requestFocus();
                }
                else if (binding.editTextGuserEmail.getText().toString().isEmpty()){
                    binding.editTextGuserEmail.setError("Email is empty");
                    binding.editTextGuserEmail.requestFocus();
                }
                else if (binding.editTextGuserPassword.getText().toString().isEmpty()){
                    binding.editTextGuserPassword.setError("Password is empty");
                    binding.editTextGuserPassword.requestFocus();
                }
                else {
                    name = binding.editTextGuserName.getText().toString().trim();
                    bloodgroup = binding.editTextGuserBloodGroup.getText().toString().trim();
                    mobile = binding.editTextGuserMobile.getText().toString().trim();
                    email = binding.editTextGuserEmail.getText().toString().trim();
                    password = binding.editTextGuserPassword.getText().toString().trim();

                    URL = "https://healthproject101.000webhostapp.com/register_g_user.php?name="+name+"&mobile_no="+mobile+"&blood_group="+bloodgroup+"&mail="+email+"&image="+gImage+"&password="+password;


                    GeneralUserSignUp();


                }


            }
        });

    }

    private void GeneralUserSignUp() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("1")) {

                            Toast.makeText(SignupGuser_Activity.this, "User added to the database", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), UserMapsActivity.class);
                            intent.putExtra("name",name);
                            intent.putExtra("phone",mobile);
                            startActivity(intent);

                        } else {
                            Toast.makeText(SignupGuser_Activity.this, "User already exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignupGuser_Activity.this, "Connection Error", Toast.LENGTH_SHORT).show();
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

        Luban.compress(SignupGuser_Activity.this, imageFile)
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
                Toast.makeText(SignupGuser_Activity.this, "uri to url conversion failed..", Toast.LENGTH_SHORT).show();
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
                            gImage  = task.getResult().toString();
                            Log.e("TAG", "onComplete: "+ gImage );
                            Glide.with(SignupGuser_Activity.this).load(gImage)
                                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                    .placeholder(R.drawable.ic_people)
                                    .into(binding.gUserImageView);
                            dialog.dismiss();
                        }
                        else{
                            dialog.dismiss();
                            Toast.makeText(SignupGuser_Activity.this, "Image loading failed...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });


    }

}