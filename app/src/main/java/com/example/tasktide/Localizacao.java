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


//Google Maps SDK for Android.
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
//Google Play Services Location.
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;


import com.google.android.material.floatingactionbutton.FloatingActionButton;


import org.json.JSONArray;
import org.json.JSONObject;


//Bibliotecas de Conexão HTTP.
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class Localizacao extends AppCompatActivity implements OnMapReadyCallback {


    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1; //número para identificar a solicitação de permissão de localização.
    private FusedLocationProviderClient fusedLocationClient;  //lida com a localização do dispositivo
    private GoogleMap mMap; //Um objeto que representa o mapa


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localizacao);
        // Configuração do fragmento de mapa


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }


        // Configuração do cliente de localização


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        // Configuração do botão de localização, o botao flutuante lá


        FloatingActionButton fabLocation = findViewById(R.id.fab_location);
        fabLocation.setOnClickListener(v -> {
            if (mMap != null) {
                getDeviceLocation();
            }
        });
    }


    //Bacana de citar na apresentação
    //Método onMapReady chamado quando o mapa está pronto para ser usado
    // Configura o mapa, solicita permissões de localização e adiciona um marcador para o destino
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // Solicitar permissões de localização se não estiverem concedidas


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            mMap.setMyLocationEnabled(true);
            getDeviceLocation();
        }


        // Coordenadas do destino


        LatLng enderecoIFAM = new LatLng(-3.1343665666971323, -60.012890561432684);
        mMap.addMarker(new MarkerOptions().position(enderecoIFAM).title("IFAM - Av. Sete de Setembro, 1975"));
    }


    //Bacana de citar na apresentação
    //Método getDeviceLocation para obter a localização atual do dispositivo/usuário
    // Se a localização for encontrada, a câmera do mapa é movida para a posição do usuário, e a rota até o destino é desenhada
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


    //Bacana de citar na apresentação
    //Método desenharRota, responsável por desenhar a rota no mapa entre a origem e o destino fornecidos
    // Chama o método DetalhesRota() para obter informações sobre a rota(como distancia e tempo estimado)




    //1. Ele pergunta para o Google Maps qual é o caminho
    //2. O Google Maps manda uma sequência de caracteres (como um código secreto) que descreve o caminho
    //3. Esse código secreto é decodificado pelo método decodePolyline para se transformar em uma lista de pontos no mapa
    //4. Esses pontos são usados para desenhar a linha do caminho no mapa
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


    //Bacana de citar na apresentação
    //Método responsável por buscar os detalhes da rota
    // como a distância e o tempo estimado, e decodificar a polyline para exibir no mapa
    //No contexto GoogleMaps, uma polyline é usada para representar um caminho ou rota entre diferentes pontos no mapa.




    //Sobre o JSON, que é como um pacote de informações que o Google Maps envia para o aplicativo.
    // O aplicativo então abre esse pacote, pega as partes que precisa (como a rota, distância e tempo)
    // e usa isso para mostrar a rota no mapa e outras informações úteis na tela


    //1. pergunta para o Google Maps sobre o caminho
    //2. O Google Maps manda uma sequência de caracteres (código secreto) e informações sobre a distância e o tempo
    //3. O código secreto é decodificado pelo método decodePolyline para criar uma lista de pontos
    //4. Esses pontos são usados para desenhar a linha do caminho no mapa e mostrar a distância e o tempo na tela
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
                        TextView distanceText = findViewById(R.id.distance_text);
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


    //Bacana pra citar na apresentação
    //Método auxiliar que decodifica uma polyline codificada em uma lista de pontos (LatLng)
    // para ser usada ao desenhar uma rota no mapa.
    // método usado para transformar uma sequência de caracteres (uma linha codificada) em uma lista de coordenadas geográficas (pontos no mapa)
    // Esses pontos têm latitude e longitude, que são os números que nos dizem exatamente onde algo está no planeta
    //resumindo: TODOS ESSES PONTOS MOSTRAM A ROTA COMPLETA NO MAPA HAHAHAH CORINGANDO AHAHAHAH












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


    //Bacana de citar na apresentação
    //Método chamado após o usuário responder a uma solicitação de permissão
    // Verifica se as permissões de localização foram concedidas
    // se sim, habilita a localização no mapa e obtém a localização do dispositivo
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
