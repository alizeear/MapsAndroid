package fr.upmfgrenoble.wicproject.viewer;

import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import fr.upmfgrenoble.wicproject.Utils;
import fr.upmfgrenoble.wicproject.model.GPX;

/**
 * Created by drouetr on 18/12/15.
 */
public class GoogleMapTracer {
    private GoogleMap mGoogleMap;
    private ArrayList<Polyline> track;
    private ArrayList<LatLng> bufferPolyline;
    Marker markerSimple;

    public GoogleMapTracer(GoogleMap mGoogleMap) {
        this.mGoogleMap = mGoogleMap;
        track = new ArrayList<>();
        bufferPolyline = new ArrayList<>();
    }

    public void newSegment(){
        if(!bufferPolyline.isEmpty()){
            mGoogleMap.addMarker(new MarkerOptions().position(bufferPolyline.get(bufferPolyline.size()-1)).title("Marker"));
        }
        bufferPolyline.clear();
        track.add(mGoogleMap.addPolyline(new PolylineOptions()));
    }

    public void newPoint(LatLng point){
        if(markerSimple != null) {
            markerSimple.remove();
            markerSimple = null;
        }
        if(bufferPolyline.isEmpty()){
            mGoogleMap.addMarker(new MarkerOptions().position(point).title("Marker").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        }else{
            markerSimple = mGoogleMap.addMarker(new MarkerOptions().position(point).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        }

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(point));

        bufferPolyline.add(point);
        track.get(track.size()-1).setPoints(bufferPolyline);
    }
}
