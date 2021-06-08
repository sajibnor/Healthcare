package com.example.videoview.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.videoview.R;


import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SliderAdapter extends PagerAdapter {
    Context context;
    List<Integer> images = new ArrayList<>();

    List<String> slideText = new ArrayList<>();
    List<String> titleText = new ArrayList<>();

    public SliderAdapter(Context context, List<Integer> img1, List<String> slideText, List<String> titleText) {
        this.context = context;
        this.images= img1;
        this.slideText = slideText;
        this.titleText = titleText;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view==(LinearLayout)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.slider_layout,null);

        ImageView sliderImage = view.findViewById(R.id.sliderImageID);

        TextView sliderText = view.findViewById(R.id.sliderText);
        TextView title = view.findViewById(R.id.titleID);

        Glide.with(context).load(images.get(position)).into(sliderImage);



        sliderText.setText(slideText.get(position));
        title.setText(titleText.get(position));

        container.addView(view);
        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
       container.removeView((LinearLayout)object);
    }
}
