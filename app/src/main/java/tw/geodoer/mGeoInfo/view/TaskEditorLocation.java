package tw.geodoer.mGeoInfo.view;

import tw.geodoer.main.taskEditor.fields.CommonEditorVar;
import tw.geodoer.utils.MyDebug;

import com.devspark.progressfragment.ProgressFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import tw.geodoer.mGeoInfo.controller.GeocodingAPI;
import tw.moretion.geodoer.R;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TaskEditorLocation extends ProgressFragment  /*implements GPSCallback*/ {


	// 宣告pick
	private static GoogleMap map;
	private static EditText SearchText;
	private static TextView PlaceName;
	private static Button Search;
	
	private Handler GpsTimehandler = new Handler();
	private static CommonEditorVar mEditorVar=CommonEditorVar.GetInstance();
	private Handler mHandler;
	private View mContentView;

	public static TaskEditorLocation newInstance() {
		TaskEditorLocation fragment = new TaskEditorLocation();
		return fragment;
	}	
	
	private Runnable mShowContentRunnable = new Runnable() {

		@Override
		public void run() {
			setContentShown(true);
			setUpMapIfNeeded();
		}

	};

	//****************搜尋到的經緯度、名稱********************
	
	private String locationName;
	private double Lat;
	private double Lon;
	
	//************************************************
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
	
	
		 mContentView =inflater.inflate(R.layout.activity_task_editor_tab_location, container, false);
		

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//setContentShown(false);
//		SearchText = (EditText) getActivity().findViewById(R.id.SearchText);
//		Search = (Button) getActivity().findViewById(R.id.Search);
//		Search.setOnClickListener(new Button.OnClickListener(){ 
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//            	SearchPlace();
//            }         
//
//        });
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//setUpMapIfNeeded();
		//setContentEmpty(false);
		//setContentShown(true);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setContentView(mContentView);
		
		//OK = (ImageButton) getActivity().findViewById(R.id.OK);
		
		SearchText = (EditText) getContentView().findViewById(R.id.SearchText);
		Search = (Button) getContentView().findViewById(R.id.Search);
		Search.setOnClickListener(SearchPlace);
		PlaceName = (TextView)getContentView().findViewById(R.id.PlaceName);
		
//		OK.setOnClickListener(SearchPlace);
//		gpsManager = new GPSManager();
//		gpsManager.startNetWorkListening(getActivity());
//		gpsManager.setGPSCallback(TaskEditorLocation.this);
//		map = ((MapFragment) getActivity().getFragmentManager()
//				 .findFragmentById(R.id.map)).getMap();
//		map= ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		
		obtainData();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mHandler.removeCallbacks(mShowContentRunnable);
	}

	
	private void obtainData() {
		// Show indeterminate progress
		setContentShown(false);

		mHandler = new Handler();
		mHandler.postDelayed(mShowContentRunnable, 400);

		MyDebug.MakeLog(0, "RemindmeFragment mHandler");
	}
	
	private void setUpMapIfNeeded()
	{
	    if(map == null)
	    {
	    	//map = ((MapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
	    	 if(map == null)
	        {
	            Log.d("", "googleMap is null !!!!!!!!!!!!!!!");
	        }else
	        {
	        	map.setMyLocationEnabled(true);
	        	map.getUiSettings().setZoomControlsEnabled(false);
	        	map.setOnCameraChangeListener(listener);
	        	map.setOnMarkerClickListener(MarkerClickListener);
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
//	    		map.addMarker(new MarkerOptions().title("當前位置").draggable(true)
//	    				.position(nowLoacation));
	        }
	    }
	}

	
	private GoogleMap.OnCameraChangeListener listener = new GoogleMap.OnCameraChangeListener() {
		
		@Override
		public void onCameraChange(CameraPosition position) {
			// TODO Auto-generated method stub
			map.clear();
			LatLng now = new LatLng(position.target.latitude, position.target.longitude);
			map.addMarker(new MarkerOptions()
            .title("目的地")
            .position(now));
		}
	};
	
	private GoogleMap.OnMarkerClickListener MarkerClickListener =new GoogleMap.OnMarkerClickListener() {
		
		@Override
		public boolean onMarkerClick(Marker marker) {
			// TODO Auto-generated method stub
			GeocodingAPI LoacationAddress = new GeocodingAPI(getActivity(),marker.getPosition().latitude+","+marker.getPosition().longitude);
			
			locationName=LoacationAddress.GeocodingApiAddressGet();
			PlaceName.setText(locationName);
			Lat=marker.getPosition().latitude;
			Lon=marker.getPosition().longitude;
			return false;
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

//	@Override
//	public void onGPSUpdate(Location location) {
//		// TODO Auto-generated method stub
//		Double Longitude = location.getLongitude();
//		// 緯度
//		Double Latitude = location.getLatitude();
//
//		// textView1.setText("經緯度:"+Latitude+","+Longitude);
//		// 拿到經緯度後馬上關閉
////		 Toast.makeText(getActivity(), "關閉GPS"+location,
////		 Toast.LENGTH_LONG).show();
//
//		if (RemindmeVar.GpsSetting.GpsStatus) {
//			RemindmeVar.GpsSetting.GpsStatus = false;
//			gpsManager.stopListening();
//			gpsManager.setGPSCallback(null);
//			gpsManager = null;
//		} else {
//			RemindmeVar.GpsSetting.GpsStatus = false;
//		}
//		LatLng nowLoacation = new LatLng(Latitude, Longitude);
//
//		map.setMyLocationEnabled(true);
//
//		map.clear();
//
//		map.animateCamera((CameraUpdateFactory.newLatLngZoom(nowLoacation,
//				map.getMaxZoomLevel() - 4)));
//
//		map.addMarker(new MarkerOptions().title("目前位置").position(nowLoacation));
//
//		// GeocodingAPI LoacationAddress = new
//		// GeocodingAPI(getApplicationContext(),Latitude+","+Longitude);
//		// textView2.setText(textView2.getText()+" "+LoacationAddress.GeocodingApiAddressGet());
//	}
//
//		private Runnable GpsTime = new Runnable() {
//			@Override
//			public void run() {
//				mEditorVar.GpsUseTime++;
//				// Timeout Sec, 超過TIMEOUT設定時間後,直接設定FLAG使得getCurrentLocation抓取
//				// lastlocation.
//				if (mEditorVar.GpsUseTime > RemindmeVar.GpsSetting.TIMEOUT_SEC) {
//					if (RemindmeVar.GpsSetting.GpsStatus) {
//						gpsManager.stopListening();
//						gpsManager.startNetWorkListening(getApplicationContext());
//						RemindmeVar.GpsSetting.GpsStatus = true;
//						// Toast.makeText(getApplicationContext(), "關閉GPS",
//						// Toast.LENGTH_LONG).show();
//					}
//				} else {
//					GpsTimehandler.postDelayed(this, 1000);
//				}
//			}
//		};
//
}

