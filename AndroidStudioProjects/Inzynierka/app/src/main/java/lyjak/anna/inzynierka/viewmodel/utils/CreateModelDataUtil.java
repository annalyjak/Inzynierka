package lyjak.anna.inzynierka.viewmodel.utils;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

import lyjak.anna.inzynierka.service.model.realm.TempPlannedRoute;
import lyjak.anna.inzynierka.service.model.realm.PlannedRoute;
import lyjak.anna.inzynierka.service.model.realm.PointOfRoute;
import lyjak.anna.inzynierka.service.model.realm.RealmLocation;

/**
 * Created by Anna on 14.10.2017.
 */

public class CreateModelDataUtil {

    private static final String TAG = CreateModelDataUtil.class.getSimpleName();

    public static RealmLocation createRelamLocationFromMarker(Marker marker) {
        Log.i(TAG, "Start to create new RealmLocation");
        RealmLocation result = new RealmLocation();
        result.setLatitude(marker.getPosition().latitude);
        result.setLongitude(marker.getPosition().longitude);
        Log.i(TAG, "New RealmLocation created");
        return result;
    }

    public static PointOfRoute createNewPointOfRoute(
            RealmLocation location,
            int number,
            String name) {

        Log.i(TAG, "Start to create new RointOfRoute");
        PointOfRoute result = new PointOfRoute();
        result.setPoint(location);
        result.setId(number);
        if (name != null) {
            result.setName(name);
        }
        Log.i(TAG, "New RointOfRoute created");
        return result;
    }

    public static PlannedRoute createPlannedRoute(String title) {
        Log.i(TAG, "Start to create new PlannedRoute");
        PlannedRoute plannedRoute = new PlannedRoute();
        if (title != null) {
            plannedRoute.setTitle(title);
        }
        Log.i(TAG, "New PlannedRoute created");
        return plannedRoute;
    }

    public static PlannedRoute createPlannedRouteWithPoint(String title, PointOfRoute pointOfRoute) {
        PlannedRoute plannedRoute = createPlannedRoute(title);
        plannedRoute.addPointOfRoute(pointOfRoute);
        Log.i(TAG, "PointOfRoute added to new PlannedRoute");
        return plannedRoute;
    }

    public static TempPlannedRoute createTempPlannedRoute(PlannedRoute plannedRoute) {
        Log.i(TAG, "Start to create new TempPlannedRoute");
        TempPlannedRoute result = new TempPlannedRoute();
        if (plannedRoute != null) {
            result.setCurrentlyPlannedRoute(plannedRoute);
        }
        Log.i(TAG, "New TempPlannedRoute created");
        return result;
    }

    public static List<LatLng> transferListOfRealmLocationsToLatLng(List<RealmLocation> locations) {
        List<LatLng> result = new ArrayList<>();
        for (RealmLocation realmLocation : locations) {
            result.add(new LatLng(realmLocation.getLatitude(), realmLocation.getLongitude()));
        }
        return result;
    }
}
