package tw.geodoer.utils;

import android.util.Log;

import tw.geodoer.main.taskPreference.controller.MyPreferences;

public class MyDebug {

    public static final String DEBUG_MSG_TAG = CommonVar.AUTHORITY;

    private MyDebug() {
    }

    /**
     * @param Log_Level 型別int；錯誤訊息等級。<br>
     *                  Log.v   ==0<br>
     *                  Log.w   ==1<br>
     *                  Log.wtf ==2
     * @param msgs      型別String；錯誤訊息內容。
     */
    public static void MakeLog(int Log_Level, String msgs) {
        if (MyPreferences.IS_DEBUG_MSG_ON()) {
            switch (Log_Level) {
                case 0:
                    Log.v(DEBUG_MSG_TAG, msgs);
                    break;
                case 1:
                    Log.w(DEBUG_MSG_TAG, msgs);
                    break;
                case 2:
                    Log.wtf(DEBUG_MSG_TAG, msgs);
                    break;
                default:
                    break;
            }
        }

    }

    public static void MakeLog(int Log_Level, String[] msgString) {
        if (MyPreferences.IS_DEBUG_MSG_ON()) {
            String msgs = "[START]";
            for (String string : msgString) {
                msgs = "," + msgs + "," + string;
            }
            switch (Log_Level) {
                case 0:
                    Log.v(DEBUG_MSG_TAG, msgs);
                    break;
                case 1:
                    Log.w(DEBUG_MSG_TAG, msgs);
                    break;
                case 2:
                    Log.wtf(DEBUG_MSG_TAG, msgs);
                    break;
                default:
                    break;
            }
        }

    }
}
