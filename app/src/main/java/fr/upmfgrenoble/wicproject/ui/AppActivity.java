package fr.upmfgrenoble.wicproject.ui;

import android.content.Context;
import android.hardware.SensorManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import fr.upmfgrenoble.wicproject.R;
import fr.upmfgrenoble.wicproject.Utils;
import fr.upmfgrenoble.wicproject.pdr.DeviceAttitudeHandler;
import fr.upmfgrenoble.wicproject.pdr.StepDetectionHandler;
import fr.upmfgrenoble.wicproject.pdr.StepPositioningHandler;
import fr.upmfgrenoble.wicproject.viewer.GoogleMapTracer;


public class AppActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMapTracer mGoogleMapTracer;
    private GoogleMap mMap;
    private StepPositioningHandler mStepPositioningHandler;
    protected GoogleMap.OnMapClickListener mOnMapClickListener;
    private SensorManager sensorManager;
    private StepDetectionHandler stepDetectionHandler;
    private DeviceAttitudeHandler deviceAttitudeHandler;
    private final float TAILLEPAS = (float) 0.4;
    private boolean tracing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
        deviceAttitudeHandler.stop();
    }

    private StepDetectionHandler.StepDetectionListener stepListener = new StepDetectionHandler.StepDetectionListener() {
        @Override
        public void onNewStepDetected() {
            double bearing = deviceAttitudeHandler.getBearing();
            LatLng newPos = mStepPositioningHandler.computeNextStep(TAILLEPAS, (float) bearing);
            if(mMap != null && tracing)
                mGoogleMapTracer.newPoint(newPos);
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
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        LatLng maison = new LatLng(45.594757, 5.872533);

        mGoogleMapTracer = new GoogleMapTracer(mMap);
        mStepPositioningHandler = new StepPositioningHandler(maison);
        tracing = true;
        mGoogleMapTracer.newSegment();
        mGoogleMapTracer.newPoint(mStepPositioningHandler.getmCurrentPosition());

        mOnMapClickListener = new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(tracing){
                    tracing = false;
                }else{
                    tracing = true;
                    mGoogleMapTracer.newSegment();
                    mGoogleMapTracer.newPoint(mStepPositioningHandler.getmCurrentPosition());
                }
            }
        };
        mMap.setOnMapClickListener(mOnMapClickListener);
    }
}
