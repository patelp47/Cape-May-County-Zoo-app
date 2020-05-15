package com.example.capemaycountyzoomap;
import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            }
        } else {
            // Permission has already been granted
        }

        setContentView(R.layout.activity_maps);
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
        //NOTE: google maps api key is required to access the api, a key i generated is loaded into the
        //google_maps_api.xml file in the app/res/values directory

        mMap = googleMap;

        LatLng zoo = new LatLng(39.103, -74.816); //lat and lng of cape may county zoo
        LatLng pic = new LatLng(39.101451, -74.815200); //lat and lng for the picture overlay
        LatLng backing = new LatLng(39.101954,-74.8152057); //lat and lng for the background overlay

        mMap.moveCamera(CameraUpdateFactory.newLatLng(zoo));  //moves camera to the coordinates of zoo
        mMap.getUiSettings().setRotateGesturesEnabled(false);   //prevents user from rotating with a gesture
        mMap.setMinZoomPreference(16.7f);  //minimum zoom level so user cant see outside zoo boundaries
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.map_style));
        } catch (Resources.NotFoundException e) {
        }

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            mMap.setMyLocationEnabled(true);
        } catch (SecurityException e) { }

        GroundOverlayOptions backgroundGoo = new GroundOverlayOptions()      //used for overlaying the image
                .image(BitmapDescriptorFactory.fromResource(R.drawable.backdrop))        //calls the bitmap image bmp
                .position(backing, 1600f, 1600f)       //Sets the size of the image to be displayed
                .bearing(270);
                //.transparency(0.7f);
        mMap.addGroundOverlay(backgroundGoo);     //ads the overlay to the map

        GroundOverlayOptions gOO = new GroundOverlayOptions()      //used for overlaying the image
            .image(BitmapDescriptorFactory.fromResource(R.drawable.zoomap50))        //calls the bitmap image bmp
            .position(pic, 700f, 425f)       //Sets the size of the image to be displayed
            .bearing(270);
            //.transparency(0.7f);
        mMap.addGroundOverlay(gOO);
        //ads the overlay to the map

        // Create a LatLngBounds that locks the user from moving the map outside of the zoo
        //using coordinates from 2 corners (the southwest and northeast corners of the zoo)
        LatLngBounds ZOO = new LatLngBounds(
                new LatLng(39.098432, -74.817210),new LatLng(39.104449, -74.813310));
        mMap.setLatLngBoundsForCameraTarget(ZOO);

        //Changes the camera bearing to be in line with the overlay
        final CameraPosition position =
                new CameraPosition.Builder().target(zoo)
                   .bearing(270)
                        .build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));


        //sets default zoom level, the higher the number the closer the zoom
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(zoo, 17));


    }
}
