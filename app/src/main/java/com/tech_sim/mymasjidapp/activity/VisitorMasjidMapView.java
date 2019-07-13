package com.tech_sim.mymasjidapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.adapter.MyRecyclerViewAdapter;
import com.tech_sim.mymasjidapp.model.Masjiidmodel;
import com.tech_sim.mymasjidapp.utils.Constants;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;

import java.util.ArrayList;

public class VisitorMasjidMapView extends FragmentActivity implements OnMapReadyCallback {

    private RecyclerView mas_recyclerview;
    private ArrayList<Masjiidmodel> masjiidmodels=new ArrayList<>();
    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_masjid_activity);
        masjiidmodels= PreferenceManager.visitorMasjidmodels;
        mas_recyclerview=(RecyclerView)findViewById(R.id.mos_recyclerview);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        MyRecyclerViewAdapter myRecyclerViewAdapter=new MyRecyclerViewAdapter(this,masjiidmodels);
        mas_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mas_recyclerview.setAdapter(myRecyclerViewAdapter);
        myRecyclerViewAdapter.setClickListener(new MyRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
            @Override
            public void onMasjidName(String name,String id,String city) {
                Constants.visitor_masjidname=name;
                PreferenceManager.setMasjidName(name);
                Constants.masjid_id=id;
                PreferenceManager.setMasjid(id);
                Constants.city=city;
                PreferenceManager.setCity(city);
                Intent intent=new Intent(VisitorMasjidMapView.this,Visitors.class);
                startActivity(intent);
                finish();

                PreferenceManager.visitorMasjidmodels=new ArrayList<>();
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        int count= PreferenceManager.visitorMasjidmodels.size();
        for(int i=0;i<count;i++) {
            float latitude =Float.parseFloat( PreferenceManager.visitorMasjidmodels.get(i).getLatitude());
            float longitude = Float.parseFloat( PreferenceManager.visitorMasjidmodels.get(i).getLongitude());
            String name= PreferenceManager.visitorMasjidmodels.get(i).getName();
            LatLng location = new LatLng(latitude, longitude);
            googleMap.addMarker(new MarkerOptions().position(location)
                    .title(name));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
            googleMap.animateCamera(CameraUpdateFactory.zoomIn());
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(location)      // Sets the center of the map to Mountain View
                    .zoom(10)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }


}
