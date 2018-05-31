package com.example.zarar.streetview;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;
import com.google.android.gms.maps.model.StreetViewPanoramaLocation;






/*public class MapsActivity extends FragmentActivity implements OnStreetViewPanoramaReadyCallback {

    private StreetViewPanorama sStreet;

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate (savedInstanceState);
        setContentView(R.layout.activity_maps);

        StreetViewPanoramaFragment streetViewPanoramaFragment = (StreetViewPanoramaFragment) getFragmentManager()
                .findFragmentById(R.id.streetviewpanorama);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);
    }


    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama panorama) {
        panorama.setPosition(new LatLng(33.811814,151.025127));
    }*/


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, OnStreetViewPanoramaReadyCallback {

    private GoogleMap mMap;
    private StreetViewPanorama sStreet;
    private LatLng wsu = new LatLng(-33.811269, 151.024738);
    private LatLng opera = new LatLng(-33.858397, 151.213574);
    private LatLng paris = new LatLng(48.858093, 2.294694);
    private LatLng venice = new LatLng(45.437806, 12.335567);

    private static final String LOG_TAG = "Google Places Autocomplete";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "------your api key here -------";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        final Button button = findViewById(R.id.type);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    button.setText("Map");
                } else {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    button.setText("Satellite");
                }
            }
        });


        StreetViewPanoramaFragment streetViewPanoramaFragment = (StreetViewPanoramaFragment) getFragmentManager()
                .findFragmentById(R.id.streetviewpanorama);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);


        // HEY DAVE WE ARE HAVING AN ISSUE BELOW HERE WITH LAT LONG AND PANO ID SEE CAMS EMAIL FOR MORE INFORMATION




        //  method for on click on images and updating the map
        ImageView img1 = findViewById(R.id.imageView1);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMap.moveCamera(CameraUpdateFactory.newLatLng(opera));
                mMap.moveCamera(CameraUpdateFactory.zoomTo(14));
                mMap.addMarker(new MarkerOptions().position(opera).title("Opera House"));

            }
        });

        ImageView img2 = findViewById(R.id.imageView2);
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMap.moveCamera(CameraUpdateFactory.newLatLng(paris));
                mMap.moveCamera(CameraUpdateFactory.zoomTo(14));
                mMap.addMarker(new MarkerOptions().position(paris).title("Eiffel Tower"));

            }
        });

        ImageView img3 = findViewById(R.id.imageView3);
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMap.moveCamera(CameraUpdateFactory.newLatLng(venice));
                mMap.moveCamera(CameraUpdateFactory.zoomTo(14));
                mMap.addMarker(new MarkerOptions().position(venice).title("Venice Canal"));

            }
        });

    }


    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama panorama) {
        sStreet = panorama;

        panorama.setPosition(opera);


        sStreet.setOnStreetViewPanoramaChangeListener(new StreetViewPanorama.OnStreetViewPanoramaChangeListener() {
            @Override
            public void onStreetViewPanoramaChange(StreetViewPanoramaLocation streetViewPanoramaLocation) {


                StreetViewPanoramaLocation location = sStreet.getLocation();

                LatLng pos = location.position;

                mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));

            }
        });


        sStreet.setOnStreetViewPanoramaCameraChangeListener(new StreetViewPanorama.OnStreetViewPanoramaCameraChangeListener() {
            @Override
            public void onStreetViewPanoramaCameraChange(StreetViewPanoramaCamera streetViewPanoramaCamera) {

                StreetViewPanoramaLocation location = sStreet.getLocation();

                Log.d("pos", location.toString());

                LatLng pos = location.position;

                Log.d("pos2", pos.toString());

                mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
                mMap.addMarker(new MarkerOptions().position(pos).title("Marker"));
                mMap.moveCamera(CameraUpdateFactory.zoomTo(16));
            }
        });


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
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

        mMap.addMarker(new MarkerOptions().position(wsu).title("My Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(wsu));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(9));
        mMap.setMyLocationEnabled(true);
    }


}


//make some changes