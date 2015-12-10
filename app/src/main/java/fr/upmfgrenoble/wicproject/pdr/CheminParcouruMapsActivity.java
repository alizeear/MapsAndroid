package fr.upmfgrenoble.wicproject.pdr;

import android.content.Context;
import android.hardware.Sensor;
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
    private Sensor sensor;
    private StepDetectionHandler stepDetectionHandler;
    private int compteurPas = 0;
    private TextView compteurView;

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
        stepDetectionHandler.setStepDetectionListener(stepListener);
        compteurView = (TextView) findViewById(R.id.compteur);
        compteurView.setText(compteurPas+"");
        Button reset = (Button) findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compteurPas=0;
                compteurView.setText(compteurPas+"");
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        stepDetectionHandler.start();
    }
    @Override
    protected void onPause() {
        super.onPause();
        stepDetectionHandler.stop();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


    private StepDetectionListener stepListener = new StepDetectionListener() {
        @Override
        public void onNewStepDetected() {
            compteurPas++;
            Log.d(Utils.LOG_TAG,compteurPas+"");
            compteurView.setText(compteurPas+"");
        }
    };
}
