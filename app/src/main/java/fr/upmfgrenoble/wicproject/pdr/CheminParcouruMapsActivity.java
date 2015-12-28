package fr.upmfgrenoble.wicproject.pdr;

import android.content.Context;
import android.hardware.SensorManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import fr.upmfgrenoble.wicproject.R;
import fr.upmfgrenoble.wicproject.pdr.StepDetectionHandler.StepDetectionListener;

public class CheminParcouruMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SensorManager sensorManager;
    private StepDetectionHandler stepDetectionHandler;
    private DeviceAttitudeHandler deviceAttitudeHandler;
    private final float TAILLEPAS = (float) 0.8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chemin_parcouru_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // déclaration du capteur
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepDetectionHandler = new StepDetectionHandler(sensorManager);
        deviceAttitudeHandler = new DeviceAttitudeHandler(sensorManager);
        stepDetectionHandler.setStepDetectionListener(stepListener);
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
        // On ajoute un marker avec les coordonnées de Grenoble puis on zoom et on centre la carte sur ce point
        LatLng grenoble = new LatLng(45.192742, 5.773653);
        mMap.addMarker(new MarkerOptions().position(grenoble));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(grenoble));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(10));
    }

    // On écoute si un pas est détecté
    private StepDetectionListener stepListener = new StepDetectionListener() {
        @Override
        public void onNewStepDetected() {
            // On récupère l'angle
            double bearing = deviceAttitudeHandler.getBearing();
        }
    };
}
