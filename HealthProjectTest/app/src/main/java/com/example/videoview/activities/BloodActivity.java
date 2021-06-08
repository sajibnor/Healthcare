package com.example.videoview.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.videoview.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.videoview.adapter.BloodAdapter;
import com.example.videoview.databinding.ActivityBloodBinding;
import com.example.videoview.models.Blood;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class BloodActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private ActivityBloodBinding binding;
    private String blood_url;
    private List<Blood> bloodList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private BloodAdapter adapter;


    @Override
    protected void onStart() {
        super.onStart();
        getAllBloods();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBloodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        requestQueue = Volley.newRequestQueue(this);
        blood_url = "https://healthproject101.000webhostapp.com/fetchall_bloodbank.php";



        binding.searchBloodID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(editable.toString().isEmpty())
                {
                    binding.noResID.setVisibility(View.VISIBLE);
                    binding.bloodRecyclerViewId.setAlpha(0);


                }
                else{
                    binding.bloodRecyclerViewId.setAlpha(1);
                    binding.noResID.setVisibility(View.GONE);
                    adapter.getFilter().filter(editable.toString());
                    adapter.notifyDataSetChanged();
                }

            }
        });

        adapter = new BloodAdapter(this,bloodList);
        linearLayoutManager = new LinearLayoutManager(this);
        binding.bloodRecyclerViewId.setHasFixedSize(true);
        binding.bloodRecyclerViewId.setAlpha(0);
        binding.bloodRecyclerViewId.setLayoutManager(linearLayoutManager);





    }

    private void getAllBloods() {
        bloodList.clear();

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, blood_url
                , null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                if(response !=null&& response.length()>0){

                    Toast.makeText(BloodActivity.this, "working", Toast.LENGTH_SHORT).show();

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            String name = response.getJSONObject(i).getString("name");
                            String fb_link = response.getJSONObject(i).getString("fb_link");
                            String mobile_no = response.getJSONObject(i).getString("mobile_no");
                            String mail = response.getJSONObject(i).getString("mail");
                            String imageUrl = response.getJSONObject(i).getString("image");
                            String password = response.getJSONObject(i).getString("password");

                            Blood blood = new Blood(
                                    name,
                                    fb_link,
                                    mobile_no,
                                    mail,
                                    imageUrl,
                                    password
                            );


                            bloodList.add(blood);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    adapter = new BloodAdapter(BloodActivity.this,bloodList);
                    binding.bloodRecyclerViewId.setAdapter(adapter);

                } else {

                    Toast.makeText(BloodActivity.this, "not working", Toast.LENGTH_SHORT).show();

                }




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(BloodActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(arrayRequest);

    }
}