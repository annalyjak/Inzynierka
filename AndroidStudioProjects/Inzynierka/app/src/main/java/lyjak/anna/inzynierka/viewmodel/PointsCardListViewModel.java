package lyjak.anna.inzynierka.viewmodel;

import android.content.Context;
import android.util.Log;

import java.util.List;

import lyjak.anna.inzynierka.model.realmObjects.PlannedRoute;
import lyjak.anna.inzynierka.model.realmObjects.PointOfRoute;

/**
 * Created by Anna on 19.11.2017.
 */

public class PointsCardListViewModel extends MainViewModel {

    private static final String TAG = PointsCardListViewModel.class.getSimpleName();

    private PlannedRoute route;
    private List<PointOfRoute> dataset;

    public PointsCardListViewModel(Context context, PlannedRoute route) {
        super(context);
        this.route = route;
        this.dataset = route.getPoints();
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
