package lyjak.anna.inzynierka.viewmodel;

import android.content.Context;

import javax.inject.Singleton;

import lyjak.anna.inzynierka.service.respository.RouteService;

/**
 * Created by Anna on 19.11.2017.
 */

public class MainViewModel {

    @Singleton
    RouteService service;

    public MainViewModel(Context context) {
        service = new RouteService(context);
    }
}
