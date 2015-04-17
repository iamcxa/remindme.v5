package tw.geodoer.mGeoInfo.API;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Handler;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.prefs.PreferenceChangeEvent;

/**
 * Created by fud on 2015/4/14.
 */
public class CurrentLocation implements GPSCallback {

    private GPSManager gpsManager = null;
    private Double lat = -1d;
    private Double lng = -1d;
    private Context context;
    private Handler mHandle;
    private Thread mGps;
    private Thread mNetWork;
    private Thread mLastKnow;
    private Boolean isThreadRun=true;
    private int taskID =0;
    private int GPS_OUTTIME=1500;
    private int NET_OUTTIME=2000;
    private  String mUserSetting;

    public CurrentLocation(Context context){
        this.context=context;
        gpsManager = new GPSManager();

        mHandle = new Handler();

        mGps = new Thread(new Runnable() {
            @Override
            public void run() {
                if(isThreadRun)startNetWorkAndSetTimeOut(NET_OUTTIME);
            }
        });

        mNetWork = new Thread(new Runnable() {
            @Override
            public void run() {
                if(isThreadRun)getLastKnow();
            }
        });
    }

    private onDistanceListener mDis=null;

    public interface onDistanceListener {

        public void onGetLatLng(Double lat,Double lng);
    }


    public void setOnLocListener(onDistanceListener mDis){
        this.mDis=mDis;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mUserSetting = sharedPreferences.getString("PowerUsageOption","0");
        Log.e("PrUr",sharedPreferences.getString("PowerUsageOption","0"));
        if(mUserSetting.equals("1")){
            GPS_OUTTIME = 5000;
            NET_OUTTIME = 5000;
        }else if(mUserSetting.equals("0")){
            GPS_OUTTIME = 0;
            NET_OUTTIME = 5000;
        }else if(mUserSetting.equals("-1")){
            GPS_OUTTIME = 0;
            NET_OUTTIME = 0;
        }
        startGpsAndSetTimeOut(GPS_OUTTIME);
    }

    public void setOnLocListenerSetGps(String acc,onDistanceListener mDis){
        this.mDis=mDis;
        if(acc.equals("1")){
            GPS_OUTTIME = 5000;
            NET_OUTTIME = 5000;
        }else if(acc.equals("0")){
            GPS_OUTTIME = 0;
            NET_OUTTIME = 5000;
        }else if(acc.equals("-1")){
            GPS_OUTTIME = 0;
            NET_OUTTIME = 0;
        }
        startGpsAndSetTimeOut(GPS_OUTTIME);
    }

    public void startGpsAndSetTimeOut(int s){
        if(gpsManager!=null){
            gpsManager.stopListening();
            gpsManager.setGPSCallback(null);
        }

        isThreadRun=true;

        if(gpsManager.startGpsListening(context) && s!=0){
            gpsManager.setGPSCallback(this);
            mHandle.postDelayed(mGps,s);
            Log.e("PrUr","使用GPS");
        }else{
            startNetWorkAndSetTimeOut(NET_OUTTIME);
        }
    }

    public void startNetWorkAndSetTimeOut(int s){
        if(gpsManager!=null){
            gpsManager.stopListening();
            gpsManager.setGPSCallback(null);
        }

        isThreadRun=true;
        if(gpsManager.startNetWorkListening(context) && s!=0){
            gpsManager.setGPSCallback(this);
            mHandle.postDelayed(mNetWork,s);
            Log.e("PrUr","使用網路");
        }else{
            getLastKnow();
        }
    }

    public void getLastKnow(){

        isThreadRun=false;
        if(gpsManager.LastLocation()!=null){
            Log.e("PrUr","使用上次位置");
            mDis.onGetLatLng(gpsManager.LastLocation().getLatitude(), gpsManager.LastLocation().getLongitude());
        }else{
            mDis.onGetLatLng(-1d,-1d);
        }

        if(gpsManager!=null){
            gpsManager.stopListening();
            gpsManager.setGPSCallback(null);
        }
    }



    public void stopGps(){
        isThreadRun=false;
        if(gpsManager!=null){
            gpsManager.stopListening();
            gpsManager.setGPSCallback(null);
        }
    }

    public void onGPSUpdate(Location location) {
        if(gpsManager!=null){
            gpsManager.LastLocation().set(location);
        }
        stopGps();
//        Log.wtf("PrU",taskID+","+lat+","+lng+"計算完成 "+ DistanceCalculator.haversine(location.getLatitude(), location.getLongitude(), lat, lng));
//        mDis.onGetDistance(DistanceCalculator.haversine(location.getLatitude(), location.getLongitude(), lat, lng));
        Log.e("PrUr",location+"");

        mDis.onGetLatLng(location.getLatitude(),location.getLongitude());
    }


}
