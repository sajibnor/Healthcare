package com.example.videoview.map_activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.QuickContactBadge;
import android.widget.Toast;

import com.example.videoview.R;
import com.example.videoview.databinding.ActivityUserMapsBinding;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserMapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    ActivityUserMapsBinding binding;


    private static final String TAG = "UserMapsActivity";
    private GoogleMap mMap;
    FusedLocationProviderClient locationProviderClient;
    Location userLocation;
    GeoFire geoFire;
    String name, phone;
    DatabaseReference userDatabaseReference;
    private DatabaseReference pharmacyDatabaseReference;
    private DatabaseReference connectRefernceDB;
    private List<LatLng> latLngs = new ArrayList<>();
    private double radius = 0.2;
    String closestPharmacyKey="";
    String pharmacyName="";
    String pharmacyPhone="";
    private boolean isPharmacyFound = false;
    private Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        name = getIntent().getStringExtra("name");
        phone = getIntent().getStringExtra("phone");

        geocoder =new Geocoder(this);
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        connectRefernceDB = FirebaseDatabase.getInstance().getReference().child("connections");
        geoFire = new GeoFire(userDatabaseReference);
        pharmacyDatabaseReference = FirebaseDatabase.getInstance().getReference().child("pharmacy");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        binding.instantPharmaServiceID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.callPharmaId.setVisibility(View.GONE);
                radius = 0.2;
                isPharmacyFound = false;
                getLastUserLocationAndClosestPharma();

            }
        });

        binding.callPharmaId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             pharmacyDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot snapshot) {
                     String phone = snapshot.child(closestPharmacyKey).child("phone").getValue().toString();

                     Log.e(TAG, "onDataChange: phone"+phone);
                     callClosestPharmacy(phone);
                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError error) {

                 }
             });

            }
        });
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

    public void getLastUserLocationAndClosestPharma() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> locationTask = locationProviderClient.getLastLocation();
        locationTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserMapsActivity.this, "লোকেশান পাওয়া যায়নি।", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {


                if(location!=null)
                {
                    userLocation = location;
                    geoFire.setLocation(phone, new GeoLocation(location.getLatitude(), location.getLongitude()));

                    setCircle(100, new LatLng(location.getLatitude(), location.getLongitude()), Color.RED);
                    setMarker(new LatLng(location.getLatitude(), location.getLongitude()), name, phone);

                    Toast.makeText(UserMapsActivity.this, "লোকেশান সেট হয়েছে।", Toast.LENGTH_SHORT).show();

                    getClosestPharmacy(location);
                }


            }
        });

    }

    private void getClosestPharmacy(Location location) {
        GeoFire fire = new GeoFire(pharmacyDatabaseReference);

        GeoQuery query = fire.queryAtLocation(new GeoLocation(location.getLatitude(), location.getLongitude()), radius);
        query.removeAllListeners();
        query.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation pharamaLocation) {
                if (!isPharmacyFound) {
                    isPharmacyFound = true;
                    mMap.clear();
                    for(LatLng latLng:latLngs)
                    {
                        setMarker(latLng,pharmacyName,pharmacyPhone);
                    }

                    setCircle(100.0, new LatLng(location.getLatitude(), location.getLongitude()), Color.RED);

                    closestPharmacyKey = key;
                    binding.callPharmaId.setVisibility(View.VISIBLE);
                    Toast.makeText(UserMapsActivity.this, "key: " + key, Toast.LENGTH_SHORT).show();

                    ///connectUserAndPharmacy(key);

                }
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                if (!isPharmacyFound) {
                    mMap.clear();
                    radius+=0.1;
                    setCircle(radius * 1000.0, new LatLng(location.getLatitude(), location.getLongitude()), Color.RED);
                    for(LatLng latLng:latLngs)
                    {
                        setMarker(latLng,pharmacyName,pharmacyPhone);
                    }
                    getClosestPharmacy(location);
                }

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });


    }

    /*private void connectUserAndPharmacy(String key) {

        Map<String,Object>userMap = new HashMap<>();
        Map<String,Object>pharmaMap = new HashMap<>();
        userMap.put("pharma",key);
        pharmaMap.put("user",phone);
        connectRefernceDB.child(phone).updateChildren(userMap);
        connectRefernceDB.child(key).updateChildren(pharmaMap);

    }*/


    public void setCircle(double radius, LatLng latLng, int strokColor) {

        mMap.addCircle(new CircleOptions().center(latLng)
                .fillColor(0x220000bb)
                .strokeColor(strokColor)
                .strokeWidth(3f).radius(radius));

    }

    public void setMarker(LatLng latLng, String name, String phone) {
        mMap.addMarker(new MarkerOptions().position(latLng)
                .title(name)
                .snippet(phone));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                addMarkerforAllPharmacy();
            }
        },1000);

        //deleteConnection();

    }

 /*   private void deleteConnection() {

        connectRefernceDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(phone).exists())
                {
                    snapshot.child(phone).child("pharma").getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }*/

    private void addMarkerforAllPharmacy() {


        latLngs.clear();

        pharmacyDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                Toast.makeText(UserMapsActivity.this, "triggerd", Toast.LENGTH_SHORT).show();
                triggerDatabase();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void triggerDatabase() {
        mMap.clear();
        latLngs.clear();
        pharmacyDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    double lat = Double.parseDouble(dataSnapshot.child("l").child("0").getValue().toString());
                    double lon = Double.parseDouble(dataSnapshot.child("l").child("1").getValue().toString());
                    pharmacyName = dataSnapshot.child("name").getValue().toString();
                    pharmacyPhone = dataSnapshot.child("phone").getValue().toString();


                    latLngs.add(new LatLng(lat,lon));
                    setMarker(new LatLng(lat,lon),pharmacyName,pharmacyPhone);

                    Log.e(TAG, "onDataChange: " + lat + "\n" + lon+"\n"+pharmacyName+"\n"+pharmacyPhone);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void callClosestPharmacy(String phone)
    {
        String connectedPharma = closestPharmacyKey;

        if(userLocation !=null)
        {
            Toast.makeText(this, "preparing to connect...", Toast.LENGTH_SHORT).show();
            List<Address>addresses = null;
            try {
                addresses = geocoder.getFromLocation(userLocation.getLatitude(),userLocation.getLongitude(),1);
                Address address = addresses.get(0);
                String userAddresss ="Address Line: "+address.getAddressLine(0)+"\n"
                        +"Locality: "+address.getLocality()+"\n"
                        +"Sub Locality: "+address.getSubLocality()+"\n"
                        +"Lat: "+address.getLatitude()+"\n"
                        +"Lon: "+address.getLongitude()+"\n"
                        +"Premises: "+address.getPremises()+"\n"
                        +"Postal Code"+address.getPostalCode()+"\n"
                        +"Fare:"+address.getThoroughfare()
                        +"Sub fare: "+address.getSubThoroughfare();


                if(!pharmacyPhone.isEmpty())
                {
                    Toast.makeText(this, "calling and sendign sms...", Toast.LENGTH_SHORT).show();
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(pharmacyPhone,null,userAddresss,null,null);
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                    startActivity(intent);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }



        }


    }

}