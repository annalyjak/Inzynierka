package lyjak.anna.inzynierka.viewmodel.report.reportModel;

import android.os.Build;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import lyjak.anna.inzynierka.service.model.realm.PointOfRoute;

/**
 * Created by Anna on 25.11.2017.
 */

public class PointOfRouteForReportDTO {

    private int id;
    private LocationForReportDTO point;
    private String name;

    public PointOfRouteForReportDTO(int id, LocationForReportDTO point, String name) {
        this.id = id;
        this.point = point;
        this.name = name;
    }

    @NonNull
    public static PointOfRouteForReportDTO getInstance(PointOfRoute pointOfRoute) {
        return new PointOfRouteForReportDTO(
                pointOfRoute.getId(),
                LocationForReportDTO.getInstance(pointOfRoute.getPoint()),
                pointOfRoute.getName());
    }

    public static List<PointOfRouteForReportDTO> getInstance(List<PointOfRoute> points) {
        List<PointOfRouteForReportDTO> result = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                points.forEach(point -> result.add(PointOfRouteForReportDTO.getInstance(point)));
        } else {
            for(PointOfRoute pointOfRoute : points) {
                result.add(PointOfRouteForReportDTO.getInstance(pointOfRoute));
            }
        }
        return result;
    }

    public int getId() {
        return id;
    }

    public LocationForReportDTO getPoint() {
        return point;
    }

    public String getName() {
        return name;
    }
}
