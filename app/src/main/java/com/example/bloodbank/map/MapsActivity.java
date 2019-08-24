package com.example.bloodbank.map;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.bloodbank.Helper;
import com.example.bloodbank.R;
import com.example.bloodbank.adapter.CustomHashMap;
import com.example.bloodbank.profile.Profile;
import com.example.bloodbank.profile.ProfileActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback , GoogleMap.OnMarkerClickListener ,GoogleMap.OnInfoWindowClickListener{

    private GoogleMap mMap;
    DatabaseReference databaseReference;
    CustomHashMap<String, Marker> markerHashMap;

    Bitmap img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        markerHashMap=new CustomHashMap<>();
        img = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
        databaseReference= FirebaseDatabase.getInstance().getReference(Helper.USERS);

    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                double lat=dataSnapshot.child("latLon").child("lat").getValue(Double.class);
                double lon=dataSnapshot.child("latLon").child("lon").getValue(Double.class);
                String key=dataSnapshot.getKey();
                String bloodGroup=dataSnapshot.child("bloodGroup").getValue(String.class);
                String name=dataSnapshot.child("name").getValue(String.class);
                addNewMarker(key,bloodGroup+" Name: "+name +" Click to view Profile",name,lat,lon);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                double lat=dataSnapshot.child("latLon").child("lat").getValue(Double.class);
                double lon=dataSnapshot.child("latLon").child("lon").getValue(Double.class);
                String key=dataSnapshot.getKey();
                changeMarker(key,lat,lon);

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                removeMarker(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //TODO: don't know what to do
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Helper.showToast(getApplicationContext(), databaseError.getMessage());
            }
        });
    }

    public void addNewMarker(String key, String title,String name, double lat, double lon){

        Marker ms=mMap.addMarker(new MarkerOptions().position(new LatLng(
                lat,lon))
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(img)));
        ms.showInfoWindow();
        ms.setTag(key);
        markerHashMap.add(key,ms);

    }
    public void changeMarker(String key,double lat,double lon){
        Marker m=markerHashMap.getByKey(key);
        m.setPosition(new LatLng(lat,lon));

    }
    public void removeMarker(String key){
        Marker m=markerHashMap.getByKey(key);
        m.remove();
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

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.google_map_styling));

            if (!success) Log.e("MapsActivityRaw", "Style parsing failed.");
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivityRaw", "Can't find style.", e);
        }

        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(10, 20), 10.0f));

        double lat=getIntent().getDoubleExtra("lat",23.7079514);
        double lon=getIntent().getDoubleExtra("lon",90.4303302);
        float zoom=getIntent().getFloatExtra("zoom",12.0f);


        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), zoom));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        double lat=marker.getPosition().latitude;
        double lon=marker.getPosition().longitude;
        float zoom= 16.0f;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), zoom));
        marker.showInfoWindow();
        return true;
    }


    @Override
    public void onInfoWindowClick(final Marker marker) {

        String uid= (String) marker.getTag();
        Intent intent=new Intent(getApplicationContext(), ProfileActivity.class);
        intent.putExtra("uid",uid);
        startActivity(intent);

    }
}
