package lyjak.anna.inzynierka.model.utils;

import android.location.Location;

import java.util.List;

import io.realm.RealmList;
import lyjak.anna.inzynierka.model.realmObjects.RealmLocation;
import lyjak.anna.inzynierka.model.modelDTO.LocationForReportDTO;

/**
 *
 * Created by Anna on 03.11.2017.
 */

public class DistanceAndDurationUtil {

    private DistanceAndDurationUtil() {}

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

    public static float getDistanceInKm(LocationForReportDTO first, LocationForReportDTO second) {
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

    private static int calculateDistance(List<LocationForReportDTO> locations) {
        int result = 0;
        if (locations == null) {
            return result;
        }
        if (locations.size() > 1) {
            LocationForReportDTO first = locations.get(0);
            LocationForReportDTO second;
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

    public static int calculateDistanceInKm(List<LocationForReportDTO> locations) {
        return calculateDistance(locations)/1000;
    }
}
