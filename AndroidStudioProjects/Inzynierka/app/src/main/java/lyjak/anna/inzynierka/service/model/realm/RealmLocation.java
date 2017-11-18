package lyjak.anna.inzynierka.service.model.realm;

import io.realm.RealmObject;

/**
 * Created by Anna Łyjak on 22.09.2017.
 */

public class RealmLocation extends RealmObject {

    private double latitude;
    private double longitude;

    public RealmLocation(){}

    public RealmLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "lat: " + latitude + " long:" + longitude;
    }
}
