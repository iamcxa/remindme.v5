package tw.geodoer.mPriority.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by MurasakiYoru on 2015/4/22.
 */
public class mScheduleFragment extends Fragment
{
    // Store instance variables
    public int page;

    //newInstance constructor for creating fragment with arguments
    public static mScheduleFragment newInstance(int page)
    {
        mScheduleFragment fragmentFirst = new mScheduleFragment();
        fragmentFirst.page=page;
        return fragmentFirst;
    }

    public mScheduleFragment()
    {
        super();
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        Random md = new Random();
        md.setSeed(System.currentTimeMillis());
        LinearLayout view = new LinearLayout(getActivity());
        TextView tvLabel = new TextView(getActivity());
        tvLabel.setText(page + " -- " );
        tvLabel.setTextSize(24);
        view.addView(tvLabel);
        view.setBackgroundColor(Color.argb(128,md.nextInt(256),md.nextInt(256),md.nextInt(256)));
        return view;
    }
}