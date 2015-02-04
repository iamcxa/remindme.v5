package tw.geodoer.mGeoInfo.controller;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GeocodingAPIJsonDecode {
    String Json;
    Context context;
    JSONObject j;

    GeocodingAPIJsonDecode(Context context, String Json) {
        this.Json = Json;
        this.context = context;
        try {
            j = new JSONObject(Json);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("JsonPaser錯誤", e.toString());
        }
    }

    public String GetAddress() {
        String Address = "";
        try {
            if (j.get("status").toString().equals("OK")) {
                JSONArray PlaceArray = j.getJSONArray("results");
                Object address = PlaceArray.getJSONObject(0).get("formatted_address");
                Address = address.toString();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Address;
    }


    public LatLng GetLatLng() {
        LatLng LatLng = null;
        try {
            if (j.get("status").toString().equals("OK")) {
                JSONArray PlaceArray = j.getJSONArray("results");
                Object lat2 = PlaceArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lat");
                Object lng2 = PlaceArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lng");
                LatLng = new LatLng((Double) lat2, (Double) lng2);
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return LatLng;
    }


}
