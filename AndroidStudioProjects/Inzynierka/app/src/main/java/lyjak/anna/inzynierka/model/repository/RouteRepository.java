package lyjak.anna.inzynierka.model.repository;

import com.google.android.gms.maps.model.Marker;

import java.util.Date;
import java.util.List;

import lyjak.anna.inzynierka.model.realmObjects.HistoricalReports;
import lyjak.anna.inzynierka.model.realmObjects.PlannedRoute;
import lyjak.anna.inzynierka.model.realmObjects.PointOfRoute;
import lyjak.anna.inzynierka.model.realmObjects.Route;
import lyjak.anna.inzynierka.model.findRoute.DirectionFinder;

/**
 *
 * Created by Anna on 27.11.2017.
 */

public interface RouteRepository {

    // Create
    void createNewPlannedRoute(String title, Marker marker);
    void createReportInDatabase(PlannedRoute plannedRoute, Route route, String filePath);

    // Read
    List<PlannedRoute> getAllPlannedRoutes();
    List<Route> getAllActualRoutes();
    List<HistoricalReports> getAllHistoricalReports();

    PlannedRoute findPlannedRoute(String title, int distance, int duration);
    Route findRoute(Date date, Date startDate, Date endDate);

    // Update
    void addPointToActualRoute(Marker marker);
    void addPointToRoute(Marker marker, PlannedRoute route);

    void storePlannedRouteInDatabase(PlannedRoute route,
                                            DirectionFinder calculatedFields);
    void swap(List<PointOfRoute> dataset, int index1, int index2);
    void calculateLine(PlannedRoute route);

    // Delete
    void removeRouteFromDatabase(Route routeToRemove);
    void removePlannedRouteFromDatabase(PlannedRoute routeToRemove);

}
