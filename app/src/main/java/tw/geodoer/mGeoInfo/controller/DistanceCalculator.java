package tw.geodoer.mGeoInfo.controller;

public class DistanceCalculator {
	public static final double R = 6372.8; // In kilometers
    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
 
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }
    
    
    public static double Distance(String LatLon,Double Lat1,Double Lon1)
    {
    	String[] array = LatLon.split(",");
    	double Lat2 =Double.parseDouble(array[0]);
    	double Lon2 = Double.parseDouble(array[1]);
    	double distances=0;
		try {
			distances = haversine(
					Lat1, Lon1,
					Lat2,Lon2);
				
		} catch (Exception e) {
			//Toast.makeText(getContext(), e.toString(),
				//	Toast.LENGTH_SHORT).show();
		}
		return distances;
    }
}
