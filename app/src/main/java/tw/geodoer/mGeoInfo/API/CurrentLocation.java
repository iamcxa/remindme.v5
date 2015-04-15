package tw.geodoer.mGeoInfo.API;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.util.Log;

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
    private int taskID =0;

    public CurrentLocation(Context context){
        this.context=context;
        gpsManager = new GPSManager();
        gpsManager.startNetWorkListening(context);
        gpsManager.setGPSCallback(this);

        mHandle = new Handler();
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                if(isThreadRun)mDis.onGetLatLng((double)-1,(double)-1);
            }
        });
    }

    private onDistanceListener mDis=null;

    public interface onDistanceListener {

//        public void onGetDistance(Double mDistance);

        public void onGetLatLng(Double lat,Double lng);
    }

//    public void setOnDistanceListener(int taskID,Double lat,Double lng,onDistanceListener mDis){
//        this.mDis=mDis;
//        this.lat=lat;
//        this.lng=lng;
//        this.taskID=taskID;
//        gpsManager.startNetWorkListening(context);
//        gpsManager.setGPSCallback(this);
//        isThreadRun=true;
//        Log.wtf("PrU",taskID+"  "+lat +","+ lng+" 開始計算");
//        setTimeOut(10000);
//    }


    public void setOnLocListener(onDistanceListener mDis){
        this.mDis=mDis;
        gpsManager.startNetWorkListening(context);
        gpsManager.setGPSCallback(this);
        isThreadRun=true;
        Log.wtf("PrU",taskID+"  "+lat +","+ lng+" 開始計算");
        setTimeOut(500000);

        //寫死正修測試用
        mDis.onGetLatLng(22.650351,120.350032);
        stopGps();
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
        Log.wtf("PrU",taskID+","+lat+","+lng+"計算完成 "+ DistanceCalculator.haversine(location.getLatitude(), location.getLongitude(), lat, lng));
//        mDis.onGetDistance(DistanceCalculator.haversine(location.getLatitude(), location.getLongitude(), lat, lng));
        mDis.onGetLatLng(location.getLatitude(),location.getLongitude());
    }


}
