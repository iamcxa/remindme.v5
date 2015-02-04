package tw.geodoer.mPriority.controller;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.location.LocationManager;

import java.util.Locale;

public class ActionSetLocationAlarm {
    final String BC_ACTION = "me.iamcxa.remindme.TaskReceiver";
    Context context;
    double latitude;
    double longitude;
    float radius;
    long taskID;

    public ActionSetLocationAlarm(Context context, double lat, double lon, float rad, int taskID) {
        // TODO Auto-generated constructor stub
        super();
        this.context = context;
        this.latitude = lat;
        this.longitude = lon;
        this.radius = rad;
        this.taskID = taskID;
    }

    // 設定通知提示
    public void SetIt() {
        // 產生實體Geocoder對象
        Geocoder gc = new Geocoder(context, Locale.getDefault());
        // 獲取LocationManager對象
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 讀取指定的查詢地址
        //String address=getString(R.string.address);


        try {

            // 定義用於完成接收趨近警告的Receiver

            Intent intent = new Intent(BC_ACTION);
            //intent.setAction(BC_ACTION);
            intent.putExtra("msg", "me.iamcxa.remindme.location");
            intent.putExtra("taskID", taskID);

            PendingIntent pengding = PendingIntent.getBroadcast(context, -1, intent, 0);
            // 添加趨近警告
            manager.addProximityAlert(latitude, longitude, radius, -1, pengding);


        } catch (Exception e) {

        }

    }

}



