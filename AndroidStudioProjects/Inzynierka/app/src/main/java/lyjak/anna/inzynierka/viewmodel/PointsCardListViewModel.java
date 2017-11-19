package lyjak.anna.inzynierka.viewmodel;

import android.app.Activity;
import android.util.Log;

import java.util.List;

import lyjak.anna.inzynierka.service.model.realm.PlannedRoute;
import lyjak.anna.inzynierka.service.model.realm.PointOfRoute;
import lyjak.anna.inzynierka.service.respository.RouteService;
import lyjak.anna.inzynierka.view.fragments.PointsFragment;

/**
 * Created by Anna on 19.11.2017.
 */

public class PointsCardListViewModel {

    private static final String TAG = PointsCardListViewModel.class.getSimpleName();

    private static RouteService routeService;
    private PlannedRoute route;
    private List<PointOfRoute> dataset;

    public PointsCardListViewModel(Activity activity, PlannedRoute route) {
        this.route = route;
        this.dataset = route.getPoints();
        routeService = new RouteService(activity);
    }

    public PointsCardListViewModel(Activity activity, PlannedRoute route, List<PointOfRoute> dataset) {
        this.route = route;
        this.dataset = dataset;
        routeService = new RouteService(activity);
    }

    public void onCardMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                routeService.swap(dataset, i, i + 1);
                routeService.calculateLine(route);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                routeService.swap(dataset, i, i - 1);
                routeService.calculateLine(route);
            }
        }
    }

    public int getDatasetSize() {
        return dataset.size();
    }

    public PointOfRoute getPoint(int position) {
        return dataset.get(position);
    }

    public void removePoint(int position) {
        dataset.remove(position);
    }

    public void setDataset(List<PointOfRoute> list) {
        if (dataset == null) {
            Log.i(TAG, "ustawiam nową listę " + list.size());
            dataset = list;
        } else {
            Log.i(TAG, "dodaję elementy " + list.size());
            this.dataset.addAll(list);
        }
    }
}
