package lyjak.anna.inzynierka.viewmodel;

import android.content.Context;

import javax.inject.Singleton;

import lyjak.anna.inzynierka.model.repository.RouteRepository;
import lyjak.anna.inzynierka.model.repository.RouteService;

/**
 * Created by Anna on 19.11.2017.
 */

public class MainViewModel {

    @Singleton
    RouteRepository routeService;

    public MainViewModel(Context context) {
        routeService = new RouteService(context);
    }
}
