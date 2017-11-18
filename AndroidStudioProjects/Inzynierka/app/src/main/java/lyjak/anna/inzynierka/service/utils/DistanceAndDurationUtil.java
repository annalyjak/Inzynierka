package lyjak.anna.inzynierka.service.utils;

import android.location.Location;

import io.realm.RealmList;
import lyjak.anna.inzynierka.service.model.realm.RealmLocation;

/**
 * Created by Anna on 03.11.2017.
 */

public class DistanceAndDurationUtil {

    public static float getDistanceInKm(RealmLocation first, RealmLocation second) {
        int result = 0;
        float[] results = new float[1];
        Location.distanceBetween(
                first.getLatitude(),
                first.getLongitude(),
                second.getLatitude(),
                second.getLongitude(),
                results);
        return results[0]/1000;
    }

    public static int calculateDistance(RealmList<RealmLocation> locations) {
        int result = 0;
        if (locations == null) {
            return result;
        }
        if (locations.size() > 1) {
            RealmLocation first = locations.get(0);
            RealmLocation second;
            for (int i = 1; i < locations.size(); i++) {
                second = locations.get(i);
                float[] results = new float[1];
                Location.distanceBetween(
                        first.getLatitude(),
                        first.getLongitude(),
                        second.getLatitude(),
                        second.getLongitude(),
                        results);
                result += results[0];

                first = locations.get(i);
            }
        }
        return result;
    }

    public static int calculateDistanceInKm(RealmList<RealmLocation> locations) {
        return calculateDistance(locations)/1000;
    }

}
