package tw.geodoer.mGeoInfo.controller;

import android.content.Context;

import fud.geodoermap.GeoInfo;
import tw.geodoer.mGeoInfo.API.CurrentLocation;
import tw.geodoer.main.taskEditor.fields.CommonEditorVar;

/**
 * Created by dan on 2015/4/14.
 */
public class onBtnSaveClick
{

   private static CommonEditorVar mEditorVar ;

    public onBtnSaveClick(GeoInfo geo) {
        this.mEditorVar = CommonEditorVar.GetInstance();
        mEditorVar.TaskLocation.setName(geo.name);
        mEditorVar.TaskLocation.setLat(geo.latlng.latitude);
        mEditorVar.TaskLocation.setLon(geo.latlng.longitude);

    }
}
