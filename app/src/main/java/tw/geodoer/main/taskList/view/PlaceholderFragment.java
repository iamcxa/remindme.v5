package tw.geodoer.main.taskList.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geodoer.geotodo.R;
import com.yalantis.taurus.PullToRefreshView;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment
{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    PullToRefreshView mPullToRefreshView;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public PlaceholderFragment() {
    }


    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main_v2_dragtop, container, false);
       //mPullToRefreshView = (PullToRefreshView) rootView.findViewById(R.id.pull_to_refresh);
//        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                mPullToRefreshView.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mPullToRefreshView.setRefreshing(false);
//                    }
//                }, 4000);
//            }
//        });
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));

    }
}