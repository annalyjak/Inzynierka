package lyjak.anna.inzynierka.viewmodel.report.modelDTO;

import java.util.Date;
import java.util.List;

import lyjak.anna.inzynierka.service.model.realm.PlannedRoute;

/**
 * Created by Anna on 25.11.2017.
 */

public class PlannedRouteForReportDTO {
    private String title;
    private List<PointOfRouteForReportDTO> points;
    private List<LocationForReportDTO> line;
    private Date date;
    private int distance;
    private int duration;

    private PlannedRouteForReportDTO(String title, List<PointOfRouteForReportDTO> points, List<LocationForReportDTO> line, Date date, int distance, int duration) {
        this.title = title;
        this.points = points;
        this.line = line;
        this.date = date;
        this.distance = distance;
        this.duration = duration;
    }

    public static PlannedRouteForReportDTO getInstance(PlannedRoute route) {
        return new PlannedRouteForReportDTO(route.getTitle(),
                PointOfRouteForReportDTO.getInstance(route.getPoints()),
                LocationForReportDTO.getInstance(route.getLine()),
                route.getDate(),
                route.getDistance(),
                route.getDuration());
    }

    public String getTitle() {
        return title;
    }

    public List<PointOfRouteForReportDTO> getPoints() {
        return points;
    }

    public List<LocationForReportDTO> getLine() {
        return line;
    }

    public Date getDate() {
        return date;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }
}
