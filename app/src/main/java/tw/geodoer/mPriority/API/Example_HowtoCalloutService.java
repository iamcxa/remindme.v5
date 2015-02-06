package tw.geodoer.mPriority.API;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class Example_HowtoCalloutService extends Service
{
    @Override
    public void onCreate()
    {

        //BroadcastSender.send(context,key)

        //call out the service_position
        BroadcastSender.send(this,BroadcastSender.KEY_POSITION);

        //call out the service_weight
        BroadcastSender.send(this,BroadcastSender.KEY_WEIGHT);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}