/**
 * 
 */
package tw.remindme.taskEditor.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * @author Kent
 *
 */
public class TaskEditorFragmentPagerAdapter extends FragmentPagerAdapter {
	
	private ArrayList<Fragment> fragments;

	public TaskEditorFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int pos) {
		return fragments.get(pos);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return "Tab_" + position;
	}

}
