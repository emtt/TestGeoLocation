package com.mobilize.testgeolocation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    private FusedLocationProviderClient mFusedLocationClient;
    private static final int PERMISSION_REQ_LOCATION_CODE = 1234;
    private static boolean locationPermissionExplained = false;

    private Button locationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationButton = (Button) findViewById(R.id.btnLocation);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestLocation();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch(requestCode){
            case PERMISSION_REQ_LOCATION_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    System.out.println("PERMISOS ESTABLECIDOS");
                    requestLocation();
                }else{
                    Toast.makeText(MainActivity.this, "SON NECESARIOS LOS PERMISOS DE LOCALIZACIÓN!", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    private void requestLocation(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) && !locationPermissionExplained){
                Toast.makeText(MainActivity.this, "SON NECESARIOS LOS PERMISOS DE LOCALIZACIÓN!", Toast.LENGTH_SHORT).show();
                locationPermissionExplained = true;
            }else{
                locationPermissionExplained = false;
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQ_LOCATION_CODE);
            }
        }else{
            try {
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Toast.makeText(MainActivity.this, "POSICIÓN ACTUAL: " + location.getLongitude() + " " + location.getLatitude(), Toast.LENGTH_SHORT).show();
                    }
                });
            }catch(SecurityException e){
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
