package fr.upmfgrenoble.wicproject.pdr;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

import fr.upmfgrenoble.wicproject.R;
import fr.upmfgrenoble.wicproject.Utils;

/**
 * Created by Alizée on 07/12/2015.
 */
public class StepDetectionHandler implements SensorEventListener{
    private Sensor sensor;
    private SensorManager sensorManager;

    public StepDetectionHandler(SensorManager sm) {
        sensorManager = sm;
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
    }

    public void start(){
        Log.d(Utils.LOG_TAG,"###########################################");
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stop(){
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d(Utils.LOG_TAG,event.values[2]+"");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
