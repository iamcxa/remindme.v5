package tw.geodoer.mPriority.service;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

import tw.geodoer.mPriority.eventReceiver.GeoBroadcastReceiver;
import tw.geodoer.mPriority.eventReceiver.GeoBroadcastReceiver_TaskAlert;
import tw.geodoer.main.taskList.view.AppMainActivity;
import tw.geodoer.utils.MyDebug;
import tw.moretion.geodoer.R;


/**
 * @Murakumo
 */
public class GeoServiceRegister extends Service {
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
    public void onCreate() {
        super.onCreate();


        //testing start command block

        final String BC_ACTION = "me.iamcxa.remindme.TaskReceiver";
        Intent intent = new Intent(this,GeoBroadcastReceiver_TaskAlert.class);
        // 設定Intent action屬性
        intent.setAction(BC_ACTION);
        intent.putExtra("msg", "me.iamcxa.remindme.position");
        sendBroadcast(intent);



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
