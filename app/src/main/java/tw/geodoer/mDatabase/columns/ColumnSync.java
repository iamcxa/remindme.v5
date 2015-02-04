package tw.geodoer.mDatabase.columns;

import android.provider.BaseColumns;

/**
 * @author Kent
 * @version 20140930
 * @category database
 */
public final class ColumnSync implements BaseColumns {

    // 預設排序常數
    public static final String DEFAULT_SORT_ORDER = "id DESC";
    // 查詢欄位陣列
    public static final String[] PROJECTION = new String[]{
            KEY._id,
            KEY.name,
            KEY.lat,
            KEY.lon,
            KEY.dintance
    };

    private ColumnSync() {
    }

    // 欄位索引
    public static class KEY_INDEX {
        public static final int _id = 0;
        public static final int name = 1;
        public static final int lat = 2;
        public static final int lon = 3;
        public static final int dintance = 4;
    }

    // 欄位名稱
    public static class KEY {
        public static final String _id = "_id";
        public static final String name = "name";
        public static final String lat = "lat";
        public static final String lon = "lon";
        public static final String dintance = "dintance";

    }
}