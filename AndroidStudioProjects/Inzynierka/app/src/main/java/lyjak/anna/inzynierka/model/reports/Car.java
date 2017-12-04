package lyjak.anna.inzynierka.model.reports;

import lyjak.anna.inzynierka.viewmodel.MainActivityViewModel;

/**
 * Created by Anna on 21.10.2017.
 */

public final class Car extends TypeOfTransport {

    private final String name;
    private final String shortName;
    private final int fuel; // liters/100km

    public Car() {
        fuel = 7;
        if (MainActivityViewModel.language.equals("pl")) {
            name = "Samochód osobowy";
            shortName = "Samochód osobowy";
        } else {
            name = "Car";
            shortName = "Car";
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
