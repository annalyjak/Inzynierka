package lyjak.anna.inzynierka.service.model;

import lyjak.anna.inzynierka.viewmodel.MainActivityViewModel;

/**
 * Created by Anna on 25.11.2017.
 */

public class Railway extends TypeOfTransport {

    private final String name;
    private final String shortName;
    private final int fuel; // liters/100km

    public Railway() {
        fuel = 0;
        if (MainActivityViewModel.language.equals("pl")) {
            name = "Pociąg";
            shortName = "Pociąg";
        } else {
            name = "Railway";
            shortName = "Railway";
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
