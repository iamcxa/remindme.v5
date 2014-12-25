package tw.geodoer.mDatabase.columns;

import android.net.Uri;
import android.provider.BaseColumns;

import tw.geodoer.common.controller.CommonVar;

/**
 * @author Kent
 * @category database
 * @since  20141222
 * 注意：每改完任一欄位都要檢查全部共四個對應位置是不是都有改到。
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
                    // 提醒與事件欄位
                    +KEY._id  + " INTEGER PRIMARY KEY autoincrement,"
                    +KEY.task_id + " INTEGER,"
                    +KEY.state + " INTEGER,"
                    +KEY.type + " INTEGER,"
                    // 提醒時間欄位
                    +KEY.interval + " TEXT,"
                    +KEY.time_offset + " TEXT,"
                    +KEY.due_date_millis  + " INTEGER,"
                    +KEY.due_date_string  + " TEXT,"
                    +KEY.actMon + " INTEGER,"
                    +KEY.actTue + " INTEGER,"
                    +KEY.actWed + " INTEGER,"
                    +KEY.actThu + " INTEGER,"
                    +KEY.actFri + " INTEGER,"
                    +KEY.actSat + " INTEGER,"
                    +KEY.actSun + " INTEGER,"
                    // 提醒地點欄位
                    +KEY.loc_id +  " INTEGER,"
                    +KEY.loc_on + " TEXT,"
                    +KEY.loc_radius +  " INTEGER,"
                    // 其他欄位
                    +KEY.other + " TEXT"
                    + ");";

    // 查詢欄位陣列
    public static final String[] PROJECTION = new String[] {
            // 提醒ID與關聯事件ID
            KEY._id,
            KEY.task_id,
            // 提醒狀態 - 已結束/等待..etc
            KEY.state,
            // 提醒類型
            KEY.type,
            // 提醒間隔
            KEY.interval,
            // 時間偏移量
            KEY.time_offset,
            // 到期時間
            KEY.due_date_millis,
            KEY.due_date_string,
            // 觸發日
            KEY.actMon,
            KEY.actTue,
            KEY.actWed,
            KEY.actThu,
            KEY.actFri,
            KEY.actSat,
            KEY.actSun,
            // 地點偵測開關
            KEY.loc_on,
            // 地點ID
            KEY.loc_id,
            // 觸發範圍
            KEY.loc_radius,
            // 備用欄位
            KEY.other
    };

    // 欄位名稱
    public static class KEY {
        /*========================
                提醒與事件欄位:4
         ========================*/
        // 提醒ID與關聯事件ID
        public static final String _id = "_id";
        public static final String task_id = "task_id";
        // 提醒狀態 - 已結束/等待..etc
        public static final String state = "state";
        // 提醒類型
        public static final String type = "type";

        /*========================
                到期時間欄位:11
         ========================*/
        // 提醒間隔
        public static final String interval = "interval";
        // 時間偏移量
        public static final String time_offset = "time_offset";
        // 到期時間
        public static final String due_date_millis = "due_date_millis";
        public static final String due_date_string = "due_date_string";
        // 觸發日
        public static final String actMon = "actMon";
        public static final String actTue = "actTue";
        public static final String actWed = "actWed";
        public static final String actThu = "actThu";
        public static final String actFri = "actFri";
        public static final String actSat = "actSat";
        public static final String actSun = "actSun";

        /*========================
                提醒地點欄位:3
         ========================*/
        // 地點偵測開關
        public static final String loc_on = "loc_on";
        // 地點ID
        public static final String loc_id = "loc_id";
        // 觸發範圍
        public static final String loc_radius = "loc_radius";

        /*========================
                其他欄位:1
         ========================*/
        // 備用欄位
        public static final String other = "other";
    }

    // 欄位索引
    public static class KEY_INDEX {

        // 提醒ID與關聯事件ID
        public static final int _id = 0;
        public static final int task_id = 1;
        // 提醒狀態 - 已結束/等待..etc
        public static final int state = 2;
        // 提醒類型
        public static final int type = 3;
        // 提醒間隔
        public static final int interval = 4;
        // 時間偏移量
        public static final int time_offset = 5;
        // 到期時間
        public static final int due_date_millis = 6;
        public static final int due_date_int = 7;
        // 觸發日
        public static final int actMon = 8;
        public static final int actTue = 9;
        public static final int actWed = 10;
        public static final int actThu = 11;
        public static final int actFri = 12;
        public static final int actSat = 13;
        public static final int actSun = 14;
        // 地點偵測開關
        public static final int loc_on = 15;
        // 地點ID
        public static final int loc_id = 16;
        // 觸發範圍
        public static final int loc_radius = 17;
        // 備用欄位
        public static final int other = 18;
    }
}