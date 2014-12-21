package tw.geodoer.main.taskList.view.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devspark.progressfragment.ProgressFragment;

import me.iamcxa.remindme.R;
import tw.geodoer.common.function.MyDebug;


/**
 * Fragment that appears in the "content_frame", shows a planet
 */

public class RemindmeFragment extends ProgressFragment   {

	private View mContentView;
	private Handler mHandler;
	private static Fragment fragment ;
	public static final String FILTER_STRING="FILTER_STRING";
    private Runnable mShowContentRunnable = new Runnable() {

        @Override
        public void run() {
            setContentShown(true);
            FragmentManager fragmentManager = getFragmentManager();
            fragment = getFragmentManager().findFragmentById(R.id.content_container);
            fragment = ListCursorCardFragment.newInstance();

            fragmentManager
                    .beginTransaction()
                    .replace(R.id.content_container, fragment, "ListCursorCardFragment")
                    .commit();

            MyDebug.MakeLog(0, "RemindmeFragment.run?Q????");
        }

    };

    {
    }

    public static RemindmeFragment newInstance() {
		RemindmeFragment fragment = new RemindmeFragment();
		return fragment;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mHandler.removeCallbacks(mShowContentRunnable);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContentView =inflater.inflate(R.layout.activity_loading_container, container, false);
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setContentView(mContentView);
		obtainData();
	}


	private void obtainData() {
		// Show indeterminate progress
		setContentShown(false);

		mHandler = new Handler();
		mHandler.postDelayed(mShowContentRunnable, 250);

		MyDebug.MakeLog(0, "RemindmeFragment mHandler");
	}
}


