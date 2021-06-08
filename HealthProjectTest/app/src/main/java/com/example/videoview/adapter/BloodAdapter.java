package com.example.videoview.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.videoview.R;
import com.example.videoview.activities.BloodProfileActivity;
import com.example.videoview.models.Blood;


import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BloodAdapter extends RecyclerView.Adapter<BloodAdapter.BloodViewHolder> implements View.OnClickListener,Filterable {

    private List<Blood> bloodList;

    private List<Blood> bloodListTempo;

    Context context;



    public BloodAdapter(Context context, List<Blood> bloodList) {
        this.context = context;
        this.bloodList = bloodList;
        bloodListTempo = new ArrayList<>(bloodList);

    }

    @NonNull
    @Override
    public BloodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blood_recycler_layout,parent,false);

        return new BloodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BloodViewHolder holder, int position) {

        Glide.with(holder.itemView.getContext()).load(bloodList.get(position).getImageUrl())
                .placeholder(R.drawable.blood_drop).diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(holder.bloodImageView);

        holder.bloodFbLinkText.setText("Fb Link: "+ bloodList.get(position).getFb_link());
        holder.bloodNameText.setText("Name: "+ bloodList.get(position).getName());
        holder.bloodMobileText.setText("Mobile: "+ bloodList.get(position).getMobile_no());


        holder.itemView.setTag(bloodList.get(position));
        holder.itemView.setOnClickListener(this);

    }

    @Override
    public int getItemCount() {
        return bloodList.size();
    }



    @Override
    public void onClick(View view) {
        if(view.getTag() instanceof Blood)
        {
            Blood blood = (Blood) view.getTag();
            Intent intent = new Intent(context.getApplicationContext(), BloodProfileActivity.class);
            intent.putExtra("blood", blood);
            context.startActivity(intent);

        }
    }

    @Override
    public Filter getFilter() {
        return bloodFilter;
    }

    private Filter bloodFilter = new Filter() {
        List<Blood>filteredBlood = new ArrayList<>();
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {


            if(charSequence.length()==0)
            {
                filteredBlood.clear();
            }
            else{
                filteredBlood.clear();
                String pattern = charSequence.toString().toLowerCase().trim();

                for(Blood blood : bloodListTempo)
                {
                    if(blood.getName().toLowerCase().contains(pattern))
                    {
                        filteredBlood.add(blood);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredBlood;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            bloodList.clear();
            bloodList.addAll((List)filterResults.values);
            notifyDataSetChanged();

        }
    };


    public static class BloodViewHolder extends RecyclerView.ViewHolder{

        CircleImageView bloodImageView;
        TextView bloodNameText, bloodFbLinkText,bloodMobileText;

        public BloodViewHolder(@NonNull View itemView) {
            super(itemView);

            bloodImageView = itemView.findViewById(R.id.blood_profile_image);
            bloodNameText = itemView.findViewById(R.id.bloodNameText);
            bloodFbLinkText =itemView.findViewById(R.id.bloodFbLinkText);
            bloodMobileText = itemView.findViewById(R.id.bloodMobileText);


        }
    }

}
