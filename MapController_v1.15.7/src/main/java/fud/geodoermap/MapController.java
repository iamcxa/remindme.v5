package fud.geodoermap;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by dan on 2015/1/30.
 */
public class MapController implements GoogleMap.OnMapClickListener,
                                      GoogleMap.OnCameraChangeListener,
                                      GoogleMap.OnMarkerClickListener,
                                      GeocodingAPI.onStatusLisitener{
    GoogleMap map;
    TextView showText;
    private Context context;
    GeoInfo geo;
    public boolean isMoveGet = true;
    private boolean isWifiOnly = false;

    private onGeoLoadLisitener l = null;

    public interface onGeoLoadLisitener {
        /**
         * 當會傳地理資訊的時候觸發
         * @param geo 地理資訊，包含name,latlng
         */
        public void onGeoLoaded(GeoInfo geo,int status);

        /**
         * 開始載入資料的時候觸發
         */
        public void onGeoLoading();

    }

    public void setOnGeoLoadedLisitener(onGeoLoadLisitener l){
        this.l = l;
    }


    /**
     *
     * @param context
     * @param map 給予GoogleMap的元件
     */
    public MapController(Context context,GoogleMap map){
        this.map = map;
        this.context = context;
    }

    /**
     *
     * @param context
     * @param map 給予GoogleMap的元件
     * @param showText 給予TextView的元件用於顯示
     */
    public MapController(Context context,GoogleMap map,TextView showText){
        this.map = map;
        this.showText = showText;
        this.context = context;
        this.map.setOnCameraChangeListener(this);
        this.map.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        map.clear();
        map.addMarker(new MarkerOptions().title("設定位置").position(latLng));
        l.onGeoLoading();
        geo = new GeoInfo(latLng);
        if(isInternetConnect()){
            GeocodingAPI a = new GeocodingAPI(context,latLng.latitude+","+latLng.longitude);
            a.setOnStatusLisitener(this);
            a.getGeocodingApiAddress(geo,showText);
        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        if(isMoveGet){
            map.clear();
            map.addMarker(new MarkerOptions().title("設定位置").position(new LatLng(cameraPosition.target.latitude,cameraPosition.target.longitude)));
            l.onGeoLoading();
            geo = new GeoInfo(new LatLng(cameraPosition.target.latitude,cameraPosition.target.longitude));
            if(isInternetConnect()){
                GeocodingAPI a = new GeocodingAPI(context,cameraPosition.target.latitude+","+cameraPosition.target.longitude);
                a.setOnStatusLisitener(this);
                a.getGeocodingApiAddress(geo,showText);
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return true;
    }

    /**
     * 設定一個EditText元件,那上面的字串值來搜尋
     * @param setText 給予EditText元件
     */
    public void searchPlace(EditText setText){
        l.onGeoLoading();
        geo = new GeoInfo(setText.getText().toString());
        if(isInternetConnect()){
            GeocodingAPI a = new GeocodingAPI(context,setText.getText().toString());
            a.setOnStatusLisitener(this);
            a.getGeocodingApiLatLng(map,geo);
        }
    }

    /**
     * 設定一個字串,拿來做搜尋,也可使用經緯度如"123.456,888.999"
     * @param setText
     */
    public void searchPlace(String setText){
        l.onGeoLoading();
        geo = new GeoInfo(setText);
        if(isInternetConnect()){
            GeocodingAPI a = new GeocodingAPI(context,setText);
            a.setOnStatusLisitener(this);
            a.getGeocodingApiLatLng(map,geo);
        }
    }

    /**
     * 設定是否要開啟移動為自動抓取位置
     * @param a 傳入false為關閉移動為自動抓取位置，預設true
     */
    public void isMoveGet(boolean a){
        this.isMoveGet = a;
    }

    /**
     * 回傳當前網路狀態
     * @return 有網路回傳True,沒有網路回傳false
     */
    private boolean isInternetConnect(){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if(info!=null && info.isConnected()){
            Log.d("wifi種類",info.getTypeName());
            if(isWifiOnly){
                if(info.getType()==ConnectivityManager.TYPE_WIFI){
                    return true;
                }else{
//                    Toast.makeText(context,"沒有Wifi",Toast.LENGTH_SHORT).show();
                    l.onGeoLoaded(this.geo, GeoStatus.WIFI_FAIL);
                    return false;
                }
            }else{
                return true;
            }
        }else{
//            Toast.makeText(context,"沒有網路",Toast.LENGTH_SHORT).show();
            l.onGeoLoaded(this.geo, GeoStatus.NETWORK_FAIL);
            return false;
        }
    }

    /**
     * 設定是否只使用wifi獲取位置
     * @param a 設定true只使用wifi獲取位置，預設false
     */
    public void isWifiOnly(boolean a){
        isWifiOnly = a;
    }

    @Override
    public void onStatus(boolean status) {
        if(status){
            if(!this.geo.name.equals("null") || (this.geo.latlng.longitude!=-1 && this.geo.latlng.latitude!=-1))
                l.onGeoLoaded(this.geo, GeoStatus.SUCESS);
        }
    }
}
