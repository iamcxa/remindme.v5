package fud.geodoermap;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by dan on 2015/1/30.
 */
public class GeoInfo {
    public String name = "null";
    public LatLng latlng = new LatLng(-1,-1);

    /**
     * 能儲存地理位置資訊，預設值 name = "null"，latlng = -1,-1
     */
    public GeoInfo(){}

    /**
     * 能儲存地理位置資訊，預設值 name = "null"，latlng 為設定的值
     * @param latlng 設定經緯度
     */
    GeoInfo(LatLng latlng){
        this.latlng=latlng;
    }

    /**
     * 能儲存地理位置資訊，預設值 name 為設定的值，預設latlng = -1,-1
     * @param name 設定地址名稱
     */
    GeoInfo(String name){
        this.name=name;
    }

    /**　
     * 於地理位置設定地點名稱,如為空值,地點名稱為 "null"
     * @param str
     */
    public void setName(String str){
        if(!str.equals(""))
            this.name=str;
        else
            this.name="null";
    }

    /**
     * 於地理位置設定經緯度
     * @param latlng
     */
    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }

}
