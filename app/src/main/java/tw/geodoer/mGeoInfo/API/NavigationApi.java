package tw.geodoer.mGeoInfo.API;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;

/**
 * Created by fud on 2015/4/19.
 */
public class NavigationApi {
    String url = "http://router.project-osrm.org/viaroute?";
    int z = 1;
    String output = "json";
    String instructions = "false";
    LatLng loc ;
    LatLng loc2 ;
    String hint = "1";
    String checksum = "1";
    public String getUrl;

    public NavigationApi(LatLng loc, LatLng loc2) {
        this.loc = loc;
        this.loc2 = loc2;
        getUrl =  url;
        getUrl += "z=" + z + "&";
        getUrl += "output=" + output + "&";
        getUrl += "instructions=" + instructions + "&";
        getUrl += "loc=" + loc.latitude + "," + loc.longitude + "&";
        getUrl += "loc=" + loc2.latitude + "," + loc2.longitude + "&";
        getUrl += "hint=" + hint + "&";
        getUrl += "checksum=" + checksum;
        Log.e("OpenMapApi", getUrl);
    }



    public DisInfo jsonDecode(JsonObject result){
        DisInfo info = new DisInfo();
        if(result.get("status").getAsInt()==0){
            JsonObject infoJson = result.get("route_summary").getAsJsonObject();
            info.setTime_S(infoJson.get("total_time").getAsInt());
            info.setDis_M(infoJson.get("total_distance").getAsInt());
        }
        return info;
    }

}

//Example

//    final NavigationApi navApi = new NavigationApi(lat1,lng1,lat2,lng2);
//    Ion.with(context)
//        .load(navApi.getUrl)
//        .asJsonObject()
//        .setCallback(new FutureCallback<JsonObject>() {
//            @Override
//            public void onCompleted(Exception e, JsonObject result) {
//                DisInfo info = navApi.jsonDecode(result);
//                Log.e("OpenMapdejson",info.time_S+","+info.dis_M);
//            }
//    });

