package fr.upmfgrenoble.wicproject.viewer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import fr.upmfgrenoble.wicproject.R;
import fr.upmfgrenoble.wicproject.Utils;
import fr.upmfgrenoble.wicproject.model.GPX;
import fr.upmfgrenoble.wicproject.pdr.DeviceAttitudeHandler;
import fr.upmfgrenoble.wicproject.ui.AppActivity;

/**
 * Created by drouetr on 18/12/15.
 */
public class GoogleMapTracer {
    private GoogleMap mGoogleMap;
    private ArrayList<Polyline> track;
    private ArrayList<LatLng> bufferPolyline;
    private Marker markerSimple;

    public GoogleMapTracer(GoogleMap mGoogleMap, DeviceAttitudeHandler deviceAttitudeHandler) {
        this.mGoogleMap = mGoogleMap;
        track = new ArrayList<>();
        bufferPolyline = new ArrayList<>();
        deviceAttitudeHandler.setDeviceAttitudeListener(deviceAttitudeListener);
    }

    public void endSegment(){
        if(!bufferPolyline.isEmpty()){
            if(markerSimple != null) {
                markerSimple.remove();
                markerSimple = null;
            }
            mGoogleMap.addMarker(new MarkerOptions().position(bufferPolyline.get(bufferPolyline.size()-1)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.trace_end)));
        }
    }

    public void newSegment(){
        bufferPolyline.clear();
        track.add(mGoogleMap.addPolyline(new PolylineOptions()));
    }

    public void newPoint(LatLng point){
        if(markerSimple != null) {
            markerSimple.remove();
            markerSimple = null;
        }
        if(bufferPolyline.isEmpty()){
            mGoogleMap.addMarker(new MarkerOptions().position(point).icon(BitmapDescriptorFactory.fromResource(R.mipmap.trace_start)));
        }else{
            markerSimple = mGoogleMap.addMarker(new MarkerOptions().position(point).icon(BitmapDescriptorFactory.fromResource(R.mipmap.arrow)));
        }

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(point));

        bufferPolyline.add(point);
        track.get(track.size()-1).setPoints(bufferPolyline);
    }

    private DeviceAttitudeHandler.DeviceAttitudeListener deviceAttitudeListener = new DeviceAttitudeHandler.DeviceAttitudeListener() {
        @Override
        public void rotationChanged(double angle) {
            if(markerSimple != null) {
                markerSimple.setRotation((float)Math.toDegrees(angle));
            }
        }
    };

    public static String GPX = "gpx";
    public static String TRK = "trk";
    public static String TRKSEG = "trkseg";
    public static String TRKPT = "trkpt";
    public static String LON = "lon";
    public static String LAT = "lat";
    public void exportToXML(String fileName){
        XmlSerializer xmlSerializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();

        try{
            xmlSerializer.setOutput(writer);
            // start DOCUMENT
            xmlSerializer.startDocument("UTF-8", true);
            // open tag: <GPX>
            xmlSerializer.startTag("", GPX);
            xmlSerializer.attribute("", "version", "1.1");
            xmlSerializer.attribute("", "xmlns", "http://www.topografix.com/GPX/1/1");
            xmlSerializer.attribute("", "xsi:schemaLocation", "http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd");
            // open tag: <TRK>
            xmlSerializer.startTag("", TRK);
            for (Polyline segment : track) {
                xmlSerializer.startTag("",TRKSEG);
                for (LatLng point : segment.getPoints()){
                    xmlSerializer.startTag("", TRKPT);
                    xmlSerializer.attribute("", LON, String.valueOf(point.longitude));
                    xmlSerializer.attribute("", LAT, String.valueOf(point.latitude));
                    xmlSerializer.endTag("",TRKPT);
                }
                xmlSerializer.endTag("",TRKSEG);
            }
            xmlSerializer.endTag("", TRK);
            xmlSerializer.endTag("", GPX);
            xmlSerializer.endDocument();
            File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),fileName);
            if(!f.exists()){
                f.createNewFile();
            }
            FileWriter fw = new FileWriter(f);
            fw.write(writer.toString());
            fw.close();
        }catch (IOException io){
            System.err.println(io.getMessage());
        }
    }
}
