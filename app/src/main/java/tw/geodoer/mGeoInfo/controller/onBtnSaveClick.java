package tw.geodoer.mGeoInfo.controller;

import android.content.ContentValues;
import android.content.Context;

import fud.geodoermap.GeoInfo;
import tw.geodoer.mDatabase.columns.ColumnLocation;
import tw.geodoer.mPriority.controller.NeoGeoInfo;
import tw.geodoer.main.taskEditor.fields.CommonEditorVar;
import tw.geodoer.main.taskEditor.view.TaskEditorMainFragment;

/**
 * Created by dan on 2015/4/14.
 */
public class onBtnSaveClick
{

   private static CommonEditorVar mEditorVar ;

    public onBtnSaveClick(NeoGeoInfo geo,Context context) {

//        this.mEditorVar = CommonEditorVar.GetInstance();
//        mEditorVar.TaskLocation.setName(geo.name);
//        mEditorVar.TaskLocation.setLat(geo.latlng.latitude);
//        mEditorVar.TaskLocation.setLon(geo.latlng.longitude);

        ContentValues values =new ContentValues();
        values.clear();
        values.put(ColumnLocation.KEY.name, geo.name);
        //-------------------------------------------//
        // 改成address
        values.put(ColumnLocation.KEY.address, geo.name);
        //-------------------------------------------//
        values.put(ColumnLocation.KEY.lat, geo.latlng.latitude);
        values.put(ColumnLocation.KEY.lon, geo.latlng.longitude);
        values.put(ColumnLocation.KEY.lastUsedTime, System.currentTimeMillis());
        context.getContentResolver().insert(ColumnLocation.URI, values);


        TaskEditorMainFragment.calledByDialog=true;
    }
}
