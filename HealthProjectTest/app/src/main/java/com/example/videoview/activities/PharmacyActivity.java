package com.example.videoview.activities;

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
import com.example.videoview.R;
import com.example.videoview.adapter.pharmacyAdapter;

import com.example.videoview.databinding.ActivityPharmacyBinding;
import com.example.videoview.models.Pharmacy;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class PharmacyActivity extends AppCompatActivity {
    RequestQueue requestQueue;

    private String pharmacy_Url;
    private ArrayList<Pharmacy> pharmacyList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private pharmacyAdapter adapter;
    private ActivityPharmacyBinding binding;

    @Override
    protected void onStart() {
        super.onStart();
        pharmacyList.clear();
        getAllPharmacy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPharmacyBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        requestQueue = Volley.newRequestQueue(this);


        pharmacy_Url = "https://healthproject101.000webhostapp.com/fetchall_pharmacy.php";
        binding.SearchPharmacyId.addTextChangedListener(new TextWatcher() {
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

                    binding.PharmacyRecyclerViewId.setAlpha(0);
                    binding.TVnoResultId.setVisibility(View.GONE);

                }
                else{
                    Toast.makeText(PharmacyActivity.this, "else", Toast.LENGTH_SHORT).show();

                    binding.PharmacyRecyclerViewId.setAlpha(1);
                    binding.TVnoResultId.setVisibility(View.GONE);
                    adapter.getFilter().filter(editable.toString());

                }



            }
        });


        adapter = new pharmacyAdapter(pharmacyList, PharmacyActivity.this);
        linearLayoutManager = new LinearLayoutManager(this);
        binding.PharmacyRecyclerViewId.setHasFixedSize(true);
        binding.PharmacyRecyclerViewId.setAlpha(0);
        binding.PharmacyRecyclerViewId.setLayoutManager(linearLayoutManager);


    }

    private void getAllPharmacy() {
        pharmacyList.clear();

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, pharmacy_Url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        String name = response.getJSONObject(i).getString("name");
                        String mobile_no = response.getJSONObject(i).getString("mobile_no");
                        String image = response.getJSONObject(i).getString("image");
                        String license_no = response.getJSONObject(i).getString("license_no");
                        String mail = response.getJSONObject(i).getString("mail");
                        String password = response.getJSONObject(i).getString("password");


                        Pharmacy pharmacy = new Pharmacy(name, mobile_no, image, license_no, mail, password);
                        pharmacyList.add(pharmacy);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapter = new pharmacyAdapter(pharmacyList, PharmacyActivity.this);
                binding.PharmacyRecyclerViewId.setAdapter(adapter);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(PharmacyActivity.this, "No Result Found, Check Your Internet Connection", Toast.LENGTH_SHORT).show();

            }
        });
        requestQueue.add(arrayRequest);

    }
}