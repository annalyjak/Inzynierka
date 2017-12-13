package lyjak.anna.inzynierka.viewmodel;

import android.content.Context;
import android.util.Log;

import java.util.List;

import lyjak.anna.inzynierka.model.realmObjects.Route;

/**
 * Created by Anna on 19.11.2017.
 */

public class ActualRouteListViewModel extends MainViewModel {

    private static final String TAG = ActualRouteListViewModel.class.getSimpleName();

    private List<Route> dataset;

    public ActualRouteListViewModel(Context context) {
        super(context);
    }

    public void getRoutes() {
        dataset = routeRepository.getAllActualRoutes();
    }

    public Route getRoute(int position) {
        return dataset.get(position);
    }

    public int getRoutesSize() {
        return dataset.size();
    }

    public void removeRoute(int position) {
        Route routeToRemove = getRoute(position);
        Log.i(TAG, "Usuwam trasę o id: " + position);
        routeRepository.removeRouteFromDatabase(routeToRemove);
    }

    public void removeRoute(Route route) {
        Log.i(TAG, "Usuwam trasę: " + route.toString());
        routeRepository.removeRouteFromDatabase(route);
    }

}
