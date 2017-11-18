package lyjak.anna.inzynierka.service.model.realm;

import io.realm.RealmObject;

/**
 * Created by Anna Łyjak on 01.10.2017.
 */

public class HistoricalReports extends RealmObject {

    private PlannedRoute plannedRoute;
    private Route actualRoute;

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
