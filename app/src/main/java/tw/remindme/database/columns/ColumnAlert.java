package tw.remindme.database.columns;

import android.net.Uri;
import android.provider.BaseColumns;

import tw.remindme.common.function.CommonVar;

/**
 * @author Kent
 * @category database
 * @version 20140930
 */
public final class ColumnAlert implements BaseColumns {

	private ColumnAlert() {
	}

	// 預設排序常數
	public static final String DEFAULT_SORT_ORDER = "_id DESC";

	// 資料表名稱常數
	public static final String TABLE_NAME = "task_alerts";

	// 存取Uri
	public static final Uri URI =
			Uri.parse("content://" + CommonVar.AUTHORITY + "/" +TABLE_NAME);

	public static final String exec_SQL_Statment=
			"CREATE TABLE "
					+ TABLE_NAME
					+ " ("
					+KEY._id  + " INTEGER PRIMARY KEY autoincrement,"
					+KEY.due_date_millis  + " INTEGER,"
					+KEY.due_date_string  + " TEXT,"
					+KEY.interval + " TEXT,"
					+KEY.loc_id +  " INTEGER,"
					+KEY.loc_on + " TEXT,"
					+KEY.loc_radius +  " INTEGER,"
					+KEY.other + " TEXT,"
					+KEY.task_id + " INTEGER,"
					+KEY.time_offset + " TEXT,"
					+KEY.type + " TEXT"
					+ ");";

	// 查詢欄位陣列
	public static final String[] PROJECTION = new String[] { 
		KEY._id ,
		KEY.due_date_millis ,
		KEY.due_date_string ,
		KEY.interval,
		KEY.loc_id,
		KEY.loc_on,
		KEY.loc_radius,
		KEY.other,
		KEY.task_id,
		KEY.time_offset,
		KEY.type
	};

	// 欄位索引
	public static class KEY_INDEX {
		public static final int _id = 0;
		public static final int task_id = 1;

		public static final int type = 2;
		public static final int interval = 3;
		public static final int time_offset = 4;
		public static final int due_date_millis = 5;
		public static final int due_date_int =6;

		public static final int loc_on = 7;
		public static final int loc_id = 8;
		public static final int loc_radius =9;

		public static final int other =10;
	}

	// 欄位名稱
	public static class KEY {
		public static final String _id = "_id";
		public static final String task_id = "task_id";

		public static final String type = "type";
		public static final String interval = "interval";
		public static final String time_offset = "time_offset";
		public static final String due_date_millis = "due_date_millis";
		public static final String due_date_string = "due_date_string";

		public static final String loc_on = "loc_on";
		public static final String loc_id = "loc_id";
		public static final String loc_radius = "loc_radius";

		public static final String other = "other";
	}
}