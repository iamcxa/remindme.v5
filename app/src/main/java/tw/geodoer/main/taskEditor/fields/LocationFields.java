package tw.geodoer.main.taskEditor.fields;

/**
 * Created by Kent on 2014/12/24.
 */ /*
 * 任務地點成員
 */
public class LocationFields {

    public  LocationFields(){}

    // 1 - 任務地點id
    public int locationId=0;
    // 2 - 地點名稱字串
    private String name ="null";
    // 3 - 4 - 經緯度
    private Double lat=0.0;
    private Double lon=0.0;
    // 5 - 與上次偵測地點之距離
    private Double distance=0.0;
    // 6 - 上次使用時間
    private long lastUsedTime=0;

    // 額外欄位  - 資料庫目前沒有存入
    // 是否有搜尋過地點
    private Boolean isSearched = false;
    private Boolean isDropped = false;
    // gps使用時間
    private int gpsUseTime = 0;

    //---------------Getter/Setter-----------------//

    /**
     * @return the LocationId
     */
    public int getLocationId() {
        return locationId;
    }
    /**
    * //@param locationId the LocationId to set
     */
    public void setLocationId(int _id) {
        this.locationId = _id;
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the lat
     */
    public Double getLat() {
        return lat;
    }
    /**
     * @param lat the lat to set
     */
    public void setLat(Double lat) {
        this.lat = lat;
    }
    /**
     * @return the lon
     */
    public Double getLon() {
        return lon;
    }
    /**
     * @param lon the lon to set
     */
    public void setLon(Double lon) {
        this.lon = lon;
    }
    /**
     * @return the distance
     */
    public Double getDistance() {
        return distance;
    }
    /**
     * @param distance the distance to set
     */
    public void setDistance(Double distance) {
        this.distance = distance;
    }
    /**
     * @return the lastUsedTime
     */
    public long getLastUsedTime() {
        return lastUsedTime;
    }
    /**
     * @param lastUsedTime the lastUsedTime to set
     */
    public void setLastUsedTime(long lastUsedTime) {
        this.lastUsedTime = lastUsedTime;
    }
    /**
     * @return the isSearched
     */
    public Boolean getIsSearched() {
        return isSearched;
    }
    /**
     * @param isSearched the isSearched to set
     */
    public void setIsSearched(Boolean isSearched) {
        this.isSearched = isSearched;
    }
    /**
     * @return the isDropped
     */
    public Boolean getIsDropped() {
        return isDropped;
    }
    /**
     * @param isDropped the isDropped to set
     */
    public void setIsDropped(Boolean isDropped) {
        this.isDropped = isDropped;
    }
    /**
     * @return the gpsUseTime
     */
    public int getGpsUseTime() {
        return gpsUseTime;
    }
    /**
     * @param gpsUseTime the gpsUseTime to set
     */
    public void setGpsUseTime(int gpsUseTime) {
        this.gpsUseTime = gpsUseTime;
    }
}
