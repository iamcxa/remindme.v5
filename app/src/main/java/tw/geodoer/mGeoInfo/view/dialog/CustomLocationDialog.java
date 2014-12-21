package tw.geodoer.mGeoInfo.view.dialog;
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
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import me.iamcxa.remindme.R;
import tw.geodoer.mGeoInfo.controller.GeocodingAPI;
import tw.geodoer.main.taskEditor.CommonEditorVar;

/**
 * This is a custom dialog class that will hold a tab view with 2 tabs.
 * Tab 1 will be a list view. Tab 2 will be a list view.
 * 
 */
public class CustomLocationDialog extends DialogFragment implements
	OnMarkerClickListener,
	OnInfoWindowClickListener,
	OnMarkerDragListener
{
	private static CommonEditorVar mEditorVar=CommonEditorVar.GetInstance();

	/**
	 * Default constructor.
	 * 
	 * @param context
	 */
	private static GoogleMap map;
	private static MapView mapView;
	private View mContentView;
	private static TextView PlaceName;
	private static EditText SearchText;
	private static Button Search;
	
	private String locationName;
	private double Lat;
	private double Lon;
	
	public CustomLocationDialog newInstance() {
		return new CustomLocationDialog();
		
    }
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	    }

	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	    	getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
	    	mContentView= inflater.inflate(R.layout.activity_task_editor_tab_location, container, false);
	    	PlaceName =(TextView)mContentView.findViewById(R.id.PlaceName);
	    	SearchText = (EditText) mContentView.findViewById(R.id.SearchText);
	    	Search = (Button) mContentView.findViewById(R.id.Search);
			Search.setOnClickListener(SearchPlace);
	    	 MapsInitializer.initialize(getActivity());
	    	 
	    	 switch (GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity()) )
	         {
	             case ConnectionResult.SUCCESS:
//	                 Toast.makeText(getActivity(), "SUCCESS", Toast.LENGTH_SHORT).show();
	                 mapView = (MapView) mContentView.findViewById(R.id.map);
	                 mapView.onCreate(savedInstanceState);
	                 // Gets to GoogleMap from the MapView and does initialization stuff
	                 if(mapView!=null)
	                 {
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
	             default: Toast.makeText(getActivity(), GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity()), Toast.LENGTH_SHORT).show();
	         }

//    	     map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
	    	

	        return mContentView;
	    }
	     
	@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onActivityCreated(savedInstanceState);
//			PlaceName = (TextView) mContentView.findViewById(R.id.PlaceName);
		}

	private void setUpMapIfNeeded()
	{
//	    if(map == null)
//	    {
	    	map = mapView.getMap();
	        if(map == null)
	        {
	            Log.d("", "googleMap is null !!!!!!!!!!!!!!!");
	        }else
	        {
	        	map.setMyLocationEnabled(true);
	        	map.getUiSettings().setZoomControlsEnabled(false);
	        	map.setMyLocationEnabled(true);
	        	map.setOnCameraChangeListener(listener);
	        	map.setOnMapClickListener(mapclickListener);
//	        	map.setOnMarkerClickListener(MarkerClickListener);
	    		LatLng nowLoacation;
//	    		if (gpsManager.LastLocation() != null) {
//	    			nowLoacation = new LatLng(gpsManager.LastLocation().getLatitude(),
//	    					gpsManager.LastLocation().getLongitude());
//	    			map.moveCamera((CameraUpdateFactory.newLatLngZoom(nowLoacation,
//	    					map.getMaxZoomLevel() - 4)));
//	    		} else {
	    			nowLoacation = new LatLng(23.6978, 120.961);
	    			map.moveCamera((CameraUpdateFactory.newLatLngZoom(nowLoacation,
	    					map.getMinZoomLevel() + 7)));
//	    		}
	    		map.addMarker(new MarkerOptions().title("當前位置").draggable(true)
	    				.position(nowLoacation));
	        }
//	    }
	}

	private GoogleMap.OnCameraChangeListener listener = new GoogleMap.OnCameraChangeListener() {
		
		@Override
		public void onCameraChange(CameraPosition position) {
			// TODO Auto-generated method stub
//			map.clear();
//			LatLng now = new LatLng(position.target.latitude, position.target.longitude);
//			map.addMarker(new MarkerOptions()
//            .title("目的地")
//            .position(now));
		}
	};
	
	private GoogleMap.OnMapClickListener mapclickListener = new GoogleMap.OnMapClickListener() {
		
		@Override
		public void onMapClick(LatLng point) {
			// TODO Auto-generated method stub
			map.clear();
			LatLng now = new LatLng(point.latitude, point.longitude);
			map.addMarker(new MarkerOptions()
            .title("目的地")
            .position(now)
            .draggable(true));
		}
	};
	
	private Button.OnClickListener SearchPlace = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==R.id.Search)
				SearchPlace();
		}
		
	};
	private void SearchPlace() {
		if (!SearchText.getText().toString().equals("")) {
			GeocodingAPI LoacationAddress2 = null;
			LatLng SearchLocation = null;
			LoacationAddress2 = new GeocodingAPI(getActivity(),
					SearchText.getText().toString());
			// textView2.setText("");
			// locationName=LoacationAddress2.GeocodingApiAddressGet();
			// textView2.setText(textView2.getText()+"\n"+Address);
			SearchLocation = LoacationAddress2.GeocodingApiLatLngGet();
			Lat=SearchLocation.latitude;
			Lon=SearchLocation.longitude;
			// textView2.setText(textView2.getText()+"\n"+SearchLocation);
			locationName=LoacationAddress2.GeocodingApiAddressGet();
			PlaceName.setText(locationName);
			if (SearchLocation != null) {
				map.animateCamera((CameraUpdateFactory.newLatLngZoom(
						SearchLocation, map.getMaxZoomLevel() - 4)));
				map.addMarker(new MarkerOptions().title("搜尋的位置")
						.snippet(locationName).position(SearchLocation));
			} else {
				Toast.makeText(getActivity(), "查無地點哦,換個詞試試看",
						Toast.LENGTH_SHORT).show();
			}
		}
	}
	/**
	 * This is called when a long press occurs on our listView02 items.
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, v, menuInfo);

		menu.setHeaderTitle("Context Menu");  
		menu.add(0, v.getId(), 0, "Delete");  
	}

	/**
	 * This is called when an item in our context menu is clicked.
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{  
		if(item.getTitle() == "Delete")
		{

		}  
		else
		{
			return false;
		}

		return true;  
	}

	@Override
	public void onMarkerDrag(Marker marker) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMarkerDragEnd(Marker marker) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMarkerDragStart(Marker marker) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub
		return false;
	}  
}