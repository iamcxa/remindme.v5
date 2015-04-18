package tw.geodoer.mDatabase.columns;

import android.net.Uri;
import android.provider.BaseColumns;

import tw.geodoer.utils.CommonVar;

/**
 * @author Kent
 * @version 20140930
 * @category database
 */
public final class ColumnLocation implements BaseColumns {

    // 預設排序常數
    public static final String DEFAULT_SORT_ORDER = "_id ASC";
    public static final String SORT_BY_LASTUSEDTIME = KEY.lastUsedTime+" DESC";
    // 資料表名稱常數
    public static final String TABLE_NAME = "task_locations";
    // 存取Uri
    public static final Uri URI =
            Uri.parse("content://" + CommonVar.AUTHORITY + "/" + TABLE_NAME);
    public static final String exec_SQL_Statment =
            "CREATE TABLE "
                    + TABLE_NAME
                    + " ("
                    + KEY._id + " INTEGER PRIMARY KEY autoincrement,"
                    + KEY.name + " TEXT,"
                    + KEY.lat + " TEXT,"
                    + KEY.lon + " TEXT,"
                    + KEY.distance + " INTEGER,"
                    + KEY.lastUsedTime + " INTEGER,"
                    + KEY.weight + " INTEGER,"
                    + KEY.type + " INTEGER,"
                    + KEY.address + " TEXT"
                    + ");";
    // 查詢欄位陣列
    public static final String[] PROJECTION = new String[]{
            KEY._id,
            KEY.name,
            KEY.lat,
            KEY.lon,
            KEY.distance,
            KEY.lastUsedTime,
            KEY.weight,
            KEY.type,
            KEY.address
    };
    private ColumnLocation() {
    }

    // 欄位名稱
    public static class KEY {
        public static final String _id = "_id";
        public static final String name = "name";
        public static final String lat = "lat";
        public static final String lon = "lon";
        public static final String distance = "distance";
        public static final String lastUsedTime = "lastUsedTime";
        public static final String weight = "weight";
        public static final String type = "type";
        public static final String address = "address";


        // 欄位索引
        public static class INDEX {
            public static final int _id = 0;
            public static final int name = 1;
            public static final int lat = 2;
            public static final int lon = 3;
            public static final int distance = 4;
            public static final int lastUsedTime = 5;
            public static final int weight = 6;
            public static final int type = 7;
            public static final int address = 8;
        }
    }
}
