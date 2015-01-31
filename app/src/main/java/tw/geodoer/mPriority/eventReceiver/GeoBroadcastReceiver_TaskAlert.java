package tw.geodoer.mPriority.eventReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import tw.geodoer.mPriority.service.GeoServiceNotification;
import tw.geodoer.utils.MyDebug;
import tw.geodoer.main.taskAlert.controller.AlertHandler;

/**
 * @author iamcxa 定時提醒廣播
 */
public class GeoBroadcastReceiver_TaskAlert extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {

		MyDebug.MakeLog(2, "@RemindmeReceiver_TaskAlert");

		Bundle b = intent.getExtras();
		
		Bundle newB = new Bundle();

		if(b.get("msg").equals("me.iamcxa.remindme.alarm"))
		{
			//intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			//
			intent.setClass(context, AlertHandler.class);
			//

			newB.putString("taskID", b.get("taskID").toString());

		    intent.putExtras(newB);

			context.startService(intent);

		}
        else if(b.get("msg").equals("me.iamcxa.remindme.location"))
        {

            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


            //------------------------------------------------------------------------

            //set location handler
            intent.setClass(context,GeoServiceNotification.class);

            //------------------------------------------------------------------------


            newB.putString("taskID", b.get("taskID").toString());
            intent.putExtra(GeoServiceNotification.MESSAGE, "12331321546");
            intent.putExtras(newB);

            context.startService(intent);


        }
	}
}
