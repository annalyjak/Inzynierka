package lyjak.anna.inzynierka.service.respository;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import javax.inject.Inject;

import lyjak.anna.inzynierka.service.model.realm.Route;

/**
 * Created by Anna on 18.11.2017.
 */

public class RouteRepository {

    private RouteService routeService;
    private static RouteRepository routeRepository;

    private RouteRepository(Activity activity) {
        routeService = new OnMarkersOperations(activity);
    }

    public synchronized static RouteRepository getInstance(Activity activity) {
        if (routeRepository == null) {
            if (routeRepository == null) {
                routeRepository = new RouteRepository(activity);
            }
        }
        return routeRepository;
    }

    public LiveData<List<Route>> getRouteList(String routes) {
        final MutableLiveData<List<Route>> data = new MutableLiveData<>();
        data.setValue(routeService.getRoutesFromDatabase());
        return data;
    }

    public void remove(Route route) {
        routeService.removeRouteFromDatabase(route);
    }
}
