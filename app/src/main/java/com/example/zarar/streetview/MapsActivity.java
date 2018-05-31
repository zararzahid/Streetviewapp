package com.example.zarar.streetview;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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
    private Marker myMarker;

    private LatLng wsu = new LatLng(-33.811269, 151.024738);
    private LatLng opera = new LatLng(-33.858397, 151.213574);
    private LatLng paris = new LatLng(48.858093, 2.294694);
    private LatLng venice = new LatLng(45.437806, 12.335567);
    private LatLng surfworld = new LatLng(-33.890311, 151.273802);
    private LatLng park = new LatLng(-27.603669, 152.910308);
    private LatLng pittstreet = new LatLng(-33.872688, 151.208149);

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
        ImageView img1 = findViewById(R.id.opera);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sStreet.setPosition(opera);

            }
        });

        ImageView img2 = findViewById(R.id.paris);
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sStreet.setPosition(paris);


            }
        });

        ImageView img3 = findViewById(R.id.venice);
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sStreet.setPosition(venice);

            }
        });

        ImageView img4 = findViewById(R.id.surf);
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sStreet.setPosition(surfworld);

            }
        });

        ImageView img5 = findViewById(R.id.park);
        img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sStreet.setPosition(park);

            }
        });

        ImageView img6 = findViewById(R.id.pitt);
        img6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sStreet.setPosition(pittstreet);

            }
        });
    }


    @Override
    public void onStreetViewPanoramaReady(final StreetViewPanorama panorama) {
        sStreet = panorama;

        panorama.setPosition(opera);


        sStreet.setOnStreetViewPanoramaChangeListener(new StreetViewPanorama.OnStreetViewPanoramaChangeListener() {
            @Override
            public void onStreetViewPanoramaChange(StreetViewPanoramaLocation streetViewPanoramaLocation) {


                StreetViewPanoramaLocation location = sStreet.getLocation();

                LatLng pos = location.position;

                mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
                mMap.moveCamera(CameraUpdateFactory.zoomTo(16));

                myMarker.setPosition(pos);


            }
        });


        sStreet.setOnStreetViewPanoramaCameraChangeListener(new StreetViewPanorama.OnStreetViewPanoramaCameraChangeListener() {
            @Override
            public void onStreetViewPanoramaCameraChange(StreetViewPanoramaCamera streetViewPanoramaCamera) {

                if (streetViewPanoramaCamera.bearing >= 0 && streetViewPanoramaCamera.bearing <= 22.5) {
                    myMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.p0));

                } else if (streetViewPanoramaCamera.bearing >= 22.6 && streetViewPanoramaCamera.bearing <= 45) {
                    myMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.p1));
                } else if (streetViewPanoramaCamera.bearing >= 45.1 && streetViewPanoramaCamera.bearing <= 67.5) {
                    myMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.p2));
                } else if (streetViewPanoramaCamera.bearing >= 67.6 && streetViewPanoramaCamera.bearing <= 90) {
                    myMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.p3));
                } else if (streetViewPanoramaCamera.bearing >= 90.1 && streetViewPanoramaCamera.bearing <= 112.5) {
                    myMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.p4));
                } else if (streetViewPanoramaCamera.bearing >= 112.6 && streetViewPanoramaCamera.bearing <= 135) {
                    myMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.p5));
                } else if (streetViewPanoramaCamera.bearing >= 135.1 && streetViewPanoramaCamera.bearing <= 157.5) {
                    myMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.p6));
                } else if (streetViewPanoramaCamera.bearing >= 157.6 && streetViewPanoramaCamera.bearing <= 180) {
                    myMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.p7));
                } else if (streetViewPanoramaCamera.bearing >= 180.1 && streetViewPanoramaCamera.bearing <= 202.5) {
                    myMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.p8));
                } else if (streetViewPanoramaCamera.bearing >= 202.6 && streetViewPanoramaCamera.bearing <= 225) {
                    myMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.p9));
                } else if (streetViewPanoramaCamera.bearing >= 225.1 && streetViewPanoramaCamera.bearing <= 247.5) {
                    myMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.p10));
                } else if (streetViewPanoramaCamera.bearing >= 247.6 && streetViewPanoramaCamera.bearing <= 270) {
                    myMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.p11));
                } else if (streetViewPanoramaCamera.bearing >= 270.1 && streetViewPanoramaCamera.bearing <= 292.5) {
                    myMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.p12));
                } else if (streetViewPanoramaCamera.bearing >= 292.6 && streetViewPanoramaCamera.bearing <= 315) {
                    myMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.p13));

                } else if (streetViewPanoramaCamera.bearing >= 315.1 && streetViewPanoramaCamera.bearing <= 337.5) {
                    myMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.p14));
                } else if (streetViewPanoramaCamera.bearing >= 337.6 && streetViewPanoramaCamera.bearing <= 360) {
                    myMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.p15));
                }

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

        MarkerOptions a = new MarkerOptions().position(wsu).icon(BitmapDescriptorFactory.fromResource(R.drawable.p0));
        myMarker = mMap.addMarker(a);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(wsu));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(9));
        mMap.setMyLocationEnabled(true);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                sStreet.setPosition(latLng);


            }
        });

    }


}


//make some changes