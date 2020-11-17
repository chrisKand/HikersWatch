package com.example.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView detailsTextView;
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode ==1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        detailsTextView = findViewById(R.id.detailsTextView);


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(@NonNull Location location) {
                String textToShow = "";
                textToShow += "Latitude: " + location.getAltitude() + "\n\n";
                textToShow += "Longitude: " + location.getLongitude() + "\n\n";
                textToShow += "Accuracy: " + location.getAccuracy() + "\n\n";
                textToShow += "Altitude: " + location.getAltitude() + "\n\n";
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                String address = "";
                try {
                    List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                    if (listAddresses.get(0).getFeatureName() != null){
                        address += listAddresses.get(0).getFeatureName() + " ";
                    }
                    if (listAddresses.get(0).getThoroughfare() != null){
                        address += listAddresses.get(0).getThoroughfare() + "\n";
                    }
                    if (listAddresses.get(0).getAdminArea() != null){
                        address += listAddresses.get(0).getAdminArea() + "\n";
                    }

                    if (listAddresses.get(0).getLocality() != null){
                        address += listAddresses.get(0).getLocality() + "\n";
                    }

                    if (listAddresses.get(0).getPostalCode() != null){
                        address += listAddresses.get(0).getPostalCode();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                textToShow += "Address: \n";
                textToShow += address;

                detailsTextView.setText(textToShow);
            }
        };


        if (Build.VERSION.SDK_INT<23) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }
}