package tw.geodoer.mGeoInfo.view;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import fud.geodoermap.GeoInfo;
import fud.geodoermap.GeoStatus;
import fud.geodoermap.MapController;
import tw.geodoer.main.taskEditor.fields.CommonEditorVar;
import tw.moretion.geodoer.R;

/**
 * This is a custom dialog class that will hold a tab view with 2 tabs.
 * Tab 1 will be a list view. Tab 2 will be a list view.
 */
public class LocationCustomDialog extends DialogFragment implements MapController.onGeoLoadLisitener,
        View.OnClickListener{
    private static CommonEditorVar mEditorVar = CommonEditorVar.GetInstance();

    /**
     * Default constructor.
     *
     * @param context
     */
    private static GoogleMap map;
    private static MapView mapView;
    private static TextView PlaceName;
    private static EditText SearchText;
    private static Button Search;
    private static Button save;
    private View mContentView;
    private String locationName;
    private double Lat;
    private double Lon;
    MapController mapController;


    public LocationCustomDialog newInstance() {
        return new LocationCustomDialog();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContentView = inflater.inflate(R.layout.activity_task_editor_tab_location, container, false);
        PlaceName = (TextView) mContentView.findViewById(R.id.PlaceName);
        SearchText = (EditText) mContentView.findViewById(R.id.SearchText);
        Search = (Button) mContentView.findViewById(R.id.Search);
        Search.setOnClickListener(this);

        save = (Button) mContentView.findViewById(R.id.save);
        save.setOnClickListener(this);

        MapsInitializer.initialize(getActivity().getApplicationContext());

        switch (GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity())) {
            case ConnectionResult.SUCCESS:
//	                 Toast.makeText(getActivity(), "SUCCESS", Toast.LENGTH_SHORT).show();
                mapView = (MapView) mContentView.findViewById(R.id.map);
                mapView.onCreate(savedInstanceState);
                if (mapView != null) {
                    mapView.onResume();
                    setUpMapIfNeeded();
                }
                break;
            case ConnectionResult.SERVICE_MISSING:
//	                 Toast.makeText(getActivity(), "SERVICE MISSING", Toast.LENGTH_SHORT).show();
                break;
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
//	                 Toast.makeText(getActivity(), "UPDATE REQUIRED", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(getActivity(), GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity()), Toast.LENGTH_SHORT).show();
        }
        return mContentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }

    private void setUpMapIfNeeded() {
        map = mapView.getMap();
        if (map == null) {
            Log.d("", "googleMap is null !!!!!!!!!!!!!!!");
        } else {
            map.setMyLocationEnabled(true);
            map.getUiSettings().setZoomControlsEnabled(false);
            map.setMyLocationEnabled(true);
            LatLng nowLoacation;
            nowLoacation = new LatLng(23.6978, 120.961);
            map.moveCamera((CameraUpdateFactory.newLatLngZoom(nowLoacation,
                    map.getMinZoomLevel() + 7)));
            map.addMarker(new MarkerOptions().title("當前位置").draggable(true)
                    .position(nowLoacation));
//            map.get
            mapController = new MapController(getActivity(),map,PlaceName);
            mapController.isMoveGet(true);
            mapController.setOnGeoLoadedLisitener(this);
        }
    }


    /**
     * This is called when a long press occurs on our listView02 items.
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderTitle("Context Menu");
        menu.add(0, v.getId(), 0, "Delete");
    }

    /**
     * This is called when an item in our context menu is clicked.
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Delete") {

        } else {
            return false;
        }

        return true;
    }

    /**
     * 當會傳地理資訊的時候觸發
     *
     * @param geo    地理資訊，包含name,latlng
     * @param status
     */
    @Override
    public void onGeoLoaded(GeoInfo geo, int status) {
        if(status == GeoStatus.NETWORK_FAIL){
            save.setEnabled(true);
            Log.e("fail","位置："+geo.name+",座標："+geo.latlng+",沒有網路");
        }else if(status == GeoStatus.WIFI_FAIL){
            save.setEnabled(true);
            Log.e("fail","位置："+geo.name+",座標："+geo.latlng+",只有3G,沒有wifi");
        }else if(status == GeoStatus.SUCESS){
            save.setEnabled(true);
            Log.d("Loaded","位置："+geo.name+",座標："+geo.latlng+",載入完成");
        }
    }

    /**
     * 開始載入資料的時候觸發
     */
    @Override
    public void onGeoLoading() {
        save.setEnabled(false);
        Log.d("Loading","開始載入");
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.Search){
            mapController.searchPlace(SearchText.getText().toString());
        }
        else if(v.getId() == R.id.save){

        }
    }
}