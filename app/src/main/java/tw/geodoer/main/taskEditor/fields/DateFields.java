package tw.geodoer.main.taskEditor.fields;

/**
 * Created by Kent on 2014/12/24.
 */
//日期成員
public class DateFields {

    // 日期
    protected int mYear = 0;
    protected int mMonth = 0;
    protected int mDay = 0;
    // 時間
    protected int mHour = 0;
    protected int mMinute = 0;
    //private int target;
    // 毫秒
    protected long mOnlyDateMillis = 0;
    protected long mDatePulsTimeMillis = 0;
    public DateFields() {
    }

    //---------------Getter/Setter-----------------//

    public int getmYear() {
        return mYear;
    }

    public void setmYear(int mYear) {
        this.mYear = mYear;
    }

    public int getmMonth() {
        return mMonth;
    }

    public void setmMonth(int mMonth) {
        this.mMonth = mMonth;
    }

    public int getmDay() {
        return mDay;
    }

    public void setmDay(int mDay) {
        this.mDay = mDay;
    }

    public int getmHour() {
        return mHour;
    }

    public void setmHour(int mHour) {
        this.mHour = mHour;
    }

    public int getmMinute() {
        return mMinute;
    }

    public void setmMinute(int mMinute) {
        this.mMinute = mMinute;
    }

    public long getmOnlyDateMillis() {
        return mOnlyDateMillis;
    }

    public void setmOnlyDateMillis(long mOnlyDateMillis) {
        this.mOnlyDateMillis = mOnlyDateMillis;
    }

    public long getmDatePulsTimeMillis() {
        return mDatePulsTimeMillis;
    }

    public void setmDatePulsTimeMillis(long mDatePulsTimeMillis) {
        this.mDatePulsTimeMillis = mDatePulsTimeMillis;
    }

}
