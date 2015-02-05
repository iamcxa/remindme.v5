/**
 *
 */
package tw.geodoer.mDatabase.contentProvider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

import java.util.HashMap;

import tw.geodoer.mDatabase.columns.ColumnAlert;
import tw.geodoer.mDatabase.columns.ColumnLocation;
import tw.geodoer.mDatabase.columns.ColumnTask;
import tw.geodoer.utils.CommonVar;

/**
 * @author cxa 資料庫操作方法
 */

public class TaskDbProvider extends ContentProvider {
    // 資料庫名稱常數
    public static final String DATABASE_NAME = "geodoerDB.db";
    // 資料庫版本常數
    public static final int DATABASE_VERSION = 3;
    // 查詢、更新條件
    private static final int URI_TASKS = 101;
    private static final int URI_TASK_ID = 102;
    //
    private static final int URI_ALERTS = 201;
    private static final int URI_ALERT_ID = 202;
    //
    private static final int URI_LOCATIONS = 301;
    private static final int URI_LOCATION_ID = 302;
    //
    private static final String TABLE_TASK = ColumnTask.TABLE_NAME;
    private static final String TABLE_ALERT = ColumnAlert.TABLE_NAME;
    private static final String TABLE_LOCATION = ColumnLocation.TABLE_NAME;
    //-----------------------------------------------------//
    // Uri工具類別
    private static final UriMatcher sUriMatcher;
    // 查詢欄位集合
    private static HashMap<String, String> sTaskProjectionMap;
    private static HashMap<String, String> sTaskAlertProjectionMap;
    private static HashMap<String, String> sTaskLocationProjectionMap;
    // 資料庫工具類別實例
    private DatabaseHelper mOpenHelper;

    // 建立或開啟資料庫
    @Override
    public boolean onCreate() {
        mOpenHelper = new DatabaseHelper(getContext());
        return true;
    }

    // 查詢
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String orderBy = sortOrder;

        switch (sUriMatcher.match(uri)) {
            // TASK
            case URI_TASKS:// 查詢所有任務
                orderBy = ColumnTask.DEFAULT_SORT_ORDER;
                qb.setTables(TABLE_TASK);
                qb.setProjectionMap(sTaskProjectionMap);
                break;
            case URI_TASK_ID:// 根據任務ID查詢
                orderBy = ColumnTask.DEFAULT_SORT_ORDER;
                qb.setTables(TABLE_TASK);
                qb.setProjectionMap(sTaskProjectionMap);
                qb.appendWhere(BaseColumns._ID + "=" + uri.getPathSegments().get(1));
                break;

            // ALERT
            case URI_ALERTS: //查詢所有提醒
                orderBy = ColumnAlert.DEFAULT_SORT_ORDER;
                qb.setTables(TABLE_ALERT);
                qb.setProjectionMap(sTaskAlertProjectionMap);
                break;
            case URI_ALERT_ID: //根據提醒ID查詢
                orderBy = ColumnAlert.DEFAULT_SORT_ORDER;
                qb.setTables(TABLE_ALERT);
                qb.setProjectionMap(sTaskAlertProjectionMap);
                qb.appendWhere(BaseColumns._ID + "=" + uri.getPathSegments().get(1));
                break;

            // Location
            case URI_LOCATIONS: //查詢所有提醒
                orderBy = ColumnLocation.DEFAULT_SORT_ORDER;
                qb.setTables(TABLE_LOCATION);
                qb.setProjectionMap(sTaskLocationProjectionMap);
                break;
            case URI_LOCATION_ID: //根據提醒ID查詢
                orderBy = ColumnLocation.DEFAULT_SORT_ORDER;
                qb.setTables(TABLE_LOCATION);
                qb.setProjectionMap(sTaskLocationProjectionMap);
                qb.appendWhere(BaseColumns._ID + "=" + uri.getPathSegments().get(1));
                break;

            default:
                throw new IllegalArgumentException("Uri錯誤！ " + uri + "/"
                        + sUriMatcher.match(uri));
        }

        if (!TextUtils.isEmpty(sortOrder)) orderBy = sortOrder;

        // 取得資料庫實例
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        // 返回游標集合
        Cursor c = qb.query(db, projection, selection, selectionArgs, null,
                null, null);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    // 取得類型
    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case URI_TASKS:
                return CommonVar.CONTENT_TYPE;
            case URI_TASK_ID:
                return CommonVar.CONTENT_ITEM_TYPE;

            case URI_ALERTS:
                return CommonVar.CONTENT_TYPE;
            case URI_ALERT_ID:
                return CommonVar.CONTENT_ITEM_TYPE;

            case URI_LOCATIONS:
                return CommonVar.CONTENT_TYPE;
            case URI_LOCATION_ID:
                return CommonVar.CONTENT_ITEM_TYPE;

            default:
                throw new IllegalArgumentException("錯誤的 URI！ " + uri);
        }
    }

    // 保存資料
    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        ContentValues values;
        SQLiteDatabase db;
        long rowId;

        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }

        switch (sUriMatcher.match(uri)) {
            //----------------------------- TASKS -----------------------------//
            case URI_TASKS:
                // 取得資料庫實例
                db = mOpenHelper.getWritableDatabase();
                // 保存資料返回欄位ID
                rowId = db.insert(TABLE_TASK, ColumnTask.KEY._id,
                        values);
                if (rowId > 0) {
                    Uri thisUri = ContentUris.withAppendedId(ColumnTask.URI,
                            rowId);
                    getContext().getContentResolver().notifyChange(thisUri, null);
                    return thisUri;
                } else {
                    throw new SQLException("插入資料表TASKS失敗 " + uri);
                }

                //----------------------------- ALERTS -----------------------------//
            case URI_ALERTS:
                // 取得資料庫實例
                db = mOpenHelper.getWritableDatabase();
                // 保存資料返回欄位ID
                rowId = db.insert(TABLE_ALERT, ColumnAlert.KEY._id,
                        values);
                if (rowId > 0) {
                    Uri thisUri = ContentUris.withAppendedId(ColumnAlert.URI,
                            rowId);
                    getContext().getContentResolver().notifyChange(thisUri, null);
                    return thisUri;
                } else {
                    throw new SQLException("插入資料表ALERTS失敗 " + uri);
                }

                //--------------------------- LOCATIONS ---------------------------//
            case URI_LOCATIONS:
                // 取得資料庫實例
                db = mOpenHelper.getWritableDatabase();
                // 保存資料返回欄位ID
                rowId = db.insert(TABLE_LOCATION, ColumnLocation.KEY._id,
                        values);
                if (rowId > 0) {
                    Uri thisUri = ContentUris.withAppendedId(ColumnLocation.URI,
                            rowId);
                    getContext().getContentResolver().notifyChange(thisUri, null);
                    return thisUri;
                } else {
                    throw new SQLException("插入資料表LOCATIONS失敗 " + uri);
                }

            default:
                throw new IllegalArgumentException("錯誤的 URI！ " + uri);
        }


    }

    // 刪除資料
    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        // 取得資料庫實例
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        String itemId;

        switch (sUriMatcher.match(uri)) {
            //--------------------------- TASKS ---------------------------//
            case URI_TASKS:    // 根據指定條件刪除
                count = db.delete(TABLE_TASK, where, whereArgs);
                break;
            case URI_TASK_ID:// 根據指定條件和ID刪除
                itemId = uri.getPathSegments().get(1);
                count = db.delete(TABLE_TASK,
                        BaseColumns._ID
                                + "="
                                + itemId
                                + (!TextUtils.isEmpty(where) ? " AND (" + where
                                + ')' : ""), whereArgs);
                break;

            //----------------------------- ALERTS -----------------------------//
            case URI_ALERTS:    // 根據指定條件刪除
                count = db.delete(TABLE_ALERT, where, whereArgs);
                break;
            case URI_ALERT_ID:// 根據指定條件和ID刪除
                itemId = uri.getPathSegments().get(1);
                count = db.delete(TABLE_ALERT,
                        BaseColumns._ID
                                + "="
                                + itemId
                                + (!TextUtils.isEmpty(where) ? " AND (" + where
                                + ')' : ""), whereArgs);
                break;

            //--------------------------- LOCATIONS ---------------------------//
            case URI_LOCATIONS:    // 根據指定條件刪除
                count = db.delete(TABLE_LOCATION, where, whereArgs);
                break;
            case URI_LOCATION_ID:// 根據指定條件和ID刪除
                itemId = uri.getPathSegments().get(1);
                count = db.delete(TABLE_LOCATION,
                        BaseColumns._ID
                                + "="
                                + itemId
                                + (!TextUtils.isEmpty(where) ? " AND (" + where
                                + ')' : ""), whereArgs);
                break;

            default:
                throw new IllegalArgumentException("錯誤的 URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    // 更新資料
    @Override
    public int update(Uri uri, ContentValues values, String where,
                      String[] whereArgs) {
        // 取得資料庫實例
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        String itemId;

        switch (sUriMatcher.match(uri)) {
            //--------------------------- TASKS ---------------------------//
            // 根據指定條件更新
            case URI_TASKS:
                count = db.update(TABLE_TASK, values, where, whereArgs);
                break;
            // 根據指定條件和ID更新
            case URI_TASK_ID:
                itemId = uri.getPathSegments().get(1);
                count = db.update(TABLE_TASK, values,
                        BaseColumns._ID
                                + "="
                                + itemId
                                + (!TextUtils.isEmpty(where) ? " AND (" + where
                                + ')' : ""), whereArgs);
                break;

            //------------------------- ALERTS ------------------------//
            // 根據指定條件更新
            case URI_ALERTS:
                count = db.update(TABLE_ALERT, values, where, whereArgs);
                break;
            // 根據指定條件和ID更新
            case URI_ALERT_ID:
                itemId = uri.getPathSegments().get(1);
                count = db.update(TABLE_ALERT, values,
                        BaseColumns._ID
                                + "="
                                + itemId
                                + (!TextUtils.isEmpty(where) ? " AND (" + where
                                + ')' : ""), whereArgs);
                break;

            //----------------------- LOCATIONS -----------------------//
            // 根據指定條件更新
            case URI_LOCATIONS:
                count = db.update(TABLE_LOCATION, values, where, whereArgs);
                break;
            // 根據指定條件和ID更新
            case URI_LOCATION_ID:
                itemId = uri.getPathSegments().get(1);
                count = db.update(TABLE_LOCATION, values,
                        BaseColumns._ID
                                + "="
                                + itemId
                                + (!TextUtils.isEmpty(where) ? " AND (" + where
                                + ')' : ""), whereArgs);
                break;

            default:
                throw new IllegalArgumentException("錯誤的 URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
    static {
        // Uriぁ匹配工具類別
        // Task
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(CommonVar.AUTHORITY, TABLE_TASK, URI_TASKS);
        sUriMatcher.addURI(CommonVar.AUTHORITY, TABLE_TASK + "/#",
                URI_TASK_ID);
        // Alert
        sUriMatcher.addURI(CommonVar.AUTHORITY, TABLE_ALERT, URI_ALERTS);
        sUriMatcher.addURI(CommonVar.AUTHORITY, TABLE_ALERT + "/#",
                URI_ALERT_ID);
        // Location
        sUriMatcher.addURI(CommonVar.AUTHORITY, TABLE_LOCATION, URI_LOCATIONS);
        sUriMatcher.addURI(CommonVar.AUTHORITY, TABLE_LOCATION + "/#",
                URI_LOCATION_ID);

        // 實例化查詢欄位集合/添加查詢欄位
        // Task
        sTaskProjectionMap = new HashMap<String, String>();
        for (String element : ColumnTask.PROJECTION) {
            sTaskProjectionMap.put(element, element);
        }
        // Alert
        sTaskAlertProjectionMap = new HashMap<String, String>();
        for (String element : ColumnAlert.PROJECTION) {
            sTaskAlertProjectionMap.put(element, element);
        }
        // Location
        sTaskLocationProjectionMap = new HashMap<String, String>();
        for (String element : ColumnLocation.PROJECTION) {
            sTaskLocationProjectionMap.put(element, element);
        }
    }

    // 內部工具類別，建立或開啟資料庫、建立或刪除資料表
    public static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        // 建立資料表
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(ColumnTask.exec_SQL_Statment);
            db.execSQL(ColumnAlert.exec_SQL_Statment);
            db.execSQL(ColumnLocation.exec_SQL_Statment);
        }

        // 刪除資料表
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS" + TABLE_TASK);
            db.execSQL("DROP TABLE IF EXISTS" + TABLE_ALERT);
            db.execSQL("DROP TABLE IF EXISTS" + TABLE_LOCATION);
            onCreate(db);
        }
    }
}
