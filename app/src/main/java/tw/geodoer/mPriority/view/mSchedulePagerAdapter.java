package tw.geodoer.mPriority.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;


/**
 * Created by MurasakiYoru on 2015/4/22.
 */


public class mSchedulePagerAdapter extends SmartFragmentStatePagerAdapter implements ViewPager.OnPageChangeListener
{
    private int max = 200;
    private int startpoint;

    public mSchedulePagerAdapter(FragmentManager fragmentManager,int startpoint,int max)
    {
        super(fragmentManager);
        this.max = max;
        this.startpoint = startpoint;
        notifyDataSetChanged();
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        //return fragments.size();
        return max;
    }

    @Override
    public int getItemPosition(Object object)
    {
        return PagerAdapter.POSITION_NONE;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position)
    {
        return mScheduleFragment.newInstance(position-startpoint);
        //return fragments.get(position);

    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return ""+position;

    }
//---------------------------------------------------------------------------------------------------
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position)
    {


//        if( position  +2 >=  max )
//        {
//            max++;
//            notifyDataSetChanged();
//        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

