package lyjak.anna.inzynierka.service.respository;

import java.util.List;

import lyjak.anna.inzynierka.service.model.realm.Route;

/**
 * Created by Anna on 18.11.2017.
 */

public interface RouteService {

    List<Route> getRoutesFromDatabase();

    void removeRouteFromDatabase(Route routeToRemove);
}
