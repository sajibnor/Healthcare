package com.example.videoview.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.videoview.R;
import com.example.videoview.databinding.ActivitySliderBinding;

import java.util.ArrayList;
import java.util.List;

public class SliderActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    List<Integer> images = new ArrayList<>();
    List<String> slideText = new ArrayList<>();
    List<String> titleText = new ArrayList<>();
    ActivitySliderBinding binding;

    SliderAdapter adapter;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySliderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        sharedPreferences = getSharedPreferences("log", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        editor.putString("key", "success");
        editor.apply();


        binding.arrowImageID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), OtpActivity.class));
                Animatoo.animateDiagonal(SliderActivity.this);

            }
        });

        addAllImageAndText();
        adapter = new SliderAdapter(this, images, slideText, titleText);
        binding.viewPagerID.setAdapter(adapter);
        binding.indicator.setViewPager(binding.viewPagerID);

        binding.viewPagerID.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position > 1) {
                    binding.arrowImageID.setVisibility(View.VISIBLE);
                }
                if (position == 4) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(getApplicationContext(), OtpActivity.class));
                            Animatoo.animateDiagonal(SliderActivity.this);
                        }
                    },5000);


                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void addAllImageAndText() {


///images

        images.add(R.drawable.doctor);
        images.add(R.drawable.pharma);
        images.add(R.drawable.ambulance);
        images.add(R.drawable.blood);
        images.add(R.drawable.assistant);

        //Text
        slideText.add("বি.এম.ডি.সি অনুমোদিত ডাক্তারের সেবা নিশ্চিত করা এবং ভুয়া ডাক্তার থেকে নিরাপদ রাখাই আমাদের মূল লক্ষ্য ।");
        slideText.add("নকল ও ভেজাল ওষুধ থেকে আপনাদের নিরাপদ রাখা এবং সরকারের অভিযান আরো জোরালো করতে আমরা প্রতিজ্ঞাবদ্ধ। ।");
        slideText.add("কম খরচে এবং স্বল্প সময়ে মানসম্পন্ন সেবা দানে সদা প্রস্তুত আমরা ।");
        slideText.add("প্রয়োজনীয় রক্তের গ্রুপ সন্ধানে ভোগান্তি বন্ধ করতে এবং স্বল্প সময়ে আপনার প্রয়োজনীয় রক্তের চাহিদা মেটানো আমাদের উদ্দেশ্য। ।");
        slideText.add("আমাদের মেডি এসিস্ট্যান্ট বা এসিস্ট্যান্টরা পুরো বাংলাদেশ জুড়ে আপনাদের নিরাপদ টেলিমেডিসিন সেবা এবং সুচিকিৎসা নিশ্চিত করবেন। ।");

        ///title
        titleText.add("ডাক্তার");
        titleText.add("ফার্মেসি");
        titleText.add("অ্যাম্বুলেন্স");
        titleText.add("ব্লাড ব্যাংক");
        titleText.add("মেডি অ্যাসিস্ট্যান্ট");


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
