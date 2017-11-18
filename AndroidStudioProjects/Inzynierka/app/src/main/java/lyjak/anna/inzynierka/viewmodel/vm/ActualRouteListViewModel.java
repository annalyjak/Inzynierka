package lyjak.anna.inzynierka.viewmodel.vm;


import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import java.util.List;

import lyjak.anna.inzynierka.service.model.realm.Route;
import lyjak.anna.inzynierka.service.respository.RouteRepository;

/**
 * Created by Anna on 18.11.2017.
 */

public class ActualRouteListViewModel extends AndroidViewModel {
    private final LiveData<List<Route>> routeListObservable;
    private RouteRepository repository;

    public ActualRouteListViewModel(@NonNull RouteRepository routeRepository, @NonNull Application application) {
        super(application);
        this.repository = routeRepository;
        routeListObservable = routeRepository.getRouteList("Routes");
    }

    public ActualRouteListViewModel(@NonNull Activity activity) {
        super(activity.getApplication());
        routeListObservable = RouteRepository.getInstance(activity).getRouteList("Routes");
    }

    public LiveData<List<Route>> getRouteListObservable() {
        return routeListObservable;
    }

    public void removeRoute(Route route) {
        repository.remove(route);
    }

    /**
     * A creator is used to inject the project ID into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Activity activity;

        public Factory(@NonNull Activity activity) {
            this.activity = activity;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ActualRouteListViewModel(activity);
        }
    }
}
