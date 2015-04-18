package fud.geodoermap;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GeodoerMapActivity extends ActionBarActivity implements View.OnClickListener,MapController.onGeoLoadLisitener{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Toolbar toolBar;

    TextView show;
    EditText searchText;
    Button button;
    MapController mapController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geodoer_map);

        show = (TextView)findViewById(R.id.textView2);
        searchText =(EditText)findViewById(R.id.editText);
        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(this);

        setUpMapIfNeeded();
        toolBar = (Toolbar) findViewById(R.id.toolbar);
        if (toolBar != null) {
            setSupportActionBar(toolBar);
        }

        toolBar.setNavigationContentDescription(R.drawable.ic_launcher);
        toolBar.setTitle("ToolBar Title");//设置标题
        toolBar.setSubtitle("This is subtitle");//设置子标题
        toolBar.setTitleTextColor(Color.parseColor("#ff0000"));//设置标题颜色
        toolBar.setLogo(R.drawable.ic_launcher);//设置logo图片
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();

            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        LatLng nowLocation=new LatLng(23.6978, 120.961);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.setMyLocationEnabled(true);
        mMap.addMarker(new MarkerOptions().title("當前位置").draggable(true).position(nowLocation));

        mapController = new MapController(getApplicationContext(),mMap,show);
        mapController.isMoveGet(true);
        mapController.isWifiOnly(true);
        mapController.setOnGeoLoadedLisitener(this);

    }

    @Override
    public void onClick(View v) {
        mapController.searchPlace(searchText);
    }

    /**
     * 當會傳地理資訊的時候觸發
     *
     * @param geo 地理資訊，包含name,latlng
     */
    @Override
    public void onGeoLoaded(GeoInfo geo,int status) {
        if(status==GeoStatus.NETWORK_FAIL){
            Log.e("fail","位置："+geo.name+",座標："+geo.latlng+",沒有網路");
        }else if(status==GeoStatus.WIFI_FAIL){
            Log.e("fail","位置："+geo.name+",座標："+geo.latlng+",只有3G,沒有wifi");
        }else if(status==GeoStatus.SUCESS){
            Log.d("Loaded","位置："+geo.name+",座標："+geo.latlng+",載入完成");
        }
    }

    /**
     * 開始載入資料的時候觸發
     */
    @Override
    public void onGeoLoading() {
        Log.d("Loading","開始載入");
    }
}
