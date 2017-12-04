package lyjak.anna.inzynierka.model.realmObjects;

import io.realm.RealmObject;

/**
 * Created by Anna Łyjak on 01.10.2017.
 */

public class HistoricalReports extends RealmObject {

    private PlannedRoute plannedRoute;
    private Route actualRoute;
    private String filePath;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public PlannedRoute getPlannedRoute() {
        return plannedRoute;
    }

    public void setPlannedRoute(PlannedRoute plannedRoute) {
        this.plannedRoute = plannedRoute;
    }

    public Route getActualRoute() {
        return actualRoute;
    }

    public void setActualRoute(Route actualRoute) {
        this.actualRoute = actualRoute;
    }
}
