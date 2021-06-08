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


public class SignupDoctorActivity extends AppCompatActivity {

    private ActivitySignupDoctorBinding binding;
    private String Name, Bmdc_no, Nid_no, Mobile,Email,Password, Image = " ";
    private String URL;
    private RequestQueue requestQueue;
    private StorageReference storageReference;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivitySignupDoctorBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        dialog=  new ProgressDialog(this);
        dialog.setMessage("please wait...");

        storageReference = FirebaseStorage.getInstance().getReference().child("Images_Factory");
        requestQueue = Volley.newRequestQueue(this);


        binding.BtnBrowseImageDrReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(SignupDoctorActivity.this);

            }
        });
        binding.btnBackDrReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupDoctorActivity.this,UserSelectionActivity.class));
            }
        });

        binding.btnNextDrReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.editTextNameDrReg.getText().toString().isEmpty()){
                    binding.editTextNameDrReg.setError("Name is empty");
                    binding.editTextNameDrReg.requestFocus();
                }
                else if (binding.editTextBMDCnoDrReg.getText().toString().isEmpty()){
                    binding.editTextBMDCnoDrReg.setError("BMDC No is empty ");
                    binding.editTextBMDCnoDrReg.requestFocus();
                }
                else if (binding.editTextNIDnoDrReg.getText().toString().isEmpty()){
                    binding.editTextNIDnoDrReg.setError("NID No is empty");
                    binding.editTextNIDnoDrReg.requestFocus();
                }
                else if (binding.editTextMobileDrReg.getText().toString().isEmpty()){
                    binding.editTextMobileDrReg.setError("Mobile is empty");
                    binding.editTextMobileDrReg.requestFocus();
                }
                else if (binding.editTextEmailDrReg.getText().toString().isEmpty()){
                    binding.editTextEmailDrReg.setError("E-mail is empty");
                    binding.editTextEmailDrReg.requestFocus();
                }
                else if (binding.editTextPasswordDrReg.getText().toString().isEmpty()){
                    binding.editTextPasswordDrReg.setError("Password is empty");
                    binding.editTextPasswordDrReg.requestFocus();
                }
                else {
                    Name = binding.editTextNameDrReg.getText().toString().trim();
                    Bmdc_no = binding.editTextBMDCnoDrReg.getText().toString().trim();
                    Nid_no = binding.editTextNIDnoDrReg.getText().toString().trim();
                    Mobile = binding.editTextMobileDrReg.getText().toString().trim();
                    Email = binding.editTextEmailDrReg.getText().toString().trim();
                    Password = binding.editTextPasswordDrReg.getText().toString().trim();

                    URL = "https://healthproject101.000webhostapp.com/register_doctor.php?name="+Name+"&bmdc_no="+Bmdc_no+"&nid_no="+Nid_no+"&mobile_no="+Mobile+"&mail="+Email+"&image="+Image+"&password="+Password;


                    DoctorSignUp();


                }


            }
        });

    }

    private void DoctorSignUp() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("1")) {

                            Toast.makeText(SignupDoctorActivity.this, "User added to the database", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), DoctorActivity.class));
                        } else {
                            Toast.makeText(SignupDoctorActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignupDoctorActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
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

        Luban.compress(SignupDoctorActivity.this, imageFile)
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
                Toast.makeText(SignupDoctorActivity.this, "uri to url conversion failed..", Toast.LENGTH_SHORT).show();
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
                            Glide.with(SignupDoctorActivity.this).load(Image)
                                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                    .placeholder(R.drawable.ic_doctor)
                                    .into(binding.CircularImageViewDrProfile);
                            dialog.dismiss();
                        }
                        else{
                            dialog.dismiss();
                            Toast.makeText(SignupDoctorActivity.this, "Image loading failed...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });


    }

}