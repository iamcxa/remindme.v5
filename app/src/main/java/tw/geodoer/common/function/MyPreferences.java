package tw.geodoer.common.function;

import android.content.SharedPreferences;

public class MyPreferences {
	
	private MyPreferences(){}

	// SharedPreferences preferences;
	public static SharedPreferences mPreferences;

	// isServiceOn
	public static boolean IS_SERVICE_ON() {
		return mPreferences.getBoolean("isServiceOn", false);
	};

	// isServiceOn
	public static boolean IS_SORTING_ON() {
		return mPreferences.getBoolean("isSortingOn", false);
	};

	// isServiceOn
	public static String getUpdatePeriod() {
		return mPreferences.getString("GetPriorityPeriod", "5000");
	};	
	
	// debug msg on/off, read from Shared Preferences XML file
	public static boolean IS_DEBUG_MSG_ON() {
		return mPreferences.getBoolean("isDebugMsgOn", true);
	}
	

	
	public static class GpsSetting {
		// GPS超時關閉改用wifi
		public static final int TIMEOUT_SEC = 5;
		// Gps狀態
		public static boolean GpsStatus = false;

		// 移動距離
		public static final double GpsTolerateErrorDistance = 1.5;

	}
}
