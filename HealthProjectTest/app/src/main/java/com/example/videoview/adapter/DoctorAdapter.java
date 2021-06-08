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
import com.example.videoview.activities.DoctorProfileActivity;
import com.example.videoview.models.Doctor;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHodler> implements View.OnClickListener,Filterable {

    private List<Doctor>doctorList = new ArrayList<>();

    private List<Doctor>doctorListTempo;

    Context context;



    public DoctorAdapter(Context context, List<Doctor> doctorList) {
        this.context = context;
        this.doctorList = doctorList;
        doctorListTempo = new ArrayList<>(doctorList);

    }

    @NonNull
    @Override
    public DoctorViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_recycler_layout,parent,false);

        return new DoctorViewHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHodler holder, int position) {

        Glide.with(holder.itemView.getContext()).load(doctorList.get(position).getImageUrl())
                .placeholder(R.drawable.my_doc).diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(holder.docImageView);

        holder.docBmdcText.setText("BMDC: "+doctorList.get(position).getBmdc_no());
        holder.docNameText.setText("Name: "+doctorList.get(position).getName());
        holder.docMobileText.setText("Mobile: "+doctorList.get(position).getMobile_no());


        holder.itemView.setTag(doctorList.get(position));
        holder.itemView.setOnClickListener(this);

    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }



    @Override
    public void onClick(View view) {
        if(view.getTag() instanceof Doctor)
        {
            Doctor doctor = (Doctor) view.getTag();
            Intent intent = new Intent(context.getApplicationContext(), DoctorProfileActivity.class);
            intent.putExtra("doctor",doctor);
            context.startActivity(intent);

            return;

        }
    }

    @Override
    public Filter getFilter() {
        return doctorFilter;
    }

    private Filter doctorFilter = new Filter() {
        List<Doctor>filteredDoc = new ArrayList<>();
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {


           if(charSequence.length()==0)
           {
               filteredDoc.clear();
           }
           else{
               filteredDoc.clear();
               String pattern = charSequence.toString().toLowerCase().trim();

               for(Doctor doctor:doctorListTempo)
               {
                   if(doctor.getName().toLowerCase().contains(pattern))
                   {
                       filteredDoc.add(doctor);
                   }
               }
           }

           FilterResults results = new FilterResults();
           results.values = filteredDoc;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            doctorList.clear();
            doctorList.addAll((List)filterResults.values);
            notifyDataSetChanged();

        }
    };


    public static class DoctorViewHodler extends RecyclerView.ViewHolder{

        CircleImageView docImageView;
        TextView docNameText,docBmdcText,docMobileText;

        public DoctorViewHodler(@NonNull View itemView) {
            super(itemView);

            docImageView = itemView.findViewById(R.id.doc_profile_image);
            docNameText = itemView.findViewById(R.id.docNameText);
            docBmdcText=itemView.findViewById(R.id.docBmdcText);
            docMobileText = itemView.findViewById(R.id.docMobileText);


        }
    }

}


