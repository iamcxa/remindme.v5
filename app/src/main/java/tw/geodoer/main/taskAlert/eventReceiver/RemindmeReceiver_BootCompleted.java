package tw.geodoer.main.taskAlert.eventReceiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import tw.geodoer.utils.MyDebug;

/**
 * @author iamcxa 定時提醒廣播
 */
public class RemindmeReceiver_BootCompleted extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		
	
		 Bundle bData = intent.getExtras();
//		 
	        if(bData.get("msg").equals("android.intent.action.BOOT_COMPLETED")) {

	    		MyDebug.MakeLog(2, "智慧提醒＠開機啟動完成！");
	    		
	        }else if(bData.get("msg").equals("android.net.wifi.supplicant.STATE_CHANGE")) {

	    		MyDebug.MakeLog(2,  "智慧提醒＠wifi狀態改變！");
	        	
			}else if(bData.get("msg").equals("android.net.nsd.STATE_CHANGED")) {

				MyDebug.MakeLog(2,  "智慧提醒＠網路連線狀態改變！");
				
			}
//	        
//
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

//		intent.setClass(context, TaskSortingService.class);
//		
//		MyDebug.MakeLog(0,"準備啟動TaskSortingService");
//		try {
//			context.startActivity(intent);
//
//			MyDebug.MakeLog(0,"TaskSortingService 啟動完成");
//		} catch (Exception e) {
//			// TODO: handle exception
//			MyDebug.MakeLog(0,"啟動TaskSortingService失敗！error="+e.toString());
//		}
	}
}
