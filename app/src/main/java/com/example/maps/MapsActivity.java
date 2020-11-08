package com.example.maps;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private final LatLng VICOSA = new LatLng(-20.760730, -42.882610);
    private final LatLng DIVINO = new LatLng(-20.6036591, -42.1433508);
    private final LatLng UFV = new LatLng(-20.7610693, -42.8723519);

    public Location myPos;
    public Marker me;

    private final int FINE_ACCESS_PERMISSION = 1;

    private GoogleMap mMap;

    public LocationManager lm;
    public Criteria criteria;
    public String provider;
    public int TEMPO_REQ_LATLONG = 500;
    public int DISTANCIA_MIN_METROS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();

        PackageManager packageManager = getPackageManager();
        boolean hasGPS = packageManager.hasSystemFeature(packageManager.FEATURE_LOCATION_GPS);

        if (hasGPS) {
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            Log.i("LOCATION", "Usando GPS");
        } else {
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            Log.i("LOCATION", "Usando WI-FI ou dados");
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        provider = lm.getBestProvider(criteria, true);

        if (provider == null) {
            Log.e("PROVEDOR", "Nenhum provedor encontrado!");
        } else {
            Log.i("PROVEDOR", "Está sendo utilizado o provedor" + provider);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_ACCESS_PERMISSION);
                    Toast.makeText(this, "Permita o uso da sua localização via GPS", Toast.LENGTH_LONG).show();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_ACCESS_PERMISSION);
                }
            }
            lm.requestLocationUpdates(provider, TEMPO_REQ_LATLONG, DISTANCIA_MIN_METROS, this);
        }
    }

    @Override
    protected void onDestroy() {
        lm.removeUpdates(this);
        Log.w("PROVEDOR", "Provedor " + provider + " parado!");
        super.onDestroy();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if(location != null){
            myPos = location;
        }
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Intent it = getIntent();
        int startAt = it.getIntExtra("foco", 0);
        Log.i("where", startAt + "");

        // Add a marker in Sydney and move the camera
        mMap.addMarker(new MarkerOptions().position(VICOSA).title("Apt de Viçosa"));
        mMap.addMarker(new MarkerOptions().position(DIVINO).title("Casa dos pais"));
        mMap.addMarker(new MarkerOptions().position(UFV).title("Faculdade"));

        if(startAt == 0) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DIVINO, 16));
        } else if(startAt == 1) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(VICOSA, 16));
        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(UFV, 16));
        }


//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_ACCESS_PERMISSION);
//                Toast.makeText(this, "Permita o uso da sua localização via GPS", Toast.LENGTH_LONG).show();
//            } else {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_ACCESS_PERMISSION);
//            }
//
//            return;
//        }
//        mMap.setMyLocationEnabled(true);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case FINE_ACCESS_PERMISSION: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("Permission", "Deu a permissão para FINE_LOCATION_ACCESS");
                } else {
                    Log.i("Permission", "Não permitiu FINE_LOCATION_ACCESS");
                }
            }
        }
    }

    public void Divino(View v){
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(DIVINO, 16);
        mMap.animateCamera(update);
    }

    public void Vicosa(View v){
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(VICOSA, 16);
        mMap.animateCamera(update);
    }

    public void Ufv(View v){
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(UFV, 16);
        mMap.animateCamera(update);
    }

    public void ChangeMapView(View v){
        String tag =  v.getTag().toString();

        if(tag.equals("normal")) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        } else if(tag.equals("sat")) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else {
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        }
    }

    public void addMarkerToMyPosition(View v) {
        double lat = myPos.getLatitude();
        double longit = myPos.getLongitude();
        final LatLng posicaoAtual = new LatLng(lat, longit);

        final Location apVicosa = new Location(provider);
        apVicosa.setLatitude(-20.760730);
        apVicosa.setLongitude(-42.882610);



        double distancia = myPos.distanceTo(apVicosa) / 1000;

        DecimalFormat df = new DecimalFormat("0.##");

        Toast.makeText(this, "Distância " + df.format(distancia), Toast.LENGTH_SHORT).show();

        if(me != null) {
            me.remove();
        }
        me = mMap.addMarker(new MarkerOptions().position(posicaoAtual).title("Minha localização atual").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(posicaoAtual, 16);
        mMap.animateCamera(update);
    }
}