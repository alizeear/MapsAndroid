package fr.upmfgrenoble.wicproject.pdr;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import fr.upmfgrenoble.wicproject.Utils;

/**
 * Created by drouetr on 18/12/15.
 */
public class DeviceAttitudeHandler implements SensorEventListener {
    private Sensor sensor;
    private SensorManager sensorManager;
    private double bearing;

    public DeviceAttitudeHandler(SensorManager sm) {
        this.sensorManager = sm;
        this.sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    }

    public void start(){
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void stop(){
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR){
            float[] rotMat = new float[9];
            float[] values = new float[3];

            SensorManager.getRotationMatrixFromVector(rotMat, event.values);
            double  yaw = SensorManager.getOrientation(rotMat,values)[0];
            bearing = yaw;
            deviceAttitudeListener.rotationChanged(bearing);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public double getBearing() {
        return bearing;
    }

    private DeviceAttitudeListener deviceAttitudeListener;

    public void setDeviceAttitudeListener(DeviceAttitudeListener deviceAttitudeListener) {
        this.deviceAttitudeListener = deviceAttitudeListener;
    }

    public interface DeviceAttitudeListener{
        public void rotationChanged(double angle);
    }
}
