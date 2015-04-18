package tw.geodoer.mGeoInfo.view;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.geodoer.geotodo.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;

import java.util.ArrayList;

import fud.geodoermap.GeoInfo;
import fud.geodoermap.MapController;
import tw.geodoer.mGeoInfo.API.CurrentLocation;
import tw.geodoer.mPriority.controller.DBtoGeoinfo;
import tw.geodoer.mPriority.controller.NeoGeoInfo;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShowTodoGeoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShowTodoGeoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowTodoGeoFragment extends Fragment implements MapController.onGeoLoadLisitener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private static GoogleMap map;
    private static MapView mapView;

    private static TextView PlaceName;
    MapController mapController;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShowTodoGeo.
     */
    // TODO: Rename and change types and number of parameters
    public static ShowTodoGeoFragment newInstance(String param1, String param2) {
        ShowTodoGeoFragment fragment = new ShowTodoGeoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ShowTodoGeoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =
                inflater.inflate(R.layout.activity_task_editor_parts_dialog_location,
                                container, false);

       ObservableScrollView mScrollView =
                (ObservableScrollView) v.findViewById(R.id.scrollViewMap);

        if(mScrollView!=null) {
            // faBtn
            final FloatingActionButton faBtn_add
                    = (FloatingActionButton) v.findViewById(R.id.faBtn_nowLoc);
            faBtn_add.attachToScrollView(mScrollView);
            faBtn_add.setType(FloatingActionButton.TYPE_MINI);
            faBtn_add.setColorNormalResId(R.color.card_background_white);
            faBtn_add.setColorPressedResId(R.color.card_background_color_red_light);
            faBtn_add.setColorRipple(R.color.card_backgroundExpand);
            faBtn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //====================================//
                    map.clear();
                    CurrentLocation mNowGeo = new CurrentLocation(getActivity());
                    mNowGeo.setOnLocListenerSetGps("-1", new CurrentLocation.onDistanceListener() {
                        @Override
                        public void onGetLatLng(Double lat, Double lng) {
                            LatLng nowLocation;
                            nowLocation = new LatLng(lat, lng);
                            map.addMarker(new MarkerOptions().title("當前位置").draggable(true)
                                    .position(nowLocation)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))).showInfoWindow();
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(nowLocation,
                                    map.getCameraPosition().zoom));
                        }
                    });
                    DBtoGeoinfo getEvent = new DBtoGeoinfo(getActivity());
                    ArrayList<NeoGeoInfo> list = getEvent.getArraylistNeoGeoInfoofTasks();
                    for (int i = 0; i < list.size(); i++) {
                        NeoGeoInfo event = list.get(i);
                        if (event.getFlag() == 2) {
                            map.addMarker(new MarkerOptions()
                                    .title(event.getName())
                                    .position(event.getLatlng())
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
                        } else if (event.getFlag() == 3) {
                            map.addMarker(new MarkerOptions()
                                    .title(event.getName())
                                    .position(event.getLatlng())
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                        }
                    }
                    //====================================//
                }
            });
        }


//        final View faBtn_add = v.findViewById(R.id.faBtn_nowLoc);
//        faBtn_add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            //====================================//
//                map.clear();
//                CurrentLocation mNowGeo = new CurrentLocation(getActivity());
//                mNowGeo.setOnLocListenerSetGps("-1", new CurrentLocation.onDistanceListener() {
//                    @Override
//                    public void onGetLatLng(Double lat, Double lng) {
//                        LatLng nowLocation;
//                        nowLocation = new LatLng(lat, lng);
//                        map.addMarker(new MarkerOptions().title("當前位置").draggable(true)
//                                .position(nowLocation)
//                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))).showInfoWindow();
//                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(nowLocation,
//                                map.getCameraPosition().zoom));
//                    }
//                });
//                DBtoGeoinfo getEvent = new DBtoGeoinfo(getActivity());
//                ArrayList<NeoGeoInfo> list = getEvent.getArraylistNeoGeoInfoofTasks();
//                for (int i = 0; i < list.size(); i++) {
//                    NeoGeoInfo event = list.get(i);
//                    if(event.getFlag()==2){
//                        map.addMarker(new MarkerOptions()
//                                .title(event.getName())
//                                .position(event.getLatlng())
//                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
//                    }else if(event.getFlag()==3){
//                        map.addMarker(new MarkerOptions()
//                                .title(event.getName())
//                                .position(event.getLatlng())
//                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
//                    }
//                }
//            }
//            //====================================//
//        });

        PlaceName = (TextView) v.findViewById(R.id.PlaceName);
        MapsInitializer.initialize(getActivity().getApplicationContext());
        switch (GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity())) {
            case ConnectionResult.SUCCESS:
//	                 Toast.makeText(getActivity(), "SUCCESS", Toast.LENGTH_SHORT).show();
                mapView = (MapView) v.findViewById(R.id.map);
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

        return v;
    }

    private void setUpMapIfNeeded() {
        map = mapView.getMap();
        if (map == null) {
            Log.d("", "googleMap is null !!!!!!!!!!!!!!!");
        } else {
//            map.setMyLocationEnabled(true);
            map.getUiSettings().setZoomControlsEnabled(true);
            map.setTrafficEnabled(true);
            CurrentLocation mNowGeo = new CurrentLocation(getActivity());
            mNowGeo.setOnLocListenerSetGps("-1", new CurrentLocation.onDistanceListener() {
                @Override
                public void onGetLatLng(Double lat, Double lng) {
                    LatLng nowLocation;
                    nowLocation = new LatLng(lat, lng);
                    map.addMarker(new MarkerOptions().title("當前位置").draggable(true)
                            .position(nowLocation)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))).showInfoWindow();
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(nowLocation,
                            map.getMaxZoomLevel() - 11));
                }
            });

            DBtoGeoinfo getEvent = new DBtoGeoinfo(getActivity());
            ArrayList<NeoGeoInfo> list = getEvent.getArraylistNeoGeoInfoofTasks();
            if(list!=null){
                for (int i = 0; i < list.size(); i++) {
                    NeoGeoInfo event = list.get(i);
                    if(event.getFlag()==2){
                        map.addMarker(new MarkerOptions()
                                .title(event.getName())
                                .position(event.getLatlng())
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
                    }else if(event.getFlag()==3){
                        map.addMarker(new MarkerOptions()
                                .title(event.getName())
                                .position(event.getLatlng())
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                    }
                }
            }

//            mapController = new MapController(getActivity(),map,PlaceName);
//            mapController.isMoveGet(true);
//            mapController.setOnGeoLoadedLisitener(this);
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onGeoLoaded(GeoInfo geo, int status) {
        DBtoGeoinfo getEvent = new DBtoGeoinfo(getActivity());
        ArrayList<NeoGeoInfo> list = getEvent.getArraylistNeoGeoInfoofTasks();
        for (int i = 0; i < list.size(); i++) {
            NeoGeoInfo event = list.get(i);
            map.addMarker(new MarkerOptions().title(event.getName()).position(event.getLatlng()));
        }
    }

    @Override
    public void onGeoLoading() {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
