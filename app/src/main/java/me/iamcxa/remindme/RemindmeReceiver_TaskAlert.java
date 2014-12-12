package me.iamcxa.remindme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import me.iamcxa.remindme.alert.AlertHandler;
import tw.remindme.common.function.MyDebug;

/**
 * @author iamcxa 定時提醒廣播
 */
public class RemindmeReceiver_TaskAlert extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {

		MyDebug.MakeLog(2, "@RemindmeReceiver_TaskAlert");

		Bundle b = intent.getExtras();
		
		Bundle newB = new Bundle();
		
		if(b.get("msg").equals("me.iamcxa.remindme.TaskReceiver"))
		{
			//intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			//
			intent.setClass(context, AlertHandler.class);
			//
						
			newB.putString("taskID", b.get("taskID").toString());
			
		    intent.putExtras(newB);
		    
			context.startService(intent);

		}
	}
}
