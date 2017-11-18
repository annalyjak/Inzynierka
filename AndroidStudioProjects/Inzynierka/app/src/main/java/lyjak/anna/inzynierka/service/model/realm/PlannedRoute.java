package lyjak.anna.inzynierka.service.model.realm;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Required;

/**
 * Created by Anna Łyjak on 01.10.2017.
 */

public class PlannedRoute extends RealmObject implements Serializable {

    private String title;

    @Required
    private Date date;

    private RealmList<PointOfRoute> points;

    private RealmList<RealmLocation> line;

    private int distance;

    private int duration;

    public PlannedRoute() {
        this.title = "";
        this.date = new Date(System.currentTimeMillis());
        this.points = new RealmList<>();
        this.line = new RealmList<>();
        this.distance = 0;
        this.duration = 0;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public RealmList<PointOfRoute> getPoints() {
        return points;
    }

    public void setPoints(RealmList<PointOfRoute> points) {
        this.points = points;
    }

    public RealmList<RealmLocation> getLine() {
        return line;
    }

    public void clearLine() {
        this.line.clear();
    }

    public void addToLine(RealmLocation location) {
        this.line.add(location);
    }

    public void addAllToLine(RealmList<RealmLocation> locations) {
        this.line.addAll(locations);
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void addPointOfRoute(PointOfRoute point) {
        this.points.add(point);
    }

    public int getSize() {
        return this.points.size();
    }
}
