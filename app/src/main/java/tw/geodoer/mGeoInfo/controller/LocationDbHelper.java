package tw.geodoer.mGeoInfo.controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocationDbHelper extends SQLiteOpenHelper {
    public final static String _TableName = "PlaceJson";
    private final static int _DBVersion = 1;
    private final static String _DBName = "LocationInfo.db";

    public LocationDbHelper(Context context) {
        super(context, _DBName, null, _DBVersion);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        final String SQL = "CREATE TABLE IF NOT EXISTS " + _TableName + "( " +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "_lat DOUBLE, " +
                "_lng DOUBLE, " +
                "_keyword TEXT, " +
                "_type TEXT, " +
                "_ApiJson TEXT" +
                ");";
        db.execSQL(SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

}
