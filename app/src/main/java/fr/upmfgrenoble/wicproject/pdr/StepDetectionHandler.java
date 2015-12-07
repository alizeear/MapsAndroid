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

    public StepDetectionHandler(SensorManager sensorManager) {

    }

    public void start(){
        sensorManager.registerListener(stepDetectionHandler, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stop(){

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
