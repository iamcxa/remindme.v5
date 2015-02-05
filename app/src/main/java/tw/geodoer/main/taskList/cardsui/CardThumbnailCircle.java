package tw.geodoer.main.taskList.cardsui;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Locale;

import it.gmariotti.cardslib.library.internal.CardThumbnail;
import tw.geodoer.mDatabase.API.DBLocationHelper;
import tw.geodoer.mDatabase.columns.ColumnLocation;
import tw.geodoer.utils.drawable.CircleDrawable;
import tw.moretion.geodoer.R;

/**
 * Created by Kent on 2014/12/25.
 */
public class CardThumbnailCircle extends CardThumbnail {

    private DBLocationHelper dbLocationHelper = new DBLocationHelper(getContext());
    private int cardID;

    public CardThumbnailCircle(Context context, int cardID) {
        super(context);
        this.cardID = cardID;

        setDrawableResource(R.drawable.ic_action_nivagate_1);

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
                // 還原本來的圖片
                v.setBackgroundResource(R.drawable.ic_action_nivagate_1);

                // 按下去!
                if ((event.getAction() == MotionEvent.ACTION_HOVER_ENTER) ||
                        (event.getAction() == MotionEvent.ACTION_HOVER_MOVE) ||
                        (event.getAction() == MotionEvent.ACTION_DOWN)) {
                    //  按下去圖案加深
                    v.setBackgroundResource(R.drawable.ic_action_nivagate_1_dark);

                    //  呼叫導航
                    callGoogleMapNavigation(
                            dbLocationHelper.getItemDouble(cardID, ColumnLocation.KEY.lat),
                            dbLocationHelper.getItemDouble(cardID, ColumnLocation.KEY.lon),
                            dbLocationHelper.getItemString(cardID, ColumnLocation.KEY.name));
                    // 放開
                } else {

                    // 還原本來的圖片
                    v.setBackgroundResource(R.drawable.ic_action_nivagate_1);
                }
                return false;
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

    /*
        呼叫導航
     */
    private void callGoogleMapNavigation(double lat, double lon, String name) {

        String uri = String.format(Locale.ENGLISH,
                "http://maps.google.com/maps?&daddr=%f,%f (%s)",
                lat, lon, name);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        try {
            getContext().startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            try {
                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                getContext().startActivity(unrestrictedIntent);
            } catch (ActivityNotFoundException innerEx) {
                Toast.makeText(getContext(), "Please install a maps application", Toast.LENGTH_LONG).show();
            }
        }
    }
}