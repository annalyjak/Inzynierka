package lyjak.anna.inzynierka.service.model;

import lyjak.anna.inzynierka.view.activities.MainActivity;
import lyjak.anna.inzynierka.viewmodel.MainActivityViewModel;

/**
 * Created by Anna on 21.10.2017.
 */

public final class Tir extends TypeOfTransport {

    private final String name;
    private final String shortName;
    private final int fuel; // liters/100km

    public Tir() {
        fuel = 25;
        if (MainActivityViewModel.language.equals("pl")) {
            name = "Bus powyżej 7,5 tony";
            shortName = "Tir";
        } else {
            name = "Bus over 7,5 tons";
            shortName = "Tir";
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
