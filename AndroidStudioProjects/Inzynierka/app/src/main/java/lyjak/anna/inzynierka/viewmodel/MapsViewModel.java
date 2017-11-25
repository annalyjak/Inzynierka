package lyjak.anna.inzynierka.viewmodel;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lyjak.anna.inzynierka.service.model.realm.PlannedRoute;
import lyjak.anna.inzynierka.service.model.realm.PointOfRoute;
import lyjak.anna.inzynierka.service.model.realm.RealmLocation;
import lyjak.anna.inzynierka.service.model.realm.Route;
import lyjak.anna.inzynierka.view.activities.MapsActivity;
import lyjak.anna.inzynierka.viewmodel.report.GenerateActualRouteReport;
import lyjak.anna.inzynierka.viewmodel.report.GeneratePlannedRouteReport;
import lyjak.anna.inzynierka.viewmodel.report.GenerateStandardReport;
import lyjak.anna.inzynierka.viewmodel.report.modelDTO.ActualRouteForReportDTO;
import lyjak.anna.inzynierka.viewmodel.report.modelDTO.PlannedRouteForReportDTO;

/**
 *
 * Created by Anna on 24.11.2017.
 */

public class MapsViewModel extends MainViewModel {

    // List of temp markers of user's founded localization
    private final List<Marker> mListOfMarkers = new ArrayList<>();

    public static GenerateStandardReport report;
    public static GenerateActualRouteReport reportAcctualRoute;
    public static GeneratePlannedRouteReport reportPlannedRoute;
    private Boolean generate;

    private PlannedRoute savePlannedRoute;
    private Route saveRoute;

    public MapsViewModel(Context context) {
        super(context);
        if(report != null) {
            saveRoute = report.getActualRoute();
            savePlannedRoute = report.getPlannedRoute();
        }
        if(reportAcctualRoute != null) {
            saveRoute = reportAcctualRoute.getActualRoute();
        }
        if(reportPlannedRoute != null) {
            savePlannedRoute = reportPlannedRoute.getPlannedRoute();
        }
    }

    public MapsViewModel(GenerateStandardReport report, Context context) {
        super(context);
        this.report = report;
    }


    public void addPointToActualRoute(Marker marker) {
        if (savePlannedRoute != null) {
            routeService.addPointToRoute(marker, savePlannedRoute);
        } else {
            routeService.addPointToActualRoute(marker);
        }
    }

    public void createNewPlannedRoute(Marker marker) {
        routeService.createNewPlannedRoute(marker);
    }

    public List<LatLng> createLatListFromLocations(List<RealmLocation> line) {
        List<LatLng> result = new ArrayList<>();
        for (RealmLocation location : line) {
            result.add(new LatLng(location.getLatitude(), location.getLongitude()));
        }
        return result;
    }

    public List<LatLng> createLatListFromSavedLocations() {
        return createLatListFromLocations((!savePlannedRoute.isValid() || savePlannedRoute.getLine()== null)? new ArrayList<>()
                : savePlannedRoute.getLine());
    }

    public void createLine() {
        if(savePlannedRoute.isValid()) {
            routeService.calculateLine(savePlannedRoute);
            savePlannedRoute = routeService.findPlannedRoute(
                    savePlannedRoute.getTitle(),
                    savePlannedRoute.getDistance(),
                    savePlannedRoute.getDuration());
        }
    }

    public void findPlannedRoute(String title, int distance, int duration) {
        savePlannedRoute = routeService.findPlannedRoute(title, distance, duration);
    }

    public void findRoute(Date date, Date startDate, Date endDate) {
        saveRoute = routeService.findRoute(date, startDate, endDate);
    }

    public boolean isPlannedRouteNull() {
        return savePlannedRoute == null;
    }

    public boolean isActuallRouteNull() {
        return saveRoute == null;
    }

    public PlannedRoute getSavePlannedRoute() {
        return savePlannedRoute;
    }

    public Route getSavedRoute() {
        return saveRoute;
    }

    public List<PointOfRoute> getSavedPlannedRoutePoints() {
        if (savePlannedRoute.isValid() && savePlannedRoute.getPoints().isValid()) {
            return savePlannedRoute.getPoints();
        } else {
            return null;
        }
    }

    public List<RealmLocation> getSavedRouteLocations() {
        return saveRoute.getLocations();
    }

    public boolean isReportMode() {
        return report != null;
    }

    public void doReport(MapsActivity activity, Context applicationContext, Bitmap bitmap) {
        report.createPdf(activity, applicationContext, bitmap);
        report = null;
        savePlannedRoute = null;
        saveRoute = null;
    }

    public boolean isActualRouteReportMode() {
        return reportAcctualRoute != null;
    }

    public void doActualRouteReport(MapsActivity activity, Bitmap bitmap) {
        reportAcctualRoute.createPdf(activity, bitmap);
        reportAcctualRoute = null;
        savePlannedRoute = null;
        saveRoute = null;
    }

    public boolean isPlannedRouteReportMode() {
        return reportPlannedRoute != null;
    }

    public void doPlannedRouteReport(MapsActivity activity, Bitmap bitmap) {
        reportPlannedRoute.createPdf(activity, bitmap);
        reportPlannedRoute = null;
        savePlannedRoute = null;
        saveRoute = null;
    }

    public void addToMarkersList(Marker marker) {
        mListOfMarkers.add(marker);
    }

    public ArrayList<String> getLatitudes() {
        ArrayList<String> result = new ArrayList<>();
        for (Marker mark: mListOfMarkers) {
            result.add(String.valueOf(mark.getPosition().latitude));
        }
        return result;
    }

    public ArrayList<String> getLongitudes() {
        ArrayList<String> result = new ArrayList<>();
        for (Marker mark: mListOfMarkers) {
            result.add(String.valueOf(mark.getPosition().longitude));
        }
        return result;
    }

    public void removeMarkerFromList(Marker tempMarker) {
        mListOfMarkers.remove(tempMarker);
    }

    public void includeMarkersToBuilder(LatLngBounds.Builder builder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mListOfMarkers.stream().forEach(marker -> {
                builder.include(marker.getPosition());
            });
        } else {
            for (Marker marker : mListOfMarkers) {
                builder.include(marker.getPosition());
            }
        }
    }

    public void includeLatLngToBuilder(LatLngBounds.Builder builder, List<LatLng> line) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            line.stream().forEach(li -> {
                builder.include(li);
            });
        } else {
            for (LatLng li : line) {
                builder.include(li);
            }
        }
    }

    public void setGenerate(boolean generate) {
        this.generate = generate;
    }

    public boolean isGenerated() {
        if (generate != null) {
            return generate == true;
        }
        else {
            return false;
        }
    }

    public void findPlannedAndAcctualRoute(String title, int distance, int duration, Date date, Date startDate, Date endDate) {
        findPlannedRoute(title,distance,duration);
        findRoute(date, startDate, endDate);
    }
}
