package tw.geodoer.main.taskList.cardsui;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;

import com.geodoer.geotodo.R;

import it.gmariotti.cardslib.library.internal.CardThumbnail;
import tw.geodoer.mDatabase.API.DBLocationHelper;
import tw.geodoer.mDatabase.API.DBTasksHelper;
import tw.geodoer.mDatabase.columns.ColumnTask;
import tw.geodoer.main.taskList.controller.ActionOnCardLongClicked;
import tw.geodoer.utils.drawable.CircleDrawable;

/**
 * Created by Kent on 2014/12/25.
 */
public class CardThumbnailCircle extends CardThumbnail {

    private DBLocationHelper dbLocationHelper = new DBLocationHelper(getContext());
    private int cardID;
    private int status;

    public CardThumbnailCircle(Context context, int cardID) {
        super(context);
        this.cardID = cardID;

        setDrawableResource(R.drawable.ic_action_nivagate_1);

        DBTasksHelper dbTasksHelper = new DBTasksHelper(getContext());
        this.status=dbTasksHelper.getItemInt(cardID, ColumnTask.KEY.status);
        switch (status){
            case 0:
                setDrawableResource(R.drawable.ic_check);
                break;
            case 1:
                setDrawableResource(R.drawable.ic_check_on);
                break;
            case 2:
                setDrawableResource(R.drawable.ic_back);
                break;

        }

        //  float density = getContext().getResources().getDisplayMetrics().density;
        // int size = (int) (40 * density);
        // setUrlResource("https://lh5.googleusercontent.com/-squZd7FxR8Q/UyN5UrsfkqI/AAAAAAAAbAo/VoDHSYAhC_E/s" + size + "/new%2520profile%2520%25282%2529.jpg");
        // setErrorResource(R.drawable.ic_drawer);
    }

    @Override
    public boolean applyBitmap(View imageView, Bitmap bitmap) {

        // 按鈕動作
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 按下去!
                if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    // 開始動作
                    DBTasksHelper dbTasksHelper = new DBTasksHelper(getContext());
                    switch (status) {
                        case 0:
                            v.setBackgroundResource(R.drawable.ic_check_on);
                            dbTasksHelper.setItem(cardID, ColumnTask.KEY.status, 1);
                            dbTasksHelper.setItem(cardID, ColumnTask.KEY.checked,
                                    System.currentTimeMillis());
                            break;
                        case 1:
                            v.setBackgroundResource(R.drawable.ic_check);
                            dbTasksHelper.setItem(cardID, ColumnTask.KEY.status, 0);
                            dbTasksHelper.setItem(cardID, ColumnTask.KEY.checked,
                                    0);
                            DBTasksHelper mBDT =new DBTasksHelper(getContext());
                            ActionOnCardLongClicked ACC = new ActionOnCardLongClicked(getContext(),mBDT.getCursor());
                            ACC.restoreCard(cardID);
                            break;
                        case 2:
                            v.setBackgroundResource(R.drawable.ic_back_on);
                            dbTasksHelper.setItem(cardID, ColumnTask.KEY.status, 1);
                            break;
                    }
                }






//                // 還原本來的圖片
//                v.setBackgroundResource(R.drawable.ic_action_nivagate_1);
//
//                // 按下去!
//                if ((event.getAction() == MotionEvent.ACTION_HOVER_ENTER) ||
//                        (event.getAction() == MotionEvent.ACTION_HOVER_MOVE) ||
//                        (event.getAction() == MotionEvent.ACTION_DOWN)) {
//                    //  按下去圖案加深
//                    v.setBackgroundResource(R.drawable.ic_action_nivagate_1_dark);
//
//                    // 放開
//                } else {
//                    //todo:還原似乎未完成20150416
//                    // 還原本來的圖片
//                    v.setBackgroundResource(R.drawable.ic_action_nivagate_1);
//                    v.setBackgroundResource(R.drawable.ic_action_undo);
//                }

                return true;
            }
        });

        // 背景畫個圓形
        CircleDrawable circle = new CircleDrawable(bitmap);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            imageView.setBackground(circle);
        else
            imageView.setBackgroundDrawable(circle);
        return true;
    }
}