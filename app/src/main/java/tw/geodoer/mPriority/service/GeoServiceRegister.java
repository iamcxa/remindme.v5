package tw.geodoer.mPriority.service;


import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;

import android.os.IBinder;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

import tw.geodoer.mPriority.eventReceiver.GeoBroadcastReceiver;
import tw.geodoer.main.taskList.view.AppMainActivity;
import tw.geodoer.utils.MyDebug;
import tw.moretion.geodoer.R;

/**
 * @Murakumo
 *
 */
public class GeoServiceRegister extends Service
{
    @Override
    public IBinder onBind(Intent arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

	@Override
	public void onDestroy()
    {
		// TODO Auto-generated method stub
		super.onDestroy();
	}



	@Override
	public void onCreate()
    {
		super.onCreate();

        /*
        IntentFilter itfilter =new IntentFilter();
        itfilter.addAction("tw.geodoer.mPriority.service.RemainBroadcast");
        GeoBroadcastReceiver myReceiver = new GeoBroadcastReceiver();
        try
        {
            registerReceiver(myReceiver, itfilter);
        }
        catch(IllegalArgumentException  e)
        {
            MyDebug.MakeLog(2,"Receiver has been registed");
        }
        //send a message
        Intent it =new Intent("tw.geodoer.mPriority.service.RemainBroadcast");
        it.putExtra("Command", GeoBroadcastReceiver.BROADCAST_COMMAND_POSITON);
        //it.putExtra(GeoServiceNotification.MESSAGE,"Notification send success");
        sendBroadcast(it);
*/









        Stopself();
	}
	
	private void Stopself()
    {
        this.stopSelf();
	}

	@Override
	public void onLowMemory()
    {
		// TODO Auto-generated method stub
		super.onLowMemory();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
    {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}


}
