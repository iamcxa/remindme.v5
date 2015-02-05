package tw.geodoer.mGeoInfo.controller;

import android.location.Location;

public interface GPSCallback {
    public abstract void onGPSUpdate(Location location);
}