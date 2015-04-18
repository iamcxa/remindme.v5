package tw.geodoer.mGeoInfo.API;
 
import android.location.Location;
 
public interface GPSCallback
{
        public abstract void onGPSUpdate(Location location);
}