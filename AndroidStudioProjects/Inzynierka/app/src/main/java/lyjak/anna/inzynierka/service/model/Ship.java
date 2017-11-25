package lyjak.anna.inzynierka.service.model;

import lyjak.anna.inzynierka.viewmodel.MainActivityViewModel;

/**
 * Created by Anna on 25.11.2017.
 */

public class Ship extends TypeOfTransport {

    private final String name;
    private final String shortName;
    private final int fuel; // liters/100km

    public Ship() {
        fuel = 0;
        if (MainActivityViewModel.language.equals("pl")) {
            name = "Statek";
            shortName = "Statek";
        } else {
            name = "Ship";
            shortName = "Ship";
        }
    }

    @Override
    public String getShortName() {
        return shortName;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getAmountOfFuelFor100km() {
        return fuel;
    }
}
