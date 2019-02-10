package lyjak.anna.inzynierka.model.reports;

import lyjak.anna.inzynierka.viewmodel.MainActivityViewModel;

/**
 * Created by Anna on 25.11.2017.
 */

public class Plane extends TypeOfTransport {

    private final String name;
    private final String shortName;
    private final int fuel; // liters/100km

    public Plane() {
        fuel = 0;
        if (MainActivityViewModel.language.equals("pl")) {
            name = "Samolot";
            shortName = "Samolot";
        } else {
            name = "Plane";
            shortName = "Plane";
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
