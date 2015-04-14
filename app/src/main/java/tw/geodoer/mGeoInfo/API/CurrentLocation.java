package tw.geodoer.mGeoInfo.API;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by fud on 2015/4/14.
 */
public class CurrentLocation implements GPSCallback {

    private GPSManager gpsManager = null;
    private Double lat = -1d;
    private Double lng = -1d;
    private Context context;

    public CurrentLocation(Context context){
        this.context=context;
        gpsManager = new GPSManager();
        gpsManager.startListening(context);
        gpsManager.setGPSCallback(this);
    }

    private onDistanceListener mDis=null;

    public interface onDistanceListener {

        public void onGetDistance(Double mDistance);

    }

    public void setOnDistanceListener(Double lat,Double lng,onDistanceListener mDis){
        this.mDis=mDis;
        this.lat=lat;
        this.lng=lng;
        gpsManager.startListening(context);
        gpsManager.setGPSCallback(this);
    }

    public void stopGps(){
        gpsManager.stopListening();
    }

    public void onGPSUpdate(Location location) {
        mDis.onGetDistance(DistanceCalculator.haversine(location.getLatitude(),location.getLongitude(),lat,lng));
        gpsManager.stopListening();
        gpsManager.setGPSCallback(null);
    }


}
