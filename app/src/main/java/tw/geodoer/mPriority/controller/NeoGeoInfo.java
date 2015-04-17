package tw.geodoer.mPriority.controller;

import com.google.android.gms.maps.model.LatLng;

import fud.geodoermap.GeoInfo;

/**
 * Created by MurasakiYoru on 2015/4/16.
 */
public class NeoGeoInfo extends GeoInfo
{
    private static final int FLAG_EMPTY = 0;
    private static final int FLAG_TIME = 1;
    private static final int FLAG_LATLONE = 2;
    private static final int FLAG_LATLON_TIME = 3;

    private long due_date_millis = 0;
    private int flag = 0;

    private String title="null";
    private String remark="null";


    public NeoGeoInfo()
    {
    }
    public NeoGeoInfo(String name)
    {
        this.setName(name);
    }
    public NeoGeoInfo(LatLng latlng)
    {
        this.setLatlng(latlng);
    }
    public NeoGeoInfo(long millis)
    {
        this.setMillis(millis);
    }

    public NeoGeoInfo(String name,LatLng latlng,long millis)
    {
        this.setName(name);
        this.setLatlng(latlng);
        this.setMillis(millis);
        check_flag();
    }

    public void setName(String name)
    {
        super.setName(name);
    }

    public void setLatlng(LatLng latlng)
    {
        super.setLatlng(latlng);
        check_flag();
    }
    public void setMillis(long millis)
    {
        this.due_date_millis = millis;
        check_flag();
    }
    public String getName() { return this.name; }
    public LatLng getLatlng() { return this.latlng; }
    public long getMillis() { return this.due_date_millis; }
    public int getFlag() { return  this.flag; }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    private void check_flag()
    {
        if(   this.latlng.equals(new LatLng(-1,-1)) && this.due_date_millis ==0  )
            this.flag = FLAG_EMPTY;  //no lat no time
        else if( this.latlng.equals(new LatLng(-1,-1)) )
            this.flag = FLAG_TIME;  //no lat have time
        else if( this.due_date_millis ==0 )
            this.flag = FLAG_LATLONE;  //have lat
        else this.flag = FLAG_LATLON_TIME;
    }
}
