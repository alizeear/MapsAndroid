package fr.upmfgrenoble.wicproject.viewer;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import fr.upmfgrenoble.wicproject.model.GPX;

/**
 * Created by drouetr on 18/12/15.
 */
public class GoogleMapTracer {
    private GoogleMap mGoogleMap;
    private ArrayList<Polyline> track;
    private ArrayList<LatLng> bufferPolyline;

    public GoogleMapTracer(GoogleMap mGoogleMap) {
        this.mGoogleMap = mGoogleMap;
        track = new ArrayList<>();
        bufferPolyline = new ArrayList<>();
    }

    public void newSegment(){
        bufferPolyline.clear();
        track.add(mGoogleMap.addPolyline(new PolylineOptions()));
    }

    public void newPoint(LatLng point){
        bufferPolyline.add(point);
        track.get(track.size()-1).setPoints(bufferPolyline);
    }
}
