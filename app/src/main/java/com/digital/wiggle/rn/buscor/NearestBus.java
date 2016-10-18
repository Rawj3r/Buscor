package com.digital.wiggle.rn.buscor;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.location.LocationListener;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class NearestBus extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMyLocationButtonClickListener, ActivityCompat.OnRequestPermissionsResultCallback, LocationListener {


    private GoogleMap mMap;
    private LatLng bus1 = new LatLng(-25.505306, 31.336984);
    private LatLng bus2 = new LatLng(-25.504431, 31.341467);
    private LatLng bus3 = new LatLng(-25.518645, 31.338867);
    private LatLng bus4 = new LatLng(-25.514878, 31.338740);
    private LatLng bus5 = new LatLng(-25.511265, 31.339617);
    private LatLng bus6 = new LatLng(-25.500960, 31.336201);
    private LatLng bus7 = new LatLng(-25.502826, 31.349362);

    LocationManager locationManager;

    private GoogleApiClient googleApiClient;


    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private Marker m1;
    private Marker m2;
    private Marker m3;
    private Marker m4;
    private Marker m5;
    private Marker m6;
    private Marker m7;

    private Location location;

    LatLng self;
    private Marker mSelf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearest_bus);

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    Thread t = new Thread() {
        @Override
        public void run() {
            while (true) {
                if (ActivityCompat.checkSelfPermission(NearestBus.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(NearestBus.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                }else {
                    locationManager.removeUpdates(NearestBus.this);
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }

    };





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
        m1 = mMap.addMarker(new MarkerOptions().position(bus1).title("NST-213").icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_icon)));
        m2 = mMap.addMarker(new MarkerOptions().position(bus2).title("NST-986"));
        m3 = mMap.addMarker(new MarkerOptions().position(bus3).title("NST-445"));
        m4 = mMap.addMarker(new MarkerOptions().position(bus4).title("NST-689"));
        m5 = mMap.addMarker(new MarkerOptions().position(bus5).title("NST-209"));
        m6 = mMap.addMarker(new MarkerOptions().position(bus6).title("NST-243"));
        m7 = mMap.addMarker(new MarkerOptions().position(bus7).title("NST-22"));

        mMap.setOnMyLocationButtonClickListener(this);

        enableMyLocation();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        } else {

            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();

            String bestProvider = locationManager.getBestProvider(criteria, true);
            Location location = locationManager.getLastKnownLocation(bestProvider);
            if (location != null) {
//                onLocationChanged(location);
            }
            locationManager.requestLocationUpdates(bestProvider, 15000, 0, this);

        }


    }

    /**
     * Called when the location has changed.
     * <p>
     * <p> There are no restrictions on the use of the supplied Location object.
     *
     * @param location The new location, as a Location object.
     */
    @Override
    public void onLocationChanged(Location location) {

//
//        double latitude = location.getLatitude();
//        double longitude = location.getLongitude();
//        self = new LatLng(latitude, longitude);
//        mSelf = mMap.addMarker(new MarkerOptions().position(self).title("You").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_person_pin_circle_black_24dp)));
//        Log.e("location changed", "true");
//        Log.e("latitude", latitude+"");
//        Log.e("longitude", longitude+"");



    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    /**
     * Called when the provider is enabled by the user.
     *
     * @param provider the name of the location provider associated with this
     *                 update.
     */
    @Override
    public void onProviderEnabled(String provider) {

    }

    /**
     * Called when the provider is disabled by the user. If requestLocationUpdates
     * is called on an already disabled provider, this method is called
     * immediately.
     *
     * @param provider the name of the location provider associated with this
     *                 update.
     */
    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(this, "is conneted", Toast.LENGTH_SHORT).show();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Please grant locations permissions", Toast.LENGTH_SHORT).show();

        } else {

            // TODO: Consider calling
            Toast.makeText(this, "permissions granted", Toast.LENGTH_SHORT).show();
            location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

            if (location != null) {
                Toast.makeText(this, "location is not null", Toast.LENGTH_SHORT).show();
                Log.e("Lattitude", location.getLatitude() + "");
                Log.e("longitude", location.getLongitude() + "");
                self = new LatLng(location.getLatitude(), location.getLongitude());
                mSelf = mMap.addMarker(new MarkerOptions().position(self).title("You").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_person_pin_circle_black_24dp)));

            } else {
                Toast.makeText(this, "location is null", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.

        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection suspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "connection failed", Toast.LENGTH_SHORT).show();
    }

    /**
     * Hook in to note that fragment state is no longer saved.
     */
    @Override
    public void onStateNotSaved() {
        super.onStateNotSaved();
    }

    /**
     * Dispatch onStart() to all fragments.  Ensure any created loaders are
     * now started.
     */
    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    /**
     * Dispatch onStop() to all fragments.  Ensure all loaders are stopped.
     */
    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }else{
            if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
                if (permissions.length == 1 &&
                        permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                } else {
                    // Permission was denied. Display an error message.
                }
            }

        }

        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }
}
