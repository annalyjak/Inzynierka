package lyjak.anna.inzynierka.viewmodel.report.reportModel;

import java.util.Date;
import java.util.List;

import lyjak.anna.inzynierka.service.model.realm.Route;

/**
 * Created by Anna on 25.11.2017.
 */

public class ActualRouteForReportDTO {

    private Date date;

    private Date startDate;
    private Date endDate;

    private List<LocationForReportDTO> locations;

    private ActualRouteForReportDTO(Date date, Date startDate, Date endDate, List<LocationForReportDTO> locations) {
        this.date = date;
        this.startDate = startDate;
        this.endDate = endDate;
        this.locations = locations;
    }

    public static ActualRouteForReportDTO getInstance(Route route) {
        return new ActualRouteForReportDTO(route.getDate(),
                route.getStartDate(),
                route.getEndDate(),
                LocationForReportDTO.getInstance(route.getLocations()));
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public List<LocationForReportDTO> getLocations() {
        return locations;
    }
}
