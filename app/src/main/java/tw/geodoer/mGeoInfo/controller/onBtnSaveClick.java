package tw.geodoer.mGeoInfo.controller;

import fud.geodoermap.GeoInfo;
import tw.geodoer.main.taskEditor.fields.CommonEditorVar;

/**
 * Created by dan on 2015/4/14.
 */
public class OnBtnSaveClick
{

   private static CommonEditorVar mEditorVar ;

    public OnBtnSaveClick(GeoInfo geo) {
        this.mEditorVar = CommonEditorVar.GetInstance();
        mEditorVar.TaskLocation.setName(geo.name);
        mEditorVar.TaskLocation.setLat(geo.latlng.latitude);
        mEditorVar.TaskLocation.setLon(geo.latlng.longitude);



    }
}
