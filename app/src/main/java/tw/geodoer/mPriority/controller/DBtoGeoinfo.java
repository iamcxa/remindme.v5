package tw.geodoer.mPriority.controller;

import android.content.Context;
import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;
import fud.geodoermap.GeoInfo;
import tw.geodoer.mDatabase.API.DBLocationHelper;
import tw.geodoer.mDatabase.API.DBTasksHelper;
import tw.geodoer.mDatabase.columns.ColumnLocation;
import tw.geodoer.mDatabase.columns.ColumnTask;

/**
 * Created by MurasakiYoru on 2015/4/16.
 */
public class DBtoGeoInfo
{
    private Context mContext;
    private DBTasksHelper mDBT;
    private DBLocationHelper mDBL;
    public DBtoGeoInfo(Context context)
    {
        this.mContext = context;
        this.mDBT = new DBTasksHelper(mContext);
        this.mDBL = new DBLocationHelper(mContext);
    }


    public ArrayList<GeoInfo> getArraylistGeoInfoofTasks() //null for no id
    {
        ArrayList<Integer> ids = mDBT.getIDArrayListOfTask();
        if(ids == null) return  null;

        int locid;
        ArrayList<GeoInfo> array_geo = new ArrayList<GeoInfo>();
        array_geo.clear();
        for(int id : ids)
        {
            GeoInfo gi = new GeoInfo();
            gi.setName(mDBT.getItemString(id, ColumnTask.KEY.title));

            locid = mDBT.getItemInt(id, ColumnTask.KEY.location_id);
            LatLng lalo = new LatLng(mDBL.getItemDouble(locid,ColumnLocation.KEY.lat),
                                     mDBL.getItemDouble(locid,ColumnLocation.KEY.lon));
            array_geo.add(gi);
        }
        return array_geo;
    }
    /** Example
     *  DBtoGeoInfo DBt = new DBtoGeoInfo(Context);
     *  ArrayList<GeoInfo>  obj = DBt.getArraylistGeoInfoofTasks();
     *  //null for no id in
     */
}
