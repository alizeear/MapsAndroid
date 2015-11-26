package fr.upmfgrenoble.wicproject.ui;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import fr.upmfgrenoble.wicproject.R;
import fr.upmfgrenoble.wicproject.model.GPX;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private  GPX gpx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapss);
        gpx = new GPX();

        try {
            InputStream is = getAssets().open("run.gpx");
            gpx = GPX.parse(is);
        }catch (IOException e){
            e.printStackTrace();
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

        // Add a marker in Sydney and move the camera
        LatLng grenoble = new LatLng(45.1842207, 5.6805231);

        mMap.addMarker(new MarkerOptions().position(grenoble).title("Marker in grenoble"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(grenoble));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(gpx.getLatLngBounds(), 2));

        showTrack();

    }

    /* on charge la structure gpx pour l'afficher sur la carte */
    public void showTrack(){

        for(GPX.Track t:gpx){

            Polyline line = mMap.addPolyline(new PolylineOptions());
            List<LatLng> latLngList = new ArrayList<LatLng>();

            for(GPX.TrackSegment ts: t){
                for(GPX.TrackPoint tp: ts){
                    latLngList.add(tp.position);
                }
            }

            line.setPoints(latLngList);
            line.setWidth(5);
            //line.setColor(Color.rgb((int)Math.round(Math.random() * 255), (int)Math.round(Math.random() * 255), (int)Math.round(Math.random() * 255)));
            line.setColor(Color.RED);
        }


    }
}

