package fr.upmfgrenoble.wicproject.pdr;

import android.content.Context;
import android.hardware.SensorManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import fr.upmfgrenoble.wicproject.R;
import fr.upmfgrenoble.wicproject.Utils;
import fr.upmfgrenoble.wicproject.pdr.StepDetectionHandler.StepDetectionListener;

public class CheminParcouruMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SensorManager sensorManager;
    private StepDetectionHandler stepDetectionHandler;
    private DeviceAttitudeHandler deviceAttitudeHandler;
    private final float TAILLEPAS = (float) 0.8;

    public CheminParcouruMapsActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chemin_parcouru_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepDetectionHandler = new StepDetectionHandler(sensorManager);
        deviceAttitudeHandler = new DeviceAttitudeHandler(sensorManager);
        stepDetectionHandler.setStepDetectionListener(stepListener);
        Button reset = (Button) findViewById(R.id.reset);
    }
    @Override
    protected void onResume() {
        super.onResume();
        stepDetectionHandler.start();
        deviceAttitudeHandler.start();
    }
    @Override
    protected void onPause() {
        super.onPause();
        stepDetectionHandler.stop();
        deviceAttitudeHandler.start();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(45.192742, 5.773653);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Nous"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(10));
    }


    private StepDetectionListener stepListener = new StepDetectionListener() {
        @Override
        public void onNewStepDetected() {
            double bearing = deviceAttitudeHandler.getBearing();
        }
    };
}
