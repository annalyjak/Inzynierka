package lyjak.anna.inzynierka.viewmodel.reportModel;

import android.os.Build;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import lyjak.anna.inzynierka.service.model.realm.RealmLocation;

/**
 *
 * Created by Anna on 25.11.2017.
 */

public class LocationForReportDTO {

    private double latitude;
    private double longitude;

    private LocationForReportDTO(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @NonNull
    public static LocationForReportDTO getInstance(RealmLocation location) {
        return new LocationForReportDTO(location.getLatitude(), location.getLongitude());
    }

    public static List<LocationForReportDTO> getInstance(List<RealmLocation> locations) {
        List<LocationForReportDTO> result = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locations.forEach(location -> result.add(LocationForReportDTO.getInstance(location)));
        } else {
            for(RealmLocation rl : locations) {
                result.add(getInstance(rl));
            }
        }
        return result;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
