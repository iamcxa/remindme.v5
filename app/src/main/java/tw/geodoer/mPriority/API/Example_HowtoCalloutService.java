package tw.geodoer.mPriority.API;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class Example_HowtoCalloutService extends Service
{
    @Override
    public void onCreate()
    {
        Context context= this;

        //call out by service caller
        //ServiceCaller.call(context,key)

        //call out the service_position
        ServiceCaller.call(context,ServiceCaller.KEY_POSITION);

        //call out the service_weight
        ServiceCaller.call(context,ServiceCaller.KEY_WEIGHT);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}