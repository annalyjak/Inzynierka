package lyjak.anna.inzynierka.service.model.realm;

import io.realm.RealmObject;

/**
 * Created by Anna Łyjak on 01.10.2017.
 */

public class PointOfRoute extends RealmObject {

    private int id;
    private RealmLocation point;

    private String name;
    private boolean endPoint;
    private boolean startPoint;

    public PointOfRoute() {
        this.name = "";
        this.endPoint = false;
        this.startPoint = false;
    }

    public PointOfRoute(int id, RealmLocation point) {
        super();
        this.id = id;
        this.point = point;
    }

    public PointOfRoute(int id, RealmLocation point, String name) {
        this.id = id;
        this.point = point;
        this.name = name;
        this.endPoint = false;
        this.startPoint = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RealmLocation getPoint() {
        return point;
    }

    public void setPoint(RealmLocation point) {
        this.point = point;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStartPoint() {
        return startPoint;
    }

    public void setStartPoint(boolean startPoint) {
        this.startPoint = startPoint;
    }

    public boolean isEndPoint() {
        return endPoint;
    }

    public void setEndPoint(boolean endPoint) {
        this.endPoint = endPoint;
    }

}
