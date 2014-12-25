package tw.geodoer.mDatabase.API;

/**
 * @author Kent
 *
 */

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import tw.geodoer.mDatabase.columns.ColumnLocation;
import tw.geodoer.utils.MyDebug;

/**
 * @version 0.1
 * @since 20141203
 */
public class DBLocationHelper {

    private Context context;
    private Uri mUri = ColumnLocation.URI;

    /**
     * 主要結構
     *
     * @param context => getContext(),getActivity(),getApplicationContext(),...
     * @return 無
     * @throws null
     */
    public DBLocationHelper(Context context) {

        this.context = context;
    }

    /**
     * 此方法用來設定準備查詢的資料範圍。
     *
     * @param ''不傳入參數即為預設選取模式：<br> 1.全選資料表task_location所有欄位.<br>
     *                             2.selection和selectionArgs為null.<br>
     *                             3.sortOrder為"created DESC".
     * @return 回傳一個可以套進各式Adapter的Cursor資料物件。
     * @throws null
     */
    public Cursor getCursor() {

        return context.getContentResolver().
                query(mUri,
                        ColumnLocation.PROJECTION, null, null,
                        ColumnLocation.DEFAULT_SORT_ORDER);
    }

    /**
     * 此方法用來設定準備查詢的資料範圍。
     *
     * @param projection    型別為String[]。表示資料表中欲選擇的欄位--傳入null可回傳全部--不過如需回傳全部建議不傳用我的預設值，或匯入"ColumnLocation.PROJECTION"這組我預先定義的全選陣列。
     * @param selection     型別為String。即SQL之(where=..)語句，如直接傳入5即等於(where==5)；可傳入＝?搭配selectionArgs使用。例如: <br>
     *                      selection="City=高雄";
     * @param selectionArgs 型別為String[]。搭配selection與?使用，表示欲查詢的元素(s)。例如：<br>
     *                      selection="City=?"; <br>
     *                      selectionArgs={高雄,台北};
     * @param shortOrder    型別為String。表示欲排序的依據與順序。shortOrder="欄位名 [ASC,DESC]";
     * @return 回傳一個可以套進各式Adapter的Cursor資料物件。
     * @throws null
     */
    public Cursor getCursor(String[] projection,
                            String selection,
                            String[] selectionArgs,
                            String shortOrder) {

        return context.getContentResolver().
                query(mUri,
                        projection,
                        selection,
                        selectionArgs,
                        shortOrder);
    }


    /**
     * 此方法可取得資料表task_location之資料總數量。
     *
     * @return int
     * @throws null
     */
    public int getCount() {

        return getCursor().getCount();
    }


    /**
     * 本方法可取得特定編號資料行之特定欄位的字串。需傳入索引編號與欄位名稱。<br>
     * 注意：索引編號可能會隨著資料庫修改而被移除。
     *
     * @param objId      型別int；物件位於表格之索引ID。
     * @param columnName 型別是String。欄位名稱由ColumnLocation.KEY方法提供。例如：<br>
     *                   getItemString(10, ColumnLocation.KEY.title)可取得id=10資料的title字串。
     * @return String
     * @throws Exception e.toString() or String "null".
     */
    public String getItemString(int objId, String columnName) {

        String[] projection = {"_id", columnName};
        String[] argStrings = {String.valueOf(objId)};
        Cursor cursor = getCursor(projection, "_id=?", argStrings, "_id DESC");
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            try {

                return cursor.getString(1).toString();
            } catch (Exception e) {

                return e.toString();
            }
        } else {
            return "null";
        }
    }


    /**
     * 本方法可取得特定編號資料行之特定欄位的數值。需傳入索引編號與欄位名稱。<br>
     * 注意：索引編號可能會隨著資料庫修改而被移除。
     *
     * @param itemId     型別int；物件位於表格之索引ID。
     * @param columnName 型別是String。欄位名稱由ColumnLocation.KEY方法提供。例如：<br>
     *                   getItemInt(10, ColumnLocation.KEY.title)可取得id=10資料的title字串。
     * @return String
     * @throws "-1".
     */
    protected int getItemInt(int itemId, String columnName) {

        String[] projection = {"_id", columnName};
        String[] argStrings = {String.valueOf(itemId)};
        Cursor cursor = getCursor(projection, "_id=?", argStrings, "_id DESC");
        try {
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {

                return cursor.getInt(1);
            } else {

                return 0;
            }
        } catch (Exception e){

            return 0;
        }
    }


    /**
     * 本方法可取得特定編號資料行之特定欄位的數值。需傳入索引編號與欄位名稱。<br>
     * 注意：索引編號可能會隨著資料庫修改而被移除。
     *
     * @param itemId     型別int；物件位於表格之索引ID。
     * @param columnName 型別是String。欄位名稱由ColumnLocation.KEY方法提供。例如：<br>
     *                   getItemInt(10, ColumnLocation.KEY.title)可取得id=10資料的title字串。
     * @return String
     * @throws 0.0.
     */
    public Double getItemDouble(int itemId, String columnName) {

        String[] projection = {"_id", columnName};
        String[] argStrings = {String.valueOf(itemId)};

        try {
            Cursor cursor = getCursor(projection, "_id=?", argStrings, "_id DESC");
            cursor.moveToFirst();
            if (cursor.getCount() > 0) return cursor.getDouble(1);
            else return 0d;

        } catch (Exception e) {
            return 0d;
        }
    }


    /**
     * 本方法可新增一筆資料到資料表task_locations。<br>
     *
     * @param locName      型別String；地點名稱。
     * @param lat          型別String；緯度。
     * @param lon          型別String；經度。
     * @param distance     型別Double；距離。
     * @param lastUsedTime 型別Double；上次使用時間。
     * @param weight       型別int；使用者造訪偏好(地點權重)。
     * @param type         型別int；地點類型。
     * @param tag          型別String；地點tag。
     * @return true.
     * @throws false , also logcat will output "DBLocationHelpr additem method error=..."。
     */
    public boolean addItem(String locName, String lat, String lon,
                           Double distance, Double lastUsedTime,
                           int weight, int type,
                           String tag) {

        ContentValues values = new ContentValues();
        // 0 - 任務地點id
        // 1 - 地點名稱字串
        values.put(ColumnLocation.KEY.name, locName);
        // 2 - 3 - 經緯度
        values.put(ColumnLocation.KEY.lat, lat);
        values.put(ColumnLocation.KEY.lon, lon);
        // 4 - 與上次偵測地點之距離
        values.put(ColumnLocation.KEY.distance, distance);
        // 5 - 上次使用時間
        values.put(ColumnLocation.KEY.lastUsedTime, lastUsedTime);
        // 6 - 上次使用時間
        values.put(ColumnLocation.KEY.weight, weight);
        // 7 - 地點類型
        values.put(ColumnLocation.KEY.type, type);
        // 8 - 地點關鍵字
        values.put(ColumnLocation.KEY.tag, tag);

        try {
            context.getContentResolver().insert(mUri, values);
            return true;
        } catch (Exception e) {
            // TODO: handle exception
            MyDebug.MakeLog(2, "DBLocationHelper additem method error=" + e.toString());
            return false;
        }
    }

    /**
     * 本方法可更新一筆資料資料表task_locations中的字串資料。<br>
     *
     * @param itemId    型別String；地點ID。
     * @param targetKey 型別String；目標欄位名稱，由ColumnLocation.Key提供。
     * @param newValue  型別String；目標欄位的新值。
     * @return true.
     * @throws false, also logcat will output "DBLocationHelpr setItem method error=..."。
     */
    public boolean setItem(int itemId, String targetKey, String newValue) {

        try {

            Uri thisUri = ContentUris.withAppendedId(mUri, itemId);

            ContentValues values = new ContentValues();

            values.put(targetKey, newValue);

            context.getContentResolver().update(thisUri, values, null, null);

            return true;
        } catch (Exception e) {

            MyDebug.MakeLog(2, "DBLocationHelper setItem method error=" + e.toString());
            return false;
        }
    }

    /**
     * 本方法可更新一筆資料資料表task_locations中的數值資料。<br>
     *
     * @param itemId    型別String；地點ID。
     * @param targetKey 型別String；目標欄位名稱，由ColumnLocation.Key提供。
     * @param newValue  型別Double；目標欄位的新值。
     * @return True.
     * @throws false, also logcat will output "DBLocationHelpr setItem method error=..."。
     */
    public boolean setItem(int itemId, String targetKey, Double newValue) {

        try {

            Uri thisUri = ContentUris.withAppendedId(mUri, itemId);

            ContentValues values = new ContentValues();

            values.put(targetKey, newValue);

            context.getContentResolver().update(thisUri, values, null, null);

            return true;
        } catch (Exception e) {

            MyDebug.MakeLog(2, "DBLocationHelpr setItem method error=" + e.toString());
            return false;
        }
    }


    /**
     * 本方法可刪除一筆資料資料表task_locations中的資料。<br>
     *
     * @param itemId 型別String；地點ID。
     * @return True.
     * @throws false, also logcat will output "DBLocationHelpr deleteItem method error=..."。
     */
    public boolean deleteItem(int itemId) {

        try {

            context.getContentResolver().delete(mUri, "_id = ?", new String[]{String.valueOf(itemId)});

            return true;
        } catch (Exception e) {

            MyDebug.MakeLog(2, "DBLocationHelpr deleteItem method error=" + e.toString());
            return false;
        }
    }
}