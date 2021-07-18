package com.example.android.maptrial;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.Console;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    public static String long_;
    public static String lat_;

    Button btn;
    TextView textView;
    TextView textView2;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        textView = findViewById(R.id.title);
        textView2 = findViewById(R.id.subtitle);
        btn = findViewById(R.id.button_1);
        ImageView imageView = findViewById(R.id.alert);

        Log.d("This is a recieved res" , ProceedActivity.result);

        if(ProceedActivity.result == "3"){
            textView.setText("Have a medical checkup in the next 48 hours");
            textView2.setText("Your symptoms are mildly alarming; it is recommended to seek attention in the next 48 hours");
            textView.setTextColor(Color.parseColor("#FFD600"));
            imageView.setImageResource(R.drawable.yellow);
        }
        if(ProceedActivity.result == "2"){
            textView.setText("Have a medical checkup at your earliest convenience");
            textView2.setText("Your symptoms are not alarming; it is recommended to run a medical checkup sometime soon");
            imageView.setImageResource(R.drawable.green);
            textView.setTextColor(Color.parseColor("#00C853"));
        }
        else if(ProceedActivity.result == "4"){
            textView.setText("Go to the hospital NOW");
            textView2.setText("Alarming symptoms. You must go to the nearest hospital as soon as possible.");
            textView.setTextColor(Color.parseColor("#FF9100"));
            imageView.setImageResource(R.drawable.orange);
        }
        else if(ProceedActivity.result == "5"){
            textView.setText("Call an Ambluance IMMEDIATELY");
            textView2.setText("You must call an ambluance as soon as possible");
            textView.setTextColor(Color.parseColor("#D50000"));
            imageView.setImageResource(R.drawable.red);
        }
        else if(ProceedActivity.result == "1"){
            textView.setText("You probably don't need a medical checkup");
            textView2.setText("No need to worry, stay safe.");
            imageView.setImageResource(R.drawable.blue);
        }






        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ContextCompat.checkSelfPermission(MainActivity.this , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    Log.d("I am","deny");
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, 100);
                }
                else
                {
                    getLocation();
                    Log.d("I am","accept");

                    try {
                        Thread.sleep(2500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Intent activityChangeIntent = new Intent(MainActivity.this, MapsActivity.class);
                    startActivity(activityChangeIntent);
                }



                // currentContext.startActivity(activityChangeIntent);



            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("i am","a permission");
        Log.d("i am permissions   ", String.valueOf(permissions[0]));
        Log.d("i am grant   ", String.valueOf(grantResults[0]));

        if(grantResults[0] == 0) {

            getLocation();

            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Intent activityChangeIntent = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(activityChangeIntent);
        }
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {

        Log.d("-","I am getting location");

        try {

            Log.d("-","I am getting location 2");

            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0,MainActivity.this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,0,MainActivity.this);


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        Log.d("-" , "I am changing");

        try{
            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String address = addresses.get(0).getAddressLine(0);

            long_ = String.valueOf(location.getLongitude());
            lat_ = String.valueOf(location.getLatitude());
           // textView.setText(String.valueOf(location.getLatitude()) + "   " + String.valueOf(location.getLongitude()) );

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }


}
