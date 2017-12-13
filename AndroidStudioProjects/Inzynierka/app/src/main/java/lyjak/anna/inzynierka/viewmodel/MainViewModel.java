package lyjak.anna.inzynierka.viewmodel;

import android.content.Context;

import javax.inject.Singleton;

import lyjak.anna.inzynierka.model.repository.IRouteRepository;
import lyjak.anna.inzynierka.model.repository.RouteRepository;

/**
 * Created by Anna on 19.11.2017.
 */

public class MainViewModel {

    @Singleton
    IRouteRepository routeRepository;

    public MainViewModel(Context context) {
        routeRepository = new RouteRepository(context);
    }
}
