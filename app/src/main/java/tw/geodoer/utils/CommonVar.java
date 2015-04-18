/**
 *
 */
package tw.geodoer.utils;

import java.util.Locale;

/**
 * @author cxa
 */
public class CommonVar {

    public static final String BundleName = "Bundle";
    // 授權常數
    public static final String AUTHORITY = "com.geodoer.geotodo";
    // 資料庫版本
    public static final int DB_VERSION = 6;
    // URI常數
    public static final String TASKLIST = "geotodo";
    public static final String CONTENT_TYPE =
            "vnd.android.cursor.dir/vnd." + TASKLIST;
    public static final String CONTENT_ITEM_TYPE =
            "vnd.android.cursor.item/vnd." + TASKLIST;
    // 廣播接收器
    public static final String BC_ACTION = "me.iamcxa.remindme.TaskReceiver";
    // 預設地區
    public static Locale DEFAULT_LOCAL = Locale.TAIWAN;
    //
    public static String[] TASKEDITOR_DUEDATE_BASIC_STRING_ARRAY = {""};
    public static String[] TASKEDITOR_DUEDATE_EXTRA_STRING_ARRAY = {""};
    public static final String STRING_DRAWER_POSITION = "drawer_position";
    private CommonVar() {
    }
}