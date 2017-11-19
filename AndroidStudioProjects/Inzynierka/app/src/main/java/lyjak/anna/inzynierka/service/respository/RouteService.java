package lyjak.anna.inzynierka.service.respository;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import lyjak.anna.inzynierka.R;
import lyjak.anna.inzynierka.service.model.realm.TempPlannedRoute;
import lyjak.anna.inzynierka.service.model.realm.PlannedRoute;
import lyjak.anna.inzynierka.service.model.realm.PointOfRoute;
import lyjak.anna.inzynierka.service.model.realm.RealmLocation;
import lyjak.anna.inzynierka.service.model.realm.Route;
import lyjak.anna.inzynierka.view.fragments.dummy.TempCreatePolyline;
import lyjak.anna.inzynierka.viewmodel.utils.CreateModelDataUtil;

/**
 * Created by Anna on 14.10.2017.
 */

public class RouteService {

    private final static String TAG = RouteService.class.getSimpleName();
    private static Activity activity;

    public RouteService(Activity activity) {
        this.activity = activity;
    }

    /**
     * user's need to put the title of new PlannedRoute and after that method calls
     * createNewPlannedRoute with 2 arguments (title of the new route and marker)
     */
    public void createNewPlannedRoute(final Marker marker) {
        final Dialog titleDialog = new Dialog(activity, R.style.SettingsDialogStyle);
        titleDialog.setContentView(R.layout.dialog_add_new_planned_route_title);

        titleDialog.setTitle(R.string.dialog_add_new_planned_route_title_title);
        Button enLanguageButton = (Button) titleDialog.findViewById(R.id.buttonNo);
        enLanguageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                titleDialog.dismiss();
                EditText editText = (EditText) titleDialog.findViewById(R.id.editTextTitleOfRoute);
                if (editText.getText() == null) {
                    createNewPlannedRoute("", marker);
                } else {
                    createNewPlannedRoute(editText.getText().toString(), marker);
                }
            }
        });
        titleDialog.show();
    }

    public void createNewPlannedRoute(String title, Marker marker) {
        Realm.init(activity);
        Realm realm = Realm.getDefaultInstance();

        RealmResults<TempPlannedRoute> results = realm.where(TempPlannedRoute.class).findAllAsync();
        results.load();

        if(!results.isEmpty()) { //if already exist -> delate tempPlannedRoute
            realm.beginTransaction();
            PlannedRoute route = results.get(0).getCurrentlyPlannedRoute();
            if (route != null) {
                if (route.getLine().isEmpty()) {
                    calculateLine(route);
                }
            }
            results.deleteAllFromRealm();
            realm.commitTransaction();
        }

        // create new TempPlannedRoute
        RealmLocation location = CreateModelDataUtil.createRelamLocationFromMarker(marker);
        PointOfRoute pointOfRoute = CreateModelDataUtil
                .createNewPointOfRoute(location, 1, marker.getTitle());
        PlannedRoute plannedRoute = CreateModelDataUtil
                .createPlannedRouteWithPoint(title, pointOfRoute);
        TempPlannedRoute newTempRoute = CreateModelDataUtil.createTempPlannedRoute(plannedRoute);

        //store created data in realm
        realm.beginTransaction();
        realm.copyToRealm(newTempRoute);
        realm.commitTransaction();
    }

    public void calculateLine(PlannedRoute route) {
        TempCreatePolyline obj = new TempCreatePolyline(route, activity);
        obj.createPolylinePlannedRoute(route);
    }

    public void storePlannedRouteInDatabase(PlannedRoute route,
                                            TempCreatePolyline calculatedFields) {

        Realm.init(activity);
        Realm realm = Realm.getDefaultInstance();

        PlannedRoute result = realm.where(PlannedRoute.class)
                .equalTo("title", route.getTitle())
                .equalTo("date", route.getDate())
                .findFirst();
        result.load();

        RealmList<RealmLocation> locations =
                createRealmLocationsFromPolylineOption(calculatedFields.getAllPolylineOptions());

        realm.beginTransaction();
        Log.i(TAG, "Zmieniam wartości w bazie");
        result.setDistance(calculatedFields.fulldistance);
        result.setDuration(calculatedFields.fullduration);
        result.clearLine();
        result.addAllToLine(locations);
        realm.commitTransaction();
        Log.i(TAG, "Wartości zmienione");
    }

    private RealmList<RealmLocation> createRealmLocationsFromPolylineOption(List<PolylineOptions> allPolylineOptions) {
        RealmList<RealmLocation> list = new RealmList<>();

        for(PolylineOptions polyline : allPolylineOptions) {
            List<LatLng> latLngs = polyline.getPoints();
            list.addAll(createRelamLocationsFromLatLngs(latLngs));
        }

        return list;
    }

    private List<RealmLocation> createRelamLocationsFromLatLngs(List<LatLng> latLngs) {
        RealmList<RealmLocation> locations = new RealmList<>();
        for(LatLng latlng : latLngs) {
            locations.add(new RealmLocation(latlng.latitude, latlng.longitude));
        }
        return locations;
    }

    public void addPointToActualRoute(Marker marker) {
        TempPlannedRoute tempPlannedRoute;
        int number;

        Realm.init(activity);
        Realm realm = Realm.getDefaultInstance();

        RealmResults<TempPlannedRoute> results = realm.where(TempPlannedRoute.class).findAllAsync();
        results.load();

        if(results.isEmpty()) {
            Log.i(TAG, "results is empty -> przechodzę do tworzenia nowej trasy");
            createNewPlannedRoute(marker);
        } else {
            Log.i(TAG, "result isn't empty -> dodaję nowy punkt");
            tempPlannedRoute = results.get(0);
            if (tempPlannedRoute == null) {
                createNewPlannedRoute(marker);
            } else {
                if (tempPlannedRoute.getCurrentlyPlannedRoute() == null) {
                    // if plannedRoute was removed before ended creation
                    createNewPlannedRoute(marker);
                } else {
                    number = tempPlannedRoute.getCurrentlyPlannedRoute().getPoints().size() + 1;

                    RealmLocation realmLocation = CreateModelDataUtil
                            .createRelamLocationFromMarker(marker);
                    PointOfRoute point = CreateModelDataUtil
                            .createNewPointOfRoute(realmLocation, number, marker.getTitle());

                    realm.beginTransaction();
                    tempPlannedRoute.getCurrentlyPlannedRoute().getPoints().add(point);
                    realm.commitTransaction();
                }
            }
        }
    }

    public void addPointToRoute(Marker marker, PlannedRoute route) {
        Realm.init(activity);
        Realm realm = Realm.getDefaultInstance();

        PlannedRoute result = realm.where(PlannedRoute.class)
                .equalTo("title", route.getTitle())
                .equalTo("duration", route.getDuration())
                .equalTo("distance", route.getDistance())
                .findFirstAsync();
        result.load();

        if(result == null) {
            createNewPlannedRoute(marker);
            Log.i(TAG, "błąd!");
        } else {
            int number = result.getPoints().size() + 1;

            RealmLocation realmLocation = CreateModelDataUtil
                    .createRelamLocationFromMarker(marker);
            PointOfRoute point = CreateModelDataUtil
                    .createNewPointOfRoute(realmLocation, number, marker.getTitle());

            realm.beginTransaction();
            result.getPoints().add(point);
            realm.commitTransaction();

            calculateLine(result);
        }
    }

    public void removeRouteFromDatabase(Route routeToRemove) {
        Realm.init(activity);
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        Route object = realm.where(Route.class)
                .equalTo("date", routeToRemove.getDate())
                .equalTo("startDate", routeToRemove.getStartDate())
                .equalTo("endDate", routeToRemove.getEndDate())
                .findFirstAsync();
        object.deleteFromRealm();
        realm.commitTransaction();
        Log.i(TAG, "Usunięto trasę z bazy");
    }

    public void removePlannedRouteFromDatabase(PlannedRoute routeToRemove) {
        Realm.init(activity);
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        PlannedRoute object = realm.where(PlannedRoute.class)
                .equalTo("title", routeToRemove.getTitle())
                .equalTo("date", routeToRemove.getDate())
                .equalTo("distance", routeToRemove.getDistance())
                .equalTo("duration", routeToRemove.getDuration())
                .findFirstAsync();
        object.deleteFromRealm();
        realm.commitTransaction();
        Log.i(TAG, "Usunięto trasę z bazy");
    }

    public PlannedRoute findPlannedRoute(String title, int distance, int duration) {
        Realm.init(activity);
        Realm realm = Realm.getDefaultInstance();

        PlannedRoute result = realm.where(PlannedRoute.class)
                .equalTo("title", title)
                .equalTo("distance", distance)
                .equalTo("duration", duration)
                .findFirstAsync();
        result.load();
        return result;
    }

    public Route findRoute(Date date, Date startDate, Date endDate) {
        Realm.init(activity);
        Realm realm = Realm.getDefaultInstance();

        Route result = realm.where(Route.class)
                .equalTo("date", date)
                .equalTo("startDate", startDate)
                .equalTo("endDate", endDate)
                .findFirstAsync();
        result.load();
        return result;
    }

    public void swap(List<PointOfRoute> mDataset, int i, int i1) {
        Realm.init(activity);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        mDataset.get(i).setId(i1+1);
        mDataset.get(i1).setId(i+1);
        Collections.swap(mDataset, i, i1);
        realm.commitTransaction();
    }

}