package tw.geodoer.mGeoInfo.API;

import android.content.Context;
import android.location.Location;
import android.os.Handler;

/**
 * Created by fud on 2015/4/14.
 */
public class CurrentLocation implements GPSCallback {

    private GPSManager gpsManager = null;
    private Double lat = -1d;
    private Double lng = -1d;
    private Context context;
    private Handler mHandle;
    private Thread t;
    private Boolean isThreadRun=true;

    public CurrentLocation(Context context){
        this.context=context;
        gpsManager = new GPSManager();
        gpsManager.startListening(context);
        gpsManager.setGPSCallback(this);

        mHandle = new Handler();
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                if(isThreadRun)mDis.onGetDistance((double)-1);
            }
        });
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
        isThreadRun=true;
        setTimeOut(5000);
    }

    public void setTimeOut(int s){
        mHandle.postDelayed(t,s);
    }

    public void stopGps(){
        isThreadRun=false;
        gpsManager.stopListening();
        gpsManager.setGPSCallback(null);
    }

    public void onGPSUpdate(Location location) {
        stopGps();
        mDis.onGetDistance(DistanceCalculator.haversine(location.getLatitude(), location.getLongitude(), lat, lng));
    }


}