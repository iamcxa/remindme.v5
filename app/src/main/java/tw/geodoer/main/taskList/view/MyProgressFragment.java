package tw.geodoer.main.taskList.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devspark.progressfragment.ProgressFragment;
import com.geodoer.geotodo.R;

import tw.geodoer.mGeoInfo.view.ShowTodoGeoFragment;
import tw.geodoer.utils.CommonVar;
import tw.geodoer.utils.MyDebug;


/**
 * Fragment that appears in the "content_frame", shows a planet
 */

public class MyProgressFragment extends ProgressFragment {

    ViewHolder viewHolder = new ViewHolder();

    private Runnable mShowContentRunnable = new Runnable() {

        @Override
        public void run() {
            //viewHolder.position=getArguments().getInt(FILTER_NAME);
            setContentShown(true);
            viewHolder.fragmentManager = getFragmentManager();
            viewHolder.targetFragment = getFragmentManager().findFragmentById(R.id.content_container);

            switch (viewHolder.position){
                case 3:
                    viewHolder.targetFragment = ShowTodoGeoFragment.newInstance("parm1", "parm2");
                    viewHolder.fragmentTag="ShowTodoGeoFragment";
                    break;

                case 6:
                    viewHolder.targetFragment = APITestingFragment.newInstance("parm1", "parm2");
                    viewHolder.fragmentTag="APITestingFragment";
                    break;

                default:
                    viewHolder.targetFragment = ListCursorCardFragment.newInstance(viewHolder.position);
                    viewHolder.fragmentTag="ListCursorCardFragment";
                    break;
            }

            //viewHolder.targetFragment.setArguments(viewHolder.arg);
            viewHolder.fragmentManager
                    .beginTransaction()
                    .replace(R.id.content_container,
                            viewHolder.targetFragment,
                            viewHolder.fragmentTag)
                    .commit();

            MyDebug.MakeLog(0, "@載入畫面="+viewHolder.targetFragment.getTag()+"...");
        }
    };

    private Handler mHandler;

    public static MyProgressFragment newInstance(int position) {
        MyProgressFragment fragment=new MyProgressFragment();
        Bundle args = new Bundle();
        args.putInt(CommonVar.STRING_DRAWER_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mHandler.removeCallbacks(mShowContentRunnable);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            viewHolder.position = getArguments().getInt(CommonVar.STRING_DRAWER_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(viewHolder.mContentView==null){
            viewHolder.inflater=inflater;
            viewHolder.mContentView =
                    viewHolder.inflater.inflate(R.layout.activity_loading_container, container, false);
        }
        return super.onCreateView(viewHolder.inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        setContentView(viewHolder.mContentView);
        obtainData();
    }


    private void obtainData() {
        // Show indeterminate progress
        setContentShown(false);

        mHandler = new Handler();
        mHandler.postDelayed(mShowContentRunnable, 333);

        MyDebug.MakeLog(0, "RemindmeFragment mHandler");
    }

    static class ViewHolder{
        int position = 0;
         String fragmentTag;
          View mContentView;
         FragmentManager fragmentManager;
         Fragment targetFragment;
         LayoutInflater inflater;
    }
}


