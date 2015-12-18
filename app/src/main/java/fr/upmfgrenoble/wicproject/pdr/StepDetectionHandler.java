package fr.upmfgrenoble.wicproject.pdr;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import fr.upmfgrenoble.wicproject.R;
import fr.upmfgrenoble.wicproject.Utils;

public class StepDetectionHandler implements SensorEventListener{
    private Sensor sensor;
    private SensorManager sensorManager;
    private boolean overSeuil = false;
    private float seuil = (float) 3;
    private int indexPas=0;
    private ArrayList<Float> pas = new ArrayList<>();

    public StepDetectionHandler(SensorManager sm) {
        sensorManager = sm;
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
    }

    public void start(){
        Log.d(Utils.LOG_TAG,"StepDetection started ...");
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void stop(){
        Log.d(Utils.LOG_TAG,"StepDetection STOP !");
        sensorManager.unregisterListener(this);
    }

    @Override
    /* On détecte un nouveau pas */
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION){
            pas.add(event.values[2]);
            if(pas.size()>5){
                pas.remove(0);
            }
            float moy = 0;
            for (float val: pas) {
                moy+=val;
            }
            moy /=pas.size();

            if(overSeuil) {
                if(moy<seuil)
                    overSeuil = false;
            }else {
                if (moy >= seuil) {
                    stepDetectionListener.onNewStepDetected();
                    overSeuil = true;
                }
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private StepDetectionListener stepDetectionListener;

    public void setStepDetectionListener(StepDetectionListener listener){
        stepDetectionListener = listener;
    }

    public interface StepDetectionListener{
        public void onNewStepDetected();
    }
}
