package lyjak.anna.inzynierka.service.respository;

import com.google.android.gms.maps.model.Marker;

import java.util.Date;
import java.util.List;

import lyjak.anna.inzynierka.service.model.realm.PlannedRoute;
import lyjak.anna.inzynierka.service.model.realm.PointOfRoute;
import lyjak.anna.inzynierka.service.model.realm.Route;
import lyjak.anna.inzynierka.view.fragments.dummy.DirectionFinder;

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

    PlannedRoute findPlannedRoute(String title, int distance, int duration);
    Route findRoute(Date date, Date startDate, Date endDate);

    // Update
    void addPointToActualRoute(Marker marker);
    void addPointToRoute(Marker marker, PlannedRoute route);

    void storePlannedRouteInDatabase(PlannedRoute route,
                                            DirectionFinder calculatedFields);
    void swap(List<PointOfRoute> mDataset, int i, int i1);
    void calculateLine(PlannedRoute route);

    // Delete
    void removeRouteFromDatabase(Route routeToRemove);
    void removePlannedRouteFromDatabase(PlannedRoute routeToRemove);

}
