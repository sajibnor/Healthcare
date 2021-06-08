package com.example.videoview.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.videoview.R;
import com.example.videoview.activities.PharmacyProfileActivity;
import com.example.videoview.models.Pharmacy;


import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class pharmacyAdapter extends RecyclerView.Adapter<pharmacyAdapter.PharmacyViewHolder>  implements View.OnClickListener, Filterable {

    private List<Pharmacy> pharmacyList = new ArrayList<>();
    private List<Pharmacy> pharmacyListTemp;

    //add context ;
           Context context;

    public pharmacyAdapter(List<Pharmacy> pharmacyList, Context context) {
        this.pharmacyList = pharmacyList;
        this.context = context;
        pharmacyListTemp = new ArrayList<>(pharmacyList);
    }



    @NonNull
    @Override
    public PharmacyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

           View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pharmacy_recycler_layout,parent,false);

        return new PharmacyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PharmacyViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext()).load(pharmacyList.get(position).getImage())
                .placeholder(R.drawable.pharmacy).diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(holder.PharmacyImageView);

           holder.PharmacyNameText.setText(pharmacyList.get(position).getName());
           holder.PharmacyMobile.setText("Mobile: " +pharmacyList.get(position).getMobile_no());
           holder.PharmacyLicenseNo.setText("License: " +pharmacyList.get(position).getLicense_no());

           holder.itemView.setTag(pharmacyList.get(position));
           holder.itemView.setOnClickListener(this);

    }

    @Override
    public int getItemCount() {
        return pharmacyList.size();
    }

    @Override
    public void onClick(View view) {
        if (view.getTag() instanceof Pharmacy) {
            Pharmacy pharmacy =(Pharmacy) view.getTag();
            Intent intent = new Intent(context.getApplicationContext(), PharmacyProfileActivity.class);
            intent.putExtra("pharmacy",pharmacy);
            context.startActivity(intent);
            Toast.makeText(context, "name: " +pharmacy.getName(), Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public Filter getFilter() {
        return pharmacyFilter;
    }
    private Filter pharmacyFilter= new Filter() {
        List<Pharmacy> filteredPharmacy = new ArrayList<>();

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            if (charSequence.length() == 0){

             filteredPharmacy.clear();

            }
            else {
                filteredPharmacy.clear();
                String pattern = charSequence.toString().toLowerCase().trim();

                for (Pharmacy pharmacy: pharmacyListTemp ){
                    if (pharmacy.getName().toLowerCase().contains(pattern))
                    {
                        filteredPharmacy.add(pharmacy);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredPharmacy;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {


            pharmacyList.clear();
            pharmacyList.addAll((List)filterResults.values);
            notifyDataSetChanged();

        }
    };


    public static class PharmacyViewHolder extends RecyclerView.ViewHolder {

           CircleImageView PharmacyImageView;
           TextView PharmacyNameText, PharmacyMobile, PharmacyLicenseNo;

        public PharmacyViewHolder(@NonNull View itemView) {
            super(itemView);

            PharmacyImageView = itemView.findViewById(R.id.pharmacy_profile_imageId);
            PharmacyNameText = itemView.findViewById(R.id.pharmacy_nameTextId);
            PharmacyMobile = itemView.findViewById(R.id.pharmacy_mobile_noId);
            PharmacyLicenseNo = itemView.findViewById(R.id.pharmacy_license_noId);

        }
    }
}
