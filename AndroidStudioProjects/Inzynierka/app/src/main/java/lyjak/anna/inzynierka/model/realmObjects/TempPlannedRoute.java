package lyjak.anna.inzynierka.model.realmObjects;

import io.realm.RealmObject;

/**
 * Created by Anna Łyjak on 01.10.2017.
 */

public class TempPlannedRoute extends RealmObject {

    private PlannedRoute currentlyPlannedRoute;

    public PlannedRoute getCurrentlyPlannedRoute() {
        return currentlyPlannedRoute;
    }

    public void setCurrentlyPlannedRoute(PlannedRoute currentlyPlannedRoute) {
        this.currentlyPlannedRoute = currentlyPlannedRoute;
    }

}
