package tw.geodoer.mPriority.service;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import tw.geodoer.utils.MyDebug;

/**
 * @Murakumo
 */
public class GeoServiceStarter extends Service {
    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }


    @Override
    public void onCreate()
    {
        super.onCreate();
        MyDebug.MakeLog(2,"@ Service Register start");

        //testing start command block
//        final String BC_ACTION = "me.iamcxa.remindme.TaskReceiver";
//        Intent intent = new Intent(this,GeoBroadcastReceiver_TaskAlert.class);
//        // 設定Intent action屬性
//        intent.setAction(BC_ACTION);
//        intent.putExtra("msg", "me.iamcxa.remindme.position");
//        sendBroadcast(intent);


        Stopself();
    }

    private void Stopself() {
        this.stopSelf();
    }

    @Override
    public void onLowMemory() {
        // TODO Auto-generated method stub
        super.onLowMemory();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        return super.onStartCommand(intent, flags, startId);
    }


}
