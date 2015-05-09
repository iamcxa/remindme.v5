package tw.geodoer.mGeoInfo.view;

import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.geodoer.geotodo.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;

import fud.geodoermap.GeoInfo;
import fud.geodoermap.GeoStatus;
import fud.geodoermap.MapController;
import tw.geodoer.mGeoInfo.API.CurrentLocation;
import tw.geodoer.mGeoInfo.controller.PlaceAutocompleteAdapter;
import tw.geodoer.mGeoInfo.controller.onBtnSaveClick;
import tw.geodoer.mPriority.controller.NeoGeoInfo;
import tw.geodoer.main.taskEditor.fields.CommonEditorVar;

/**
 * This is a custom dialog class that will hold a tab view with 2 tabs.
 * Tab 1 will be a list view. Tab 2 will be a list view.
 */
public class LocationCustomDialog extends DialogFragment implements MapController.onGeoLoadLisitener,
        View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener{
    private static CommonEditorVar mEditorVar = CommonEditorVar.GetInstance();
    private static  ObservableScrollView mScrollView;

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
    private GeoInfo geo;

    //****自動補齊地點*****
    protected GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mAdapter;
    private AutoCompleteTextView mAutocompleteView;
    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));
    //********************


    public LocationCustomDialog newInstance() {
        return new LocationCustomDialog();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(true);
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContentView = inflater.inflate(R.layout.activity_task_editor_tab_location, container, false);


        mScrollView =
                (ObservableScrollView) mContentView.findViewById(R.id.scrollViewMap);
        // faBtn
        final FloatingActionButton faBtn_add
                = (FloatingActionButton) mContentView.findViewById(R.id.faBtn_nowLoc);
        faBtn_add.attachToScrollView(mScrollView);
        faBtn_add.setType(FloatingActionButton.TYPE_MINI);
        faBtn_add.setColorNormalResId(R.color.card_background_white);
        faBtn_add.setColorPressedResId(R.color.card_background_color_red_light);
        faBtn_add.setColorRipple(R.color.card_backgroundExpand);
        faBtn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.clear();
                CurrentLocation mNowGeo = new CurrentLocation(getActivity());
                mNowGeo.setOnLocListenerSetGps("-1", new CurrentLocation.onDistanceListener() {
                    @Override
                    public void onGetLatLng(Double lat, Double lng) {
                        LatLng nowLoacation;
                        nowLoacation = new LatLng(lat, lng);
                        map.addMarker(new MarkerOptions().title("當前位置").draggable(true)
                                .position(nowLoacation)).showInfoWindow();
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(nowLoacation,
                                map.getCameraPosition().zoom));
                    }
                });
            }
        });

        //****自動補齊地點*****
        mAutocompleteView = (AutoCompleteTextView)mContentView.findViewById(R.id.autocomplete_places);
        mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);
        mAdapter = new PlaceAutocompleteAdapter(getActivity(), android.R.layout.simple_list_item_1,
                mGoogleApiClient, BOUNDS_GREATER_SYDNEY,null);
        mAutocompleteView.setAdapter(mAdapter);
        //*********************

        PlaceName = (TextView) mContentView.findViewById(R.id.PlaceName);
//        SearchText = (EditText) mContentView.findViewById(R.id.SearchText);
//        Search = (Button) mContentView.findViewById(R.id.Search);
//        Search.setOnClickListener(this);

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
//            map gps不會停止Bug使用自幹的GPS成
//            map.setMyLocationEnabled(true);
            map.getUiSettings().setZoomControlsEnabled(true);
            CurrentLocation mNowGeo = new CurrentLocation(getActivity());
            mNowGeo.setOnLocListenerSetGps("-1", new CurrentLocation.onDistanceListener() {
                @Override
                public void onGetLatLng(Double lat, Double lng) {
                    LatLng nowLoacation;
                    nowLoacation = new LatLng(lat, lng);
                    map.addMarker(new MarkerOptions().title("當前位置").draggable(true)
                            .position(nowLoacation)).showInfoWindow();
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(nowLoacation,
                            map.getMaxZoomLevel() - 8));
                }
            });

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
        this.geo = geo;
    }

    /**
     * 開始載入資料的時候觸發
     */
    @Override
    public void onGeoLoading() {
        save.setEnabled(false);
        Log.d("Loading","開始載入");
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mGoogleApiClient.disconnect();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
//        if(v.getId() == R.id.Search){
////            mapController.searchPlace(SearchText.getText().toString());
//        }
//        else
        if(v.getId() == R.id.save){
            NeoGeoInfo saveGeo = new NeoGeoInfo();
            saveGeo.setName(geo.name);
            saveGeo.setLatlng(geo.latlng);


            onBtnSaveClick a = new onBtnSaveClick(saveGeo,getActivity().getApplicationContext());
//            OnBtnSaveClick.saveDB(geo);
            Toast.makeText(getActivity(),geo.name,Toast.LENGTH_SHORT).show();
            getDialog().dismiss();
//            CurrentLocation b = new CurrentLocation(getActivity());
//            b.setOnDistanceListener(geo.latlng.latitude,geo.latlng.longitude,new CurrentLocation.onDistanceListener() {
//                @Override
//                public void onGetDistance(Double mDistance) {
//                    Toast.makeText(getActivity(),mDistance+"",Toast.LENGTH_SHORT).show();
//                }
//            });
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(getActivity(),
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
        mAdapter.setGoogleApiClient(null);
    }


    /**
     * Listener that handles selections from suggestions from the AutoCompleteTextView that
     * displays Place suggestions.
     * Gets the place id of the selected item and issues a request to the Places Geo Data API
     * to retrieve more details about the place.
     *
     * @see com.google.android.gms.location.places.GeoDataApi#getPlaceById(com.google.android.gms.common.api.GoogleApiClient,
     * String...)
     */
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a PlaceAutocomplete object from which we
             read the place ID.
              */
            final PlaceAutocompleteAdapter.PlaceAutocomplete item = mAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
              details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     */
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(),
                    map.getMaxZoomLevel() - 8));
            // Format details of the place for display and show it in a TextView.
//            mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(),
//                    place.getId(), place.getAddress(), place.getPhoneNumber(),
//                    place.getWebsiteUri()));

//            // Display the third party attributions if set.
//            final CharSequence thirdPartyAttribution = places.getAttributions();
//            if (thirdPartyAttribution == null) {
//                mPlaceDetailsAttribution.setVisibility(View.GONE);
//            } else {
//                mPlaceDetailsAttribution.setVisibility(View.VISIBLE);
//                mPlaceDetailsAttribution.setText(Html.fromHtml(thirdPartyAttribution.toString()));
//            }

//            Log.i(TAG, "Place details received: " + place.getName());

            places.release();
        }
    };
}