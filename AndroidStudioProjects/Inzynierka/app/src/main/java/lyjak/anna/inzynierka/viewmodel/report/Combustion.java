package lyjak.anna.inzynierka.viewmodel.report;

import lyjak.anna.inzynierka.model.reports.Currency;
import lyjak.anna.inzynierka.model.reports.TypeOfTransport;

/**
 * Created by Anna on 21.10.2017.
 */

public class Combustion {

    public static final int PLANNED_ROUTE = 1;
    public static final int ACTUAL_ROUTE = 2;
    private static int TYPE_OF_COUNTING = PLANNED_ROUTE;
    private final int HUNDRED_KM = 100;

    public static Double mFuelCost = 5.0;
    public static Currency mFuelCostCurrency = Currency.PLN;

    private Integer mDistance; // in km
    private Integer mTypeOfTransportConverter; //...liter per 100km
    private Integer mBurnedFuel; // all burned fuel in liter
    private Double mAllRouteCost; // all cost of route
    private Integer mCombustionAmount; //...liter per 100km - calculated combustion - spalanie


    public int getDistance() {
        return mDistance;
    }

    public void setDistance(int distance) {
        this.mDistance = distance;
    }

    public void setTypeOfTransportConverter(TypeOfTransport typeOfTransportConverter) {
        this.mTypeOfTransportConverter = typeOfTransportConverter.getAmountOfFuelFor100km();
    }

    public void setBurnedFuel(Integer mBurnedFuel) {
        this.mBurnedFuel = mBurnedFuel;
    }

    public void setTypeOfTransportConverter(int typeOfTransportConverter) {
        this.mTypeOfTransportConverter = typeOfTransportConverter;
    }

    public String getCombustionAmount() {
        return (mCombustionAmount == null? mTypeOfTransportConverter:  mCombustionAmount)
                + "l /" + HUNDRED_KM + " km";
    }

    public int getBurnedFuel() {
        return mBurnedFuel;
    }

    public void setBurnedFuel(int mBurnedFuel) {
        this.mBurnedFuel = mBurnedFuel;
    }

    public String calculateCombustionAmount(int TYPE_OF_COUNTING) {
        mAllRouteCost = null;
        if (TYPE_OF_COUNTING > 2 || TYPE_OF_COUNTING < 1) {
            TYPE_OF_COUNTING = this.TYPE_OF_COUNTING;
        }
        if (TYPE_OF_COUNTING == ACTUAL_ROUTE) {
            mAllRouteCost = (mBurnedFuel==null? 0 : mBurnedFuel) * mFuelCost;
            if (mBurnedFuel==null || mDistance == null) {
                mCombustionAmount = 0;
            } else {
                mCombustionAmount = (mBurnedFuel * HUNDRED_KM) / (mDistance);
            }
        }
        if (TYPE_OF_COUNTING == PLANNED_ROUTE) {
            mAllRouteCost = mFuelCost *
                    (mTypeOfTransportConverter==null? 0 : mTypeOfTransportConverter) *
                    ((mDistance==null? 0 : mDistance) / HUNDRED_KM);
        }
        if (mAllRouteCost == null) {
            mAllRouteCost = 0.0;
        }

        return mAllRouteCost.toString() + " " + mFuelCostCurrency;
    }
}
