package lyjak.anna.inzynierka.viewmodel.report;

/**
 * Created by Anna on 21.10.2017.
 */

public class TimeTripInfo {

    private int mTripNumberOfDays;
    private double mBreakNumberOfHours;

    public int getTripNumberOfDays() {
        return mTripNumberOfDays;
    }

    public void setTripNumberOfDays(int tripNumberOfDays) {
        this.mTripNumberOfDays = tripNumberOfDays;
    }

    public double getBreakNumberOfHours() {
        return mBreakNumberOfHours;
    }

    public void setBreakNumberOfHours(double breakNumberOfHours) {
        this.mBreakNumberOfHours = breakNumberOfHours;
    }
}
