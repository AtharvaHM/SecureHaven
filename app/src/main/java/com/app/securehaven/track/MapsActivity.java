package com.app.securehaven.track;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.app.securehaven.databinding.ActivityMapsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.app.securehaven.DashboardActivity;
import com.app.securehaven.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient mfusedLocationProviderclient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.app.securehaven.databinding.ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button btn_back = findViewById(R.id.maps_btn_back);
        Button btn_update = findViewById(R.id.maps_btn_update);


        btn_back.setOnClickListener(v -> {
            Intent i = new Intent(MapsActivity.this, DashboardActivity.class);
            finish();
            startActivity(i);
        });



        mfusedLocationProviderclient =  LocationServices.getFusedLocationProviderClient(this);
    }

    private void getCurrentLocation() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mfusedLocationProviderclient.getLastLocation().addOnCompleteListener(task -> {
                TextView tv_latitude = findViewById(R.id.maps_tv_latitude);
                TextView tv_longitude = findViewById(R.id.maps_tv_longitude);
                Location loc = task.getResult();

                if(loc != null) {
                    Double latitude = loc.getLatitude();
                    Double longitude = loc.getLongitude();
                    LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng));
                    CameraUpdate cameraUpdateFactory = CameraUpdateFactory.newLatLngZoom(latLng, 16.0f);
                    mMap.moveCamera(cameraUpdateFactory);
                    tv_latitude.setText("Latitude: "+ latitude);
                    tv_longitude.setText("Longitude: "+ longitude);
                    FirebaseDatabase.getInstance().getReference("Location")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(loc);
                }
                else {
                    Toast.makeText(MapsActivity.this,"No Location found", Toast.LENGTH_LONG).show();
                }

            });
        } else {
            Toast.makeText(MapsActivity.this, "Location permissions denied",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        getCurrentLocation();
    }
}