package tw.geodoer.mPriority.view;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


/**
 * Created by MurasakiYoru on 2015/4/22.
 */
public class mTestActivity extends FragmentActivity
{
    private ViewPager pager = null;

    private final int startpoint = 7;
    private final int max = 38;
    private SmartFragmentStatePagerAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);

//        super.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LinearLayout RR = new LinearLayout(this);
//----------------------------------------------------------------
        pager = new ViewPager(this);
        pager.setId(132456);
        adapter = new mSchedulePagerAdapter(getSupportFragmentManager(),startpoint,max);
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener((ViewPager.OnPageChangeListener)adapter);
        RR.addView(pager);

        pager.setOffscreenPageLimit(3);
        pager.setCurrentItem(startpoint);
//-----------------------------------------------------------------
        setContentView(RR);
        getWindow().setLayout(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);


    }

}
