package com.example.videoview.map_activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.videoview.R;
import com.example.videoview.databinding.ActivityPharmacyMapsBinding;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class PharmacyMapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ActivityPharmacyMapsBinding binding;
    FusedLocationProviderClient locationProviderClient;
    Location pharmacyLocation;
    String pharmacyLicenece,pharmacyPhone,pharmacyName;
    GeoFire geoFire;
    DatabaseReference pharmacyDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPharmacyMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        pharmacyDatabaseReference = FirebaseDatabase.getInstance().getReference().child("pharmacy");
        geoFire = new GeoFire(pharmacyDatabaseReference);
        pharmacyLicenece = getIntent().getStringExtra("licence");
        pharmacyPhone = getIntent().getStringExtra("phone");
        pharmacyName = getIntent().getStringExtra("name");
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setSupportActionBar(binding.toolbarID);
        getSupportActionBar().setTitle("Pharmacy");

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mMap.setMyLocationEnabled(true);
        mMap.setTrafficEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pharmacy_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.setLocationMenuID) {
            setLocation();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> locationTask = locationProviderClient.getLastLocation();
        locationTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PharmacyMapsActivity.this, "লোকেশান পাওয়া যায়নি।", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null)
                {
                    pharmacyLocation = location;
                    Toast.makeText(PharmacyMapsActivity.this, "লোকেশান সেট হয়েছে।", Toast.LENGTH_SHORT).show();

                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude())));

                   geoFire.setLocation(pharmacyLicenece,new GeoLocation(location.getLatitude(),location.getLongitude()));

                    Map<String,Object> map = new HashMap<>();
                    map.put("phone",pharmacyPhone);
                    map.put("name",pharmacyName);

                    pharmacyDatabaseReference.child(pharmacyLicenece).updateChildren(map);


                }
            }
        });


    }
}