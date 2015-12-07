package fr.upmfgrenoble.wicproject.pdr;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

/**
 * Created by Alizée on 07/12/2015.
 */
public class StepDetectionHandler {

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private Context context;

    public StepDetectionHandler(SensorManager mSensorManager) {
        super();
    }

    @Override
    public void start(){
        mSensorManager.registerListener(context, mSensor, mSensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void stop(){
        mSensorManager.unregisterListener(this);
    }
}
