package com.example.tasktide;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Localizacao extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localizacao);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            mMap.setMyLocationEnabled(true);
            getDeviceLocation();
        }

        LatLng enderecoIFAM = new LatLng(-3.1343665666971323, -60.012890561432684);
        mMap.addMarker(new MarkerOptions().position(enderecoIFAM).title("IFAM - Av. Sete de Setembro, 1975"));
    }

    private void getDeviceLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));

                LatLng enderecoIFAM = new LatLng(-3.1343665666971323, -60.012890561432684);
                desenharRota(userLocation, enderecoIFAM);
            }
        });
    }

    private void desenharRota(LatLng origin, LatLng destination) {
        DetalhesRota(origin, destination);
        new Thread(() -> {
            try {
                String url = "https://maps.googleapis.com/maps/api/directions/json?origin="
                        + origin.latitude + "," + origin.longitude
                        + "&destination=" + destination.latitude + "," + destination.longitude
                        + "&key=AIzaSyDXskG52GlExtiuhK_auSINgRvQubTVvjU";


                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;


                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();


                JSONObject jsonResponse = new JSONObject(result.toString());
                JSONArray routes = jsonResponse.getJSONArray("routes");
                if (routes.length() > 0) {
                    JSONObject route = routes.getJSONObject(0);
                    JSONObject overviewPolyline = route.getJSONObject("overview_polyline");
                    String encodedPoints = overviewPolyline.getString("points");
                    List<LatLng> polylinePoints = decodePolyline(encodedPoints);


                    runOnUiThread(() -> mMap.addPolyline(new PolylineOptions().addAll(polylinePoints)));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void DetalhesRota(LatLng origin, LatLng destination) {
        new Thread(() -> {
            try {
                String url = "https://maps.googleapis.com/maps/api/directions/json?origin="
                        + origin.latitude + "," + origin.longitude
                        + "&destination=" + destination.latitude + "," + destination.longitude
                        + "&key=AIzaSyDXskG52GlExtiuhK_auSINgRvQubTVvjU";


                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;


                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();


                JSONObject jsonResponse = new JSONObject(result.toString());
                JSONArray routes = jsonResponse.getJSONArray("routes");
                if (routes.length() > 0) {
                    JSONObject route = routes.getJSONObject(0);
                    JSONObject legs = route.getJSONArray("legs").getJSONObject(0);

                    String distance = legs.getJSONObject("distance").getString("text");
                    String duration = legs.getJSONObject("duration").getString("text");

                    JSONObject overviewPolyline = route.getJSONObject("overview_polyline");
                    String encodedPoints = overviewPolyline.getString("points");
                    List<LatLng> polylinePoints = decodePolyline(encodedPoints);

                    runOnUiThread(() -> {
                        TextView distanceText = findViewById(R.id.txtLocalizacao);
                        distanceText.setText("Distância: " + distance + " | Tempo estimado: " + duration);

                        if (mMap != null) {
                            mMap.addPolyline(new PolylineOptions().addAll(polylinePoints));
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private List<LatLng> decodePolyline(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;


        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;
            LatLng p = new LatLng(((lat / 1E5)), ((lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mMap.setMyLocationEnabled(true);
                getDeviceLocation();
            } else {
                Toast.makeText(this, "Permissão negada", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
