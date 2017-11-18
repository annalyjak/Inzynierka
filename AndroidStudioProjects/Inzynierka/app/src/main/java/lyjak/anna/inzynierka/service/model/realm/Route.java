package lyjak.anna.inzynierka.service.model.realm;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Required;

/**
 * Created by Anna Łyjak on 21.09.2017.
 */

public class Route extends RealmObject {

    @Required
    private Date date;

    private Date startDate;
    private Date endDate;

    private RealmList<RealmLocation> locations;

    public Route() {
        this.date = new Date(System.currentTimeMillis());
        this.locations = new RealmList<>();
    }

    public void addLocationToList(RealmLocation rl) {
        this.locations.add(rl);
    }

    public RealmList<RealmLocation> getLocations() {
        return locations;
    }

    public void setLocations(RealmList<RealmLocation> locations) {
        this.locations = locations;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String toString() {
        return "Route started " + startDate.toString() + " and ended " + (endDate==null? "" : endDate.toString()) +
                ", size: " + String.valueOf(locations.size());
    }
}
