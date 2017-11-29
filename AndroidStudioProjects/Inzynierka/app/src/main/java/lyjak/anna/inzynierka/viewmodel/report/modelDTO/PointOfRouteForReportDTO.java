package lyjak.anna.inzynierka.viewmodel.report.modelDTO;

import android.os.Build;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import lyjak.anna.inzynierka.service.model.realm.PointOfRoute;

/**
 * DTO for creating report in another thread
 * Created by Anna on 25.11.2017.
 */

public class PointOfRouteForReportDTO {

    private int id;
    private LocationForReportDTO point;
    private String name;
    private int distance;
    private int duration;

    public PointOfRouteForReportDTO(int id,
                                    LocationForReportDTO point,
                                    String name,
                                    int distance,
                                    int duration) {
        this.id = id;
        this.point = point;
        this.name = name;
        this.distance = distance;
        this.duration = duration;
    }

    @NonNull
    public static PointOfRouteForReportDTO getInstance(PointOfRoute pointOfRoute) {
        return new PointOfRouteForReportDTO(
                pointOfRoute.getId(),
                LocationForReportDTO.getInstance(pointOfRoute.getPoint()),
                pointOfRoute.getName(),
                0,
                0);
    }

    @NonNull
    public static PointOfRouteForReportDTO getInstanceWithDistance(PointOfRoute pointOfRoute) {
        return new PointOfRouteForReportDTO(
                pointOfRoute.getId(),
                LocationForReportDTO.getInstance(pointOfRoute.getPoint()),
                pointOfRoute.getName(),
                pointOfRoute.getDistance(),
                pointOfRoute.getDuration());
    }

    public static List<PointOfRouteForReportDTO> getInstance(List<PointOfRoute> points) {
        List<PointOfRouteForReportDTO> result = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                points.forEach(point -> {
                    if (point.getDistance() != 0) {
                        result.add(PointOfRouteForReportDTO.getInstanceWithDistance(point));
                    } else {
                        result.add(PointOfRouteForReportDTO.getInstance(point));
                    }
                });
        } else {
            for(PointOfRoute pointOfRoute : points) {
                if (pointOfRoute.getDistance() != 0) {
                    result.add(PointOfRouteForReportDTO.getInstanceWithDistance(pointOfRoute));
                } else {
                    result.add(PointOfRouteForReportDTO.getInstance(pointOfRoute));
                }
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

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }
}
