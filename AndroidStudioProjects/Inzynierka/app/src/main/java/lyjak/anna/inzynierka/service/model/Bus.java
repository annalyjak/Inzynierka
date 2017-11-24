package lyjak.anna.inzynierka.service.model;

import lyjak.anna.inzynierka.view.activities.MainActivity;
import lyjak.anna.inzynierka.viewmodel.MainActivityViewModel;

/**
 * Created by Anna on 21.10.2017.
 */

public final class Bus extends TypeOfTransport {

    private final String name;
    private final String shortName;
    private final int fuel; // liters/100km

    public Bus() {
        fuel = 12;
        if (MainActivityViewModel.language.equals("pl")) {
            name = "Bus do 7,5 tony";
            shortName = "Bus";
        } else {
            name = "Bus up to 7,5 tons";
            shortName = "Bus";
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
