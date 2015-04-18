package tw.geodoer.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyCalendar {

    String array[];

    private MyCalendar() {
    }

    /**
     * @param {TaskDate} 要比對的日期
     * @param {Option}   輸入是否含有 HH:mm
     * @return 取得今天與輸入日期相距多久
     */
    public static long getDaysLeftByLong(long aTimeMillis, int Option) {
        try {
            // 取得兩個時間的Unix時間
            Long now = System.currentTimeMillis();
            Long timeP = now - aTimeMillis;// 毫秒差
            // 相減獲得兩個時間差距的毫秒
            // Long sec = timeP / 1000;// 秒差
            // Long min = timeP / 1000 * 60;// 分差
            // Long hr = timeP / 1000 * 60 * 60;// 時差
            Long day = timeP / (1000 * 60 * 60 * 24);// 日差
            return day;
        } catch (Exception e) {
            // TODO: handle exception
            MyDebug.MakeLog(2, e.toString());
            return -1;
        }
    }

    /**
     * @param {TaskDate} 要比對的日期
     * @param {Option}   輸入是否含有 HH:mm
     * @return 取得今天與輸入日期相距多久
     */
    public static long getDaysLeft(String TaskDate, int Option) {
        // 定義時間格式
        // java.text.SimpleDateFormat sdf = new


        SimpleDateFormat sdf = null;
        if (Option == 1) {
            sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());
        } else if (Option == 2) {
            sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        }

//        // 取得現在時間
//        //Date now = new Date();
//        String nowDate = sdf.format(getNow());
//        MyDebug.MakeLog(0, "now:" + nowDate + ", task:" + TaskDate);
//        try {
//            // 取得事件時間與現在時間
//            Date dt1 = sdf.parse(nowDate);
//            //Date dt2 = sdf.parse(TaskDate);
//
//            // 取得兩個時間的Unix時間
//            Long ut1 = dt1.getTime();
//            //Long ut2 = dt2.getTime();
//
//            Long timeP = Long.valueOf(TaskDate) - ut1;// 毫秒差
//            // 相減獲得兩個時間差距的毫秒
//            // Long sec = timeP / 1000;// 秒差
//            // Long min = timeP / 1000 * 60;// 分差
//            // Long hr = timeP / 1000 * 60 * 60;// 時差
//            Long day = timeP / (1000 * 60 * 60 * 24);// 日差
//            MyDebug.MakeLog(0, "Get days left Sucessed! " + day);
//            return day;
//        } catch (Exception e) {
//            // TODO: handle exception
//            MyDebug.MakeLog(2, e.toString());
//            return -1;
        try
        {
            Date date = sdf.parse(TaskDate);
            Long task_millis = date.getTime();
            Long now_millis = System.currentTimeMillis();

            task_millis /= 86400*1000;
            now_millis /= 86400*1000;

            return task_millis - now_millis;
        } catch (Exception e) {
            //  TODO : handle exception
            MyDebug.MakeLog(2, e.toString());
            return -1;
        }
    }

    /**
     * @param {Days} 未來天數, 0即為今天
     * @return 取得未來n天的毫秒數
     */
    public static long getNextFewDays(int Days) {
        long NextFewDays = System.currentTimeMillis() + (60 * 60 * 24 * 1000 * Days);//N天的毫秒數;
        return NextFewDays;

    }

    /**
     * @return 取得現在當下的毫秒數
     */
    public static long getNow() {
        long curDate = System.currentTimeMillis();
        return curDate;
    }

    /**
     * @param {A_Time_In_Milliseconds} 要被轉換的毫秒數
     * @param {IsNeedHourAndMins}      是否需要輸出小時/分鐘
     * @return 將輸入的的毫秒數轉換為文字 YYYY/MM/DD 後輸出
     */
    public static String getDate_From_TimeMillis(boolean IsNeedHourAndMins, long A_Time_In_Milliseconds) {
        SimpleDateFormat sdf = null;
        if (IsNeedHourAndMins) {
            sdf = new SimpleDateFormat("yyyy/MM/dd_HH:mm", getDefaultLocal());
        } else {
            sdf = new SimpleDateFormat("yyyy/MM/dd", getDefaultLocal());
        }
        String nowDate = sdf.format(A_Time_In_Milliseconds);
        MyDebug.MakeLog(1, "MyCalendar Millis->Date function\n,start with raw long=" + A_Time_In_Milliseconds + ", result=" + nowDate);
        return nowDate;
    }

    /**
     * @param {A_Date} 要被轉換的毫秒數
     * @return 由輸入的 YYYY/MM/DD 轉換為毫秒數輸出
     */
    public static long getTimeMillis_From_Date(String A_Date) {
        String[] mTimeMillis = {""};
        MyDebug.MakeLog(0, "A_Date=" + A_Date);
        if (A_Date.contains("_")) {
            String[] nTimeMillis = A_Date.split("_");
            MyDebug.MakeLog(0, "nTimeMillis=" + nTimeMillis[0]);
            mTimeMillis = nTimeMillis[0].split("/");
        } else {
            mTimeMillis = A_Date.split("/");
        }
        MyDebug.MakeLog(0, "mTimeMillis[]='" + mTimeMillis[0] + "," + mTimeMillis[1] + "," + mTimeMillis[2] + "'");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.valueOf(mTimeMillis[0]),
                Integer.valueOf(mTimeMillis[1]),
                Integer.valueOf(mTimeMillis[2]),
                0, 0, 0);

        return (calendar.getTimeInMillis()) / 1000;
    }

    public static String getTodayString(int ExtraDaysIfNeeded) {
        Calendar today = Calendar.getInstance();
        today.add(Calendar.DAY_OF_MONTH, ExtraDaysIfNeeded);
        int dayOfMonth = today.get(Calendar.DAY_OF_MONTH);
        String month = String.valueOf(today.get(Calendar.MONTH) + 1);
        int year = today.get(Calendar.YEAR);
        if (String.valueOf(month).length() == 1) {
            month = "0" + month;
        }
        return year + "/" + month + "/" + dayOfMonth;
    }

    public static String getThisMonth() {
        Calendar today = Calendar.getInstance();
        String month = String.valueOf(today.get(Calendar.MONTH) + 1);
        if (String.valueOf(month).length() == 1) {
            month = "0" + month;
        }
        return month;
    }

    public static String getThisYear() {
        Calendar today = Calendar.getInstance();
        int year = today.get(Calendar.YEAR);
        return String.valueOf(year);
    }

    public static String getThisDay() {
        Calendar today = Calendar.getInstance();
        int dayOfMonth = today.get(Calendar.DAY_OF_MONTH);
        return String.valueOf(dayOfMonth);
    }

    public static String getThisHour() {
        Calendar today = Calendar.getInstance();
        int dayOfMonth = today.get(Calendar.HOUR_OF_DAY);
        return String.valueOf(dayOfMonth);
    }

    public static String getThisMinutes() {
        Calendar today = Calendar.getInstance();
        int dayOfMonth = today.get(Calendar.MINUTE);
        return String.valueOf(dayOfMonth);
    }

    // Get the defaulf Local of android
    public static Locale getDefaultLocal() {
        CommonVar.DEFAULT_LOCAL = Locale.getDefault();
        return CommonVar.DEFAULT_LOCAL;
    }
}
