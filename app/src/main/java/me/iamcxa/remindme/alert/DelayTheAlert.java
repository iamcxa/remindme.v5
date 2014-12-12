package me.iamcxa.remindme.alert;

import java.util.Calendar;

import tw.remindme.common.function.MyDebug;

import me.iamcxa.remindme.editor.Act_SetAlarm;
import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class DelayTheAlert extends IntentService {

	public DelayTheAlert() {
		super(null);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub

		Bundle b = intent.getExtras();

		String taskID=b.getString("taskID");

		MyDebug.MakeLog(2,"@DelayTheAlert taskID="+ taskID);

		NotificationManager nm = 
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancel("remindme",Integer.valueOf(taskID));

		//AlertHandler alertHandler=AlertHandler.getInstance();

		AlertHandler alertHandler=new AlertHandler();

		Calendar calendar=Calendar.getInstance();

		//calendar.clear();

		//calendar.setTimeInMillis(System.currentTimeMillis());

		calendar.add(Calendar.MINUTE, 5);

		Act_SetAlarm act_SetAlarm=new Act_SetAlarm(
				this, calendar.getTimeInMillis(), Integer.valueOf(taskID));
		act_SetAlarm.SetIt();

		ShowToastInIntentService("延遲任務 "+alertHandler.getTaskName(this, taskID) +" 5  分鐘");
	}

	public void ShowToastInIntentService(final String sText)
	{  final Context MyContext = this;
	new Handler(Looper.getMainLooper()).post(new Runnable()
	{  @Override public void run()
	{  Toast toast1 = Toast.makeText(MyContext, sText, 5);
	toast1.show(); 
	}
	});
	};
	

}
