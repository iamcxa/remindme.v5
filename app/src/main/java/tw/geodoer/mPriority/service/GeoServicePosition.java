package tw.geodoer.mPriority.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import tw.geodoer.mDatabase.API.DBLocationHelper;
import tw.geodoer.mDatabase.columns.ColumnLocation;
import tw.geodoer.mGeoInfo.API.DistanceCalculator;
import tw.geodoer.mPriority.eventReceiver.GeoBroadcastReceiver;
import tw.geodoer.utils.MyDebug;

/**
 * Created by MurasakiYoru on 2015/1/23.
 */
public class GeoServicePosition extends Service {
    private double Last_Lat, Last_Lon;
    private double Now_Lat, Now_Lon;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MyDebug.MakeLog(2, "Position Service Start");

        boolean check;
        check = this.GetlastPos();
        if (check) {
            check = this.position_updater();
            MyDebug.MakeLog(2, "position_updater success");
        }
        if (check) {
            check = this.distance_updater();
            MyDebug.MakeLog(2, "distance_updater success");
        }
        if (check) {
            MyDebug.MakeLog(2, "call out Service Weight");
            Intent it = new Intent("tw.geodoer.mPriority.service.RemainBroadcast");
            it.putExtra("Command", GeoBroadcastReceiver.BROADCAST_COMMAND_WEIGHT);
            sendBroadcast(it);
        }

        stopSelf();
        return flags;
    }

    public boolean GetlastPos() {
        //Load the last position from database
        this.Last_Lat = 22.0;
        this.Last_Lon = 120.0;
        return true;
    }

    public boolean position_updater() {
        //double NData[] = {22.0 ,23.0 ,24.0};
        //double WData[] = {120.0,121.0,122.0};
        //Random ran = new Random();
        //this.Now_Lat = NData[ran.nextInt()%NData.length];
        //this.Now_Lon = WData[ran.nextInt()%WData.length];
        this.Now_Lat = 23.0;
        this.Now_Lon = 121.0;
        // true = update success  / false = at the same position
        if (this.Last_Lat == this.Now_Lat && this.Last_Lon == this.Now_Lon) {
            MyDebug.MakeLog(2, "get the same position");
            return false;
        } else {
            //set new position in last position in database
            MyDebug.MakeLog(2, "get not the same position");
            return true;
        }
    }

    public boolean distance_updater() {
        DBLocationHelper dbLocationHelper = new DBLocationHelper(getApplicationContext());
        int count = dbLocationHelper.getCount();

        MyDebug.MakeLog(2, "there are " + count + " event can be updated distance");
        if (count == 0) return true;


        double itemLat, itemLon;
        //DistanceCalculator Cal = new DistanceCalculator();

        for (int i = 0; i < count; i++) {
            itemLat = dbLocationHelper.getItemDouble(i, ColumnLocation.KEY.lat);
            itemLon = dbLocationHelper.getItemDouble(i, ColumnLocation.KEY.lon);
            dbLocationHelper.setItem(i, ColumnLocation.KEY.distance, DistanceCalculator.haversine(this.Last_Lat, this.Last_Lon, itemLat, itemLon));

            MyDebug.MakeLog(2, "set " + i + " event's distance success");
        }
        return true;
    }
}
