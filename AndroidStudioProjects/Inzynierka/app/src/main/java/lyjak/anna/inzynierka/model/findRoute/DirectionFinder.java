package lyjak.anna.inzynierka.model.findRoute;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import lyjak.anna.inzynierka.model.realmObjects.PlannedRoute;
import lyjak.anna.inzynierka.model.realmObjects.PointOfRoute;
import lyjak.anna.inzynierka.model.realmObjects.RealmLocation;
import lyjak.anna.inzynierka.model.repository.RouteService;
import lyjak.anna.inzynierka.viewmodel.others.RouteBeetweenTwoPointsDTO;

public class DirectionFinder implements FindDirectionListener {

    private static final String TAG = DirectionFinder.class.getSimpleName();

    public int fulldistance = 0; // the number of kilometers variable
    public int fullduration = 0; // full time to overcome actuall route variable

    private List<MarkerOptions> originMarkers = new ArrayList<>(); // temporary list of started markers
    private List<MarkerOptions> destinationMarkers = new ArrayList<>(); //temporary list of destiation markers
    private List<Polyline> polylinePaths = new ArrayList<>(); // temporary list of polylines

    private List<List<MarkerOptions>> allOriginMarkers = new ArrayList<>(); // list of all started markers
    private List<List<MarkerOptions>> alldestinationMarkers = new ArrayList<>(); // list of all destination markers
    private List<PolylineOptions> allPolylineOptions = new ArrayList<>(); // list of all polylinesOptions

    private List<List<Polyline>> allpolylinePaths = new ArrayList<>(); // list of all polylines

    private PlannedRoute route;
    private Context context;

    public DirectionFinder(PlannedRoute route, Context context) {
        this.route = route;
        this.context = context;
    }

    @Override
    public void onStartFindDirection() {
        Log.i(TAG, "onStartFindDirection");
        // remove origin, destination and polyline markers between 2 points of route
        // and put it into one list for all route
        if (originMarkers != null) {
            allOriginMarkers.add(originMarkers);
        }
        if (destinationMarkers != null) {
            alldestinationMarkers.add(destinationMarkers);
        }
        if (polylinePaths != null) {
            allpolylinePaths.add(polylinePaths);
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
        Log.i(TAG, "onStartFindDirection ended");
    }

    @Override
    public void onSucceedFindDirection(List<RouteBeetweenTwoPointsDTO> routes) {
        Log.i(TAG, "onSucceedFindDirection");
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        RealmList<PointOfRoute> points = route.getPoints();
        for (RouteBeetweenTwoPointsDTO route : routes) {
            for (PointOfRoute pointOfRoute: points) {
                if(pointOfRoute.getPoint().getLatitude() == route.getOriginStartPlace().latitude &&
                        pointOfRoute.getPoint().getLongitude() == route.getOriginStartPlace()
                                .longitude) {
                    Log.i(TAG, "Ustawiam wielkości distance and duration");
                    RouteService routeRepository = new RouteService(context);
                    routeRepository.changePoint(pointOfRoute,
                            route.getDistance(),
                            route.getDuration());
                }
            }
            fulldistance += route.getDistance();
            fullduration += route.getDuration();
            originMarkers.add(new MarkerOptions().title(route.getStartPlaceName())
                    .position(route.getStartPoint())
            );
            destinationMarkers.add(new MarkerOptions()
                    .title(route.getEndPlaceName())
                    .position(route.getEndPoint()));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).width(10);

            allPolylineOptions.add(polylineOptions);

            for (int i = 0; i < route.getSizeOfRoutePoints(); i++)
                polylineOptions.add(route.getLatLngFromRoutesPoints(i));
        }

        int hour = (fullduration / 60)/60;
        int min = fullduration / 60;
        String distance = String.valueOf(fulldistance);
        Log.i(TAG, "onSucceedFindDirection ended");
    }

    @Override
    public void onStoreFindDirection() {
        Log.i(TAG, "onStoreFindDirection");
        RouteService operations = new RouteService(context);
        operations.storePlannedRouteInDatabase(route, this);
        Log.i(TAG, "onStoreFindDirection end");
    }

    /**
     * The method selects the currently selected route and creates polyline using RoadFinder
     */
    public void createPolylinePlannedRoute(PlannedRoute route){
        RealmList<PointOfRoute> points = route.getPoints();
        if(points.size() >= 2){
            RealmLocation start = points.get(0).getPoint();
            for(int i = 1; i < points.size(); i++) {
                RealmLocation end = points.get(i).getPoint();
                try {
                    new RoadFinder(this, new LatLng(start.getLatitude(), start.getLongitude()),
                            new LatLng(end.getLatitude(), end.getLongitude())).execute();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Error in creating polyline: " + e.getMessage());
                }
                start = points.get(i).getPoint();
            }
        }
    }

    public List<PolylineOptions> getAllPolylineOptions() {
        return allPolylineOptions;
    }
}
