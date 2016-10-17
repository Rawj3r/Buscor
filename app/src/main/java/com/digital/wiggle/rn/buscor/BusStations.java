package com.digital.wiggle.rn.buscor;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class BusStations extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private static final LatLng paradise = new LatLng(-25.505306, 31.336984);
    private static final LatLng sbusisiwe = new LatLng(-25.504431, 31.341467);
    private static final LatLng zcc = new LatLng(-25.518645, 31.338867);
    private static final LatLng emagezini = new LatLng(-25.514878, 31.338740);
    private static final LatLng station1 = new LatLng(-25.511265, 31.339617);
    private static final LatLng bizzicona = new LatLng(-25.500960, 31.336201);
    private static final LatLng funindlela = new LatLng(-25.502826, 31.349362);

    private GoogleApiClient googleApiClient;

    private Marker mParadise;
    private Marker mSbusisiwe;
    private Marker mZcc;
    private Marker mEmagezini;
    private Marker mStation1;
    private Marker mFunindlela;
    private Marker mBizzicona;

    private Location location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_stations);

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

        mBizzicona = mMap.addMarker(new MarkerOptions()
                .position(bizzicona)
                .title("BizziCona bus station")
        );
        mBizzicona.setTag(0);

        mEmagezini = mMap.addMarker(new MarkerOptions()
                .position(emagezini)
                .title("Emagezini bus station")
        );
        mEmagezini.setTag(0);
        mFunindlela = mMap.addMarker(new MarkerOptions().position(funindlela).title("Funindlela bus station"));
        mFunindlela.setTag(0);
        mParadise = mMap.addMarker(new MarkerOptions().position(paradise).title("Paradise Bus station"));
        mSbusisiwe = mMap.addMarker(new MarkerOptions().position(sbusisiwe).title("Sbusisiwe Bus station"));
        mStation1 = mMap.addMarker(new MarkerOptions().position(sbusisiwe).title("Bus Station"));
        mZcc = mMap.addMarker(new MarkerOptions().position(zcc).title("ZCC "));

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(funindlela));
        mMap.setMinZoomPreference(14.0f);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(this, "is conneted", Toast.LENGTH_SHORT).show();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            Toast.makeText(this, "permissions granted", Toast.LENGTH_SHORT).show();
            location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (location != null){
                Toast.makeText(this, "location is not null", Toast.LENGTH_SHORT).show();
                Log.e("Lattitude", location.getLatitude()+"");
                Log.e("longitude", location.getLongitude()+"");
            }
            else{
                Toast.makeText(this, "location is null", Toast.LENGTH_SHORT).show();
            }
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection suspended", Toast.LENGTH_SHORT).show();
    }

    /**
     * Dispatch onStop() to all fragments.  Ensure all loaders are stopped.
     */
    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "connection failed", Toast.LENGTH_SHORT).show();
    }
}
