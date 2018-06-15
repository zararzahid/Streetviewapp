package com.example.zarar.streetview;

import android.Manifest;
import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;
import com.google.android.gms.maps.model.StreetViewPanoramaLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



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

// main class
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, OnStreetViewPanoramaReadyCallback,
        GoogleApiClient.OnConnectionFailedListener {
    //our google api key, this is a verification for google
    private static final String API_KEY = "AIzaSyCb-5Zvpe91N-yhfR1A_s0hP14Wh3GwdDw";

    private static final String TAG = "MapsActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));


    //vars

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleMap mMap;
    private StreetViewPanorama sStreet;
    private Marker myMarker;
    private GoogleApiClient mGoogleApiClient;
    private GeoDataClient mGeoDataClient;

    //widgets
    private AutoCompleteTextView mSearchText;
    //static latitude longitudes of bookmark images
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

    // this is for our autocomplete adapter checking for conneciton to google api client
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    // this is our oncreate function, it runs everything below as the app is created
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //connects java msearchtext to xml inout search
        mSearchText = findViewById(R.id.input_search);

        // google places google api client, finds location data for autofilling search bar with google places
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
        //adapter for our google places api
        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient,
                LAT_LNG_BOUNDS, null);
        //set the adapter on mSearchText
        mSearchText.setAdapter(mPlaceAutocompleteAdapter);

        //this sets an onitemclick listener to mSearchText
        //when someone clicks on an item in the drop down list automatically created by places auto complete
        //we call geoLocate(); which can be found on line 253

        mSearchText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                geoLocate();
            }
        });

        //similar to on click listener, instead listens for when someone clicks enter on keyboard
        // or clicks the search button
        //calls geolocate which can be found on line 253
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction() == KeyEvent.KEYCODE_ENTER) {
                    geoLocate();
                }


                return false;
            }
        });


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //a click listener on the button that changes map type
        //also changes what the button message is
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


        //  method for on click on images and updating the map

        //sStreet is our street view object.

        //setPosition is a function within Street View and it takes coordinates and
        //moves us to those coordinates.
        //since it is streetview it uses the coordinates to find a panorama id
        //this moves street view to the right panorama

        ImageView img1 = findViewById(R.id.opera);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sStreet.setPosition(opera);

            }
        });
        //on click listener, listens for click on an image and takes you to coordinates of paris
        // these coordinates are set at the top of the file
        ImageView img2 = findViewById(R.id.paris);
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sStreet.setPosition(paris);


            }
        });
        //this is the same as the other images
        ImageView img3 = findViewById(R.id.venice);
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sStreet.setPosition(venice);

            }
        });
        //same as other images
        ImageView img4 = findViewById(R.id.surf);
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sStreet.setPosition(surfworld);

            }
        });
        //same as other images
        ImageView img5 = findViewById(R.id.park);
        img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sStreet.setPosition(park);

            }
        });
        //same as other images
        ImageView img6 = findViewById(R.id.pitt);
        img6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sStreet.setPosition(pittstreet);

            }
        });
    }

    //geolocate takes a search string and finds a place that matches the string and takes us there
    //this function also gives us the coordinates of our location and finds the closest panorama id
    //then sets our street view to that panorama
    private void geoLocate() {
        String searchString = mSearchText.getText().toString();

        Geocoder geo = new Geocoder(MapsActivity.this);

        List<Address> list = new ArrayList<>();

        try {
            list = geo.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.e(TAG, "geo: IOE" + e.getMessage());
        }

        if (list.size() > 0) {
            Address address = list.get(0);

            Log.d(TAG, "geoLocate: found a location" + address.toString());
            sStreet.setPosition(new LatLng(address.getLatitude(), address.getLongitude()));
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();
            mMap.moveCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM));
        }


    }


    //changes to the map on changing the panormama

    //this is called when the app starts
    //sets panorama start position
    //sets a default map zoom
    //then we have a function that listens for panorama changes to the street view
    //so when the street view changes, we make changes to the map
    //we make coordinate changes here
    //these are only changes to the panorama id and not the camera, that is below


    @Override
    public void onStreetViewPanoramaReady(final StreetViewPanorama panorama) {
        sStreet = panorama;

        panorama.setPosition(opera);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM));

        sStreet.setOnStreetViewPanoramaChangeListener(new StreetViewPanorama.OnStreetViewPanoramaChangeListener() {
            @Override
            public void onStreetViewPanoramaChange(StreetViewPanoramaLocation streetViewPanoramaLocation) {


                StreetViewPanoramaLocation location = sStreet.getLocation();

                LatLng pos = location.position;

                mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));


                myMarker.setPosition(pos);


            }
        });

        //this is a panorama camera change listener that updates whenever the camera is moved not the panorama id
        //we update the pegman icon on the map as we change bearing on the panorama

        //Adding pegman to the map and its facing directions
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

    // Adding the pegman marker in map and updating map zoom options
    public void onMapReady(final GoogleMap googleMap) {

        mMap = googleMap;

        // Add a marker in Sydney and move the camera

        MarkerOptions a = new MarkerOptions().position(wsu).icon(BitmapDescriptorFactory.fromResource(R.drawable.p0));
        myMarker = mMap.addMarker(a);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(wsu));

        mMap.setMyLocationEnabled(true);

        //this listens for clicks on the map
        //we then move the map and center it on wherever is clicked

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {


                float zoom = mMap.getCameraPosition().zoom;

                //this changes the panorama search radius depending on map zoom level.
                //if you are at country zoom the radius is much larger
                //if you are at street zoom it is much smaller and accurate

                if (zoom >= 0 && zoom <= 5) {
                    sStreet.setPosition(latLng, 500000);
                } else if (zoom >= 5.1 && zoom <= 10) {
                    sStreet.setPosition(latLng, 5000);
                } else if (zoom >= 10.1 && zoom <= 13) {
                    sStreet.setPosition(latLng, 500);
                } else if (zoom >= 13.1 && zoom <= 20) {
                    sStreet.setPosition(latLng, 50);
                }



            }
        });

    }


}


