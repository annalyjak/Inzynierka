package lyjak.anna.inzynierka.viewmodel.others;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anna on 14.10.2017.
 */

public class RouteBeetweenTwoPointsDTO {

    private LatLng startPoint;
    private LatLng endPoint;

    private String startPlaceName;
    private String endPlaceName;

    private int distance;
    private int duration;

    private List<LatLng> routePoints;

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void createStartPoint(double lat, double longi) {
        startPoint = new LatLng(lat, longi);
    }

    public void createEndPoint(double lat, double longi) {
        endPoint = new LatLng(lat, longi);
    }

    public LatLng getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(LatLng startPoint) {
        this.startPoint = startPoint;
    }

    public LatLng getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(LatLng endPoint) {
        this.endPoint = endPoint;
    }

    public String getStartPlaceName() {
        return startPlaceName;
    }

    public void setStartPlaceName(String startPlaceName) {
        this.startPlaceName = startPlaceName;
    }

    public String getEndPlaceName() {
        return endPlaceName;
    }

    public void setEndPlaceName(String endPlaceName) {
        this.endPlaceName = endPlaceName;
    }

    public List<LatLng> getRoutePoints() {
        return routePoints;
    }

    public void setRoutePoints(List<LatLng> routePoints) {
        this.routePoints = routePoints;
    }

    public void addToRoutePoints(LatLng latLng) {
        if (routePoints == null) {
            routePoints = new ArrayList<>();
        }
        routePoints.add(latLng);
    }

    public LatLng getLatLngFromRoutesPoints(int i) {
        return routePoints.get(i);
    }

    public int getSizeOfRoutePoints() {
        return routePoints == null? 0 : routePoints.size();
    }
}
