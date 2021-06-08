package com.example.videoview.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.videoview.databinding.ActivityOtpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OtpActivity extends AppCompatActivity {

    ActivityOtpBinding binding;
    private String checker = "", phoneNo = "";
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;
    private String mVerificationId = " ";
    private ProgressDialog dialog;
    SharedPreferences otpPreference;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dialog = new ProgressDialog(this);

        otpPreference = getSharedPreferences("otp",MODE_PRIVATE);
        editor = otpPreference.edit();

        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {


                signInWithPhoneAuthCredential(credential);
                dialog.dismiss();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                dialog.dismiss();

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Toast.makeText(OtpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                    Toast.makeText(OtpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }

                Toast.makeText(OtpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {


                mVerificationId = verificationId;
                mResendToken = token;
                checker = "code_send";
                binding.otpID.setVisibility(View.GONE);
                binding.confirmBTNID.setVisibility(View.GONE);
                binding.editPinTextID.setVisibility(View.VISIBLE);
                binding.verifyBtnId.setVisibility(View.VISIBLE);
                dialog.dismiss();
                Toast.makeText(OtpActivity.this, "Code has been sent to your phone...", Toast.LENGTH_SHORT).show();

                // ...
            }
        };
/*
        String url = getIntent().getStringExtra("imageUrl");

        Glide.with(this).load(url).into(binding.profileImage);*/

        binding.ccp.registerCarrierNumberEditText(binding.editPhoneTextID);


        binding.verifyBtnId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checker.equals("code_send")) {
                    String code;
                    if (binding.editPinTextID.getText().toString().isEmpty()) {
                        Toast.makeText(OtpActivity.this, "", Toast.LENGTH_SHORT).show();
                    } else {

                        dialog.setTitle("Code nVerifiaction...");
                        dialog.setMessage("Please wait, We are verifying your code...");
                        dialog.show();
                        code = binding.editPinTextID.getText().toString();
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
                        signInWithPhoneAuthCredential(credential);

                    }
                }
            }
        });

        binding.confirmBTNID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                phoneNo = binding.ccp.getFullNumberWithPlus();
                if (!phoneNo.equals("")) {

                    dialog.setTitle("Verifying Phone...");
                    dialog.setMessage("Plese Wait,We are Verifying your phone no...");
                    dialog.show();

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phoneNo,        // Phone number to verify
                            60,                 // Timeout duration
                            TimeUnit.SECONDS,   // Unit of timeout
                            OtpActivity.this,               // Activity (for callback binding)
                            mCallbacks);        // OnVerificationStateChangedCallbacks

                } else {
                    Toast.makeText(OtpActivity.this, "Please write your phone no. correctly...", Toast.LENGTH_SHORT).show();

                }

            }
        });


    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            dialog.dismiss();
                            Toast.makeText(OtpActivity.this, "Phone Verification Successful....", Toast.LENGTH_SHORT).show();

                            editor.putString("otp_service","done");
                            editor.apply();
                            startActivity(new Intent(getApplicationContext(),UserSelectionActivity.class));
                            Animatoo.animateDiagonal(OtpActivity.this);

                        } else {
                            dialog.dismiss();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(OtpActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
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
