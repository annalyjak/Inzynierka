package lyjak.anna.inzynierka.model.repository;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
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
import lyjak.anna.inzynierka.model.realmObjects.ActualPlannedRoute;
import lyjak.anna.inzynierka.model.realmObjects.HistoricalReport;
import lyjak.anna.inzynierka.model.realmObjects.PlannedRoute;
import lyjak.anna.inzynierka.model.realmObjects.PointOfRoute;
import lyjak.anna.inzynierka.model.realmObjects.RealmLocation;
import lyjak.anna.inzynierka.model.realmObjects.Route;
import lyjak.anna.inzynierka.model.utils.CreateModelDataUtil;
import lyjak.anna.inzynierka.model.findRoute.DirectionFinder;

/**
 * Created by Anna on 14.10.2017.
 */
public class RouteRepository implements IRouteRepository {

    private final static String TAG = RouteRepository.class.getSimpleName();

    private Context context;

    public RouteRepository(Context context) {
        this.context = context;
    }

    /**
     * user's need to put the title of new PlannedRoute and after that method calls
     * createNewPlannedRoute with 2 arguments (title of the new route and marker)
     */
    @Deprecated
    public void createNewPlannedRoute(final Marker marker) {
        final Dialog titleDialog = new Dialog(context, R.style.SettingsDialogStyle);
        titleDialog.setContentView(R.layout.dialog_add_new_planned_route_title);

        titleDialog.setTitle(R.string.dialog_add_new_planned_route_title_title);
        Button enLanguageButton = (Button) titleDialog.findViewById(R.id.buttonOK);
        enLanguageButton.setOnClickListener(v -> {
            titleDialog.dismiss();
            EditText editText = (EditText) titleDialog.findViewById(R.id.editTextTitleOfRoute);
            if (editText.getText() == null) {
                createNewPlannedRoute("", marker);
            } else {
                createNewPlannedRoute(editText.getText().toString(), marker);
            }
        });
        titleDialog.show();
    }

    @Override
    public void createNewPlannedRoute(String title, Marker marker) {
        Realm.init(context);
        Realm realm = Realm.getDefaultInstance();

        RealmResults<ActualPlannedRoute> results = realm.where(ActualPlannedRoute.class)
                .findAllAsync();
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

        // create new ActualPlannedRoute
        RealmLocation location = CreateModelDataUtil.createRelamLocationFromMarker(marker);
        PointOfRoute pointOfRoute = CreateModelDataUtil
                .createNewPointOfRoute(location, 1, marker.getTitle());
        PlannedRoute plannedRoute = CreateModelDataUtil
                .createPlannedRouteWithPoint(title, pointOfRoute);
        ActualPlannedRoute newActualPlannedRoute = CreateModelDataUtil
                .createActualPlannedRoute(plannedRoute);

        //store created data in realm
        realm.beginTransaction();
        realm.copyToRealm(newActualPlannedRoute);
        realm.commitTransaction();
    }

    @Override
    public void calculateLine(PlannedRoute route) {
        DirectionFinder obj = new DirectionFinder(route, context);
        obj.createPolylinePlannedRoute(route);
    }

    @Override
    public void storePlannedRouteInDatabase(PlannedRoute route,
                                            DirectionFinder calculatedFields) {
        Realm.init(context);
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

    @Override
    public void addPointToActualRoute(Marker marker) {
        ActualPlannedRoute actualPlannedRoute;
        int number;

        Realm.init(context);
        Realm realm = Realm.getDefaultInstance();

        RealmResults<ActualPlannedRoute> results = realm.where(ActualPlannedRoute.class).findAllAsync();
        results.load();

        if(results.isEmpty()) {
            Log.i(TAG, "results is empty -> przechodzę do tworzenia nowej trasy");
            createNewPlannedRoute(marker);
        } else {
            Log.i(TAG, "result isn't empty -> dodaję nowy punkt");
            actualPlannedRoute = results.get(0);
            if (actualPlannedRoute == null) {
                createNewPlannedRoute(marker);
            } else {
                if (actualPlannedRoute.getCurrentlyPlannedRoute() == null) {
                    // if plannedRoute was removed before ended creation
                    createNewPlannedRoute(marker);
                } else {
                    number = actualPlannedRoute.getCurrentlyPlannedRoute().getPoints().size() + 1;

                    RealmLocation realmLocation = CreateModelDataUtil
                            .createRelamLocationFromMarker(marker);
                    PointOfRoute point = CreateModelDataUtil
                            .createNewPointOfRoute(realmLocation, number, marker.getTitle());

                    realm.beginTransaction();
                    actualPlannedRoute.getCurrentlyPlannedRoute().getPoints().add(point);
                    realm.commitTransaction();
                }
            }
        }
    }

    @Override
    public void addPointToRoute(Marker marker, PlannedRoute route) {
        Realm.init(context);
        Realm realm = Realm.getDefaultInstance();

        PlannedRoute result = realm.where(PlannedRoute.class)
                .equalTo("title", route.getTitle())
                .equalTo("duration", route.getDuration())
                .equalTo("distance", route.getDistance())
                .findFirstAsync();
        result.load();

        int number = result.getPoints().size() + 1;
        RealmLocation realmLocation = CreateModelDataUtil
                .createRelamLocationFromMarker(marker);
        PointOfRoute point = CreateModelDataUtil
                .createNewPointOfRoute(realmLocation, number, marker.getTitle());
        realm.beginTransaction();
        result.getPoints().add(point);
        realm.commitTransaction();
        Log.i("", "Wykonuję calculateLine");
        calculateLine(result);
    }

    @Override
    public void removeRouteFromDatabase(Route routeToRemove) {
        Realm.init(context);
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

    @Override
    public void removePlannedRouteFromDatabase(PlannedRoute routeToRemove) {
        Realm.init(context);
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

    @Override
    public PlannedRoute findPlannedRoute(String title, int distance, int duration) {
        Realm.init(context);
        Realm realm = Realm.getDefaultInstance();

        PlannedRoute result = realm.where(PlannedRoute.class)
                .equalTo("title", title)
                .equalTo("distance", distance)
                .equalTo("duration", duration)
                .findFirstAsync();
        result.load();
        return result;
    }

    @Override
    public Route findRoute(Date date, Date startDate, Date endDate) {
        Realm.init(context);
        Realm realm = Realm.getDefaultInstance();

        Route result = realm.where(Route.class)
                .equalTo("date", date)
                .equalTo("startDate", startDate)
                .equalTo("endDate", endDate)
                .findFirstAsync();
        result.load();
        return result;
    }

    @Override
    public void swap(List<PointOfRoute> mDataset, int i, int i1) {
        Realm.init(context);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        mDataset.get(i).setId(i1+1);
        mDataset.get(i1).setId(i+1);
        Collections.swap(mDataset, i, i1);
        realm.commitTransaction();
    }

    @Override
    public List<PlannedRoute> getAllPlannedRoutes() {
        Realm.init(context);
        Realm realm = Realm.getDefaultInstance();
        RealmResults<PlannedRoute> results = realm.where(PlannedRoute.class).findAllAsync();
        results.load();
        return results;
    }

    @Override
    public List<Route> getAllActualRoutes() {
        Realm.init(context);
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Route> results = realm.where(Route.class).findAllAsync();
        results.load();
        return results;
    }

    @Override
    public List<HistoricalReport> getAllHistoricalReports() {
        Realm.init(context);
        Realm realm = Realm.getDefaultInstance();
        RealmResults<HistoricalReport> results = realm.where(HistoricalReport.class)
                .findAllAsync();
        results.load();
        return results;
    }

    @Override
    public void createReportInDatabase(PlannedRoute plannedRoute, Route route, String filePath) {
        Realm.init(context);
        Realm realm = Realm.getDefaultInstance();
        HistoricalReport historicalReport = CreateModelDataUtil
                .createHistoricalReport(plannedRoute, route, filePath);
        //store created data in realm
        realm.beginTransaction();
        realm.copyToRealm(historicalReport);
        realm.commitTransaction();
    }


    public void changePoint(PointOfRoute pointOfRoute, int distance, int duration) {
        Realm.init(context);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        pointOfRoute.setDistance(distance);
        pointOfRoute.setDuration(duration);
        realm.commitTransaction();
    }
}
