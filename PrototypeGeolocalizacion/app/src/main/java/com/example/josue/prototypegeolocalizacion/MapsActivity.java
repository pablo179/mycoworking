package com.example.josue.prototypegeolocalizacion;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker marcador;
    private double lat = 0.0;
    private double lng = 0.0;
    private String valeeee;
    //private final double REVOLATITUD = 20.1205679;
    //private final double REVOLONGITUD = -98.7369749;
    private final double REVOLATITUD = 20.12057;
    private final double REVOLONGITUD = -98.73697;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        miUbicacion();
    }

    private boolean checarSiEsta() {
        boolean vale;
        if(lat >= (REVOLATITUD - 0.00003) && lat <= (REVOLATITUD + 0.00003))
            vale = true;
        else
            vale = false;

        if(lng >= (REVOLONGITUD - 0.00003) && lng <= (REVOLONGITUD + 0.00003))
            vale = true;
        else
            vale = false;

        return vale;
    }

    private void agregarMarcador() {
        LatLng coordenada = new LatLng(lat, lng);
        CameraUpdate ubicacion = CameraUpdateFactory.newLatLngZoom(coordenada, 10);
        if (marcador != null)
            marcador.remove();

        if(checarSiEsta())
            valeeee = "ESTAS EN REVO300";
        else
            valeeee = "NO ESTAS EN REVO300";

        marcador = mMap.addMarker(new MarkerOptions()
                .position(coordenada)
                .title(valeeee)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        marcador.showInfoWindow();
        mMap.animateCamera(ubicacion);
    }

    private void actualizarUbicacion(Location location) {
        if (location != null) {
            lat = Math.round(location.getLatitude() * 1000000.0) / 1000000.0;
            lng = Math.round(location.getLongitude() * 1000000.0) / 1000000.0;
            agregarMarcador();
        }
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            actualizarUbicacion(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private void miUbicacion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location;
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
        }
        else if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 0, locationListener);
        }
        else {
            location = null;
        }

        actualizarUbicacion(location);
    }
}
