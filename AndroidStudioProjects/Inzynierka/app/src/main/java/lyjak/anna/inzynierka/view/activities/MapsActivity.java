package lyjak.anna.inzynierka.view.activities;

import android.app.Dialog;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.RealmList;
import lyjak.anna.inzynierka.R;
import lyjak.anna.inzynierka.service.model.realm.PlannedRoute;
import lyjak.anna.inzynierka.service.model.realm.PointOfRoute;
import lyjak.anna.inzynierka.service.model.realm.RealmLocation;
import lyjak.anna.inzynierka.service.model.realm.Route;
import lyjak.anna.inzynierka.service.respository.RouteService;
import lyjak.anna.inzynierka.viewmodel.report.GenerateReport;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

    private static final String TAG = MapsActivity.class.getSimpleName();
    public static GenerateReport report;
    private Boolean generate;

    private GoogleMap mMap;
    private CameraPosition mCameraPosition;

    // The entry point to Google Play services, used by the Places API and Fused Location Provider.
    private GoogleApiClient mGoogleApiClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);

    // List of temp markers of user's founded localization
    private final List<Marker> mListOfMarkers = new ArrayList<>();

    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final String BUNDLE_LIST_LAT = "LatList";
    private static final String BUNDLE_LIST_LONG = "LongList";

    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;
    private Place mSearchPlaceSelected;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private RouteService routeService;
    private PlannedRoute savePlannedRoute;
    private Route saveRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        routeService = new RouteService(this);

        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
            if (savePlannedRoute == null) {
                String title = savedInstanceState.getString("title");
                int distance = savedInstanceState.getInt("distance");
                int duration = savedInstanceState.getInt("duration");
                if (title != null) {
                    savePlannedRoute = routeService.findPlannedRoute(title, distance, duration);
                }
            }
        } else {
            Bundle extras = getIntent().getExtras();
            // if bundle had been put -> get infromations about route to display
            if(extras != null && saveRoute == null) {
                String title = extras.getString("title");
                if(title != null) {
                    if(title.equals("@ACTUALL_ROUTE@")) {
                        Long dateLong = extras.getLong("date");
                        Long startDateLong = extras.getLong("startDate");
                        Long endDateLong = extras.getLong("endDate");
                        Date date = null;
                        Date startDate = null;
                        Date endDate = null;
                        if (dateLong != null) {
                            date = new Date();
                            date.setTime(dateLong);
                        }
                        if (startDateLong != null) {
                            startDate = new Date();
                            startDate.setTime(startDateLong);
                        }
                        if (endDateLong != null) {
                            endDate = new Date();
                            endDate.setTime(endDateLong);
                        }
                        saveRoute = routeService.findRoute(date, startDate, endDate);
                    } else {
                        int distance = extras.getInt("distance");
                        int duration = extras.getInt("duration");
                        savePlannedRoute = routeService.findPlannedRoute(title, distance, duration);
                    }
                }
                generate = extras.getBoolean("REPORT"); //if report should be generated
            }
        }
        setContentView(R.layout.activity_maps);

        // Build the Play services client for use by the Fused Location Provider and the Places API.
        // Use the addApi() method to request the Google Places API and the Fused Location Provider.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

        initSearchPlacesFragment();
    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            if(savePlannedRoute != null) {
                outState.putString("title", savePlannedRoute.getTitle());
                outState.putInt("distance", savePlannedRoute.getDistance());
                outState.putInt("duration", savePlannedRoute.getDuration());
            }
            if(saveRoute != null) {
                outState.putLong("date", saveRoute.getDate().getTime());
                outState.putLong("startDate", saveRoute.getStartDate().getTime());
                outState.putLong("endDate", saveRoute.getEndDate().getTime());
            }
            super.onSaveInstanceState(outState);
        }
    }

    /**
     * Builds the map when the Google Play services client is successfully connected.
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Handles suspension of the connection to the Google Play services client.
     */
    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
    }

    /**
     * Handles failure to connect to the Google Play services client.
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "Connection filed.");
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setMapStyle();
        setLongClickContextMenu();

        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                        (FrameLayout) findViewById(R.id.map), false);

                TextView title = ((TextView) infoWindow.findViewById(R.id.title));
                title.setText(marker.getTitle());

                TextView snippet = ((TextView) infoWindow.findViewById(R.id.snippet));
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }
        });

        // Set listener to each marker on map
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();
        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        Log.i(TAG, "onMapReady, savePlannedRoute: " + savePlannedRoute == null? "null" : "nie null");
        if (savePlannedRoute != null) {
            drawPlannedRouteOnMap();
        }
        if (saveRoute != null) {
            drawRouteOnMap();
        }
    }

    private void drawRouteOnMap() {
        Log.i(TAG, "drawRouteOnMap");
        List<LatLng> line = createLatListFromLocations(saveRoute.getLocations());
        Polyline routePolyline = mMap.addPolyline(new PolylineOptions()
                .addAll(line)
        );
        calculateZoom(line);
    }

    private void drawPlannedRouteOnMap() {
        Log.i(TAG, "drawPlannedRouteOnMap");
        RealmList<PointOfRoute> points = savePlannedRoute.getPoints();
        for (PointOfRoute point : points) {
            LatLng latLng = new LatLng(point.getPoint().getLatitude(),
                    point.getPoint().getLongitude());
            Marker tempMarker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(point.getName()));
            addToMarkersList(tempMarker);
        }
        List<LatLng> line = createLatListFromLocations(savePlannedRoute.getLine());
        if(line.size() != 0) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(line.get(0), DEFAULT_ZOOM));
        } else {
            routeService.calculateLine(savePlannedRoute);
            savePlannedRoute = routeService.findPlannedRoute(
                    savePlannedRoute.getTitle(),
                    savePlannedRoute.getDistance(),
                    savePlannedRoute.getDuration());
        }

        Polyline actuallRoutePolyline = mMap.addPolyline( new PolylineOptions()
                .addAll(line)
        );

        calculateZoom(null);
    }

    private List<LatLng> createLatListFromLocations(List<RealmLocation> line) {
        List<LatLng> result = new ArrayList<>();
        for (RealmLocation location : line) {
            result.add(new LatLng(location.getLatitude(), location.getLongitude()));
        }
        return result;
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        if (mLocationPermissionGranted) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mLastKnownLocation = null;
        }
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        if (mLocationPermissionGranted) {
            mLastKnownLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
        }

        // Set the map's camera position to the current location of the device.
        if (mCameraPosition != null) {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        } else if (mLastKnownLocation != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mLastKnownLocation.getLatitude(),
                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
        } else {
            Log.d(TAG, "Current location is null. Using defaults.");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }

    // setting style of the map, which I had created using the website:
    // https://developers.google.com/maps/documentation/android-api/styling
    // This style you can find in raw/map_style_json.json
    private void setMapStyle() {
        try {
            mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style_json));

        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        } catch (NullPointerException e) {
            Log.e(TAG, "GoogleMap is null. Error: ", e);
        }
    }

    // Sets suitable zoom to see all markers - points
    private void calculateZoom(List<LatLng> line) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        if (line == null) {
            includeMarkersToBuilder(builder);
        } else {
            includeLatLngToBuilder(builder, line);
        }
        LatLngBounds bounds = builder.build();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.15);
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        mMap.animateCamera(cu);
        if (generate != null) {
            if (generate == true) {
                screenShot();
            }
        }
    }

    private void includeMarkersToBuilder(LatLngBounds.Builder builder) {
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

    private void includeLatLngToBuilder(LatLngBounds.Builder builder, List<LatLng> line) {
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

    private void setLongClickContextMenu() {
        mMap.setOnMapLongClickListener(latLng -> {
            final Dialog contextMenuDialog = new Dialog(MapsActivity.this, R.style.SettingsDialogStyle);
            contextMenuDialog.setContentView(R.layout.map_context_dialog);
            contextMenuDialog.setTitle("@string/map_setting_traffic");

            RadioButton onTrafficButton = (RadioButton) contextMenuDialog.findViewById(R.id.action_traffic_on);
            onTrafficButton.setOnClickListener(v -> {
                contextMenuDialog.dismiss();
                OnTrafficRadioButtonClick(v);
            });
            RadioButton offTrafficButton = (RadioButton) contextMenuDialog.findViewById(R.id.action_traffic_off);
            offTrafficButton.setOnClickListener(v -> {
                contextMenuDialog.dismiss();
                OnTrafficRadioButtonClick(v);
            });

            if(mMap.isTrafficEnabled()) {
                onTrafficButton.setChecked(true);
                offTrafficButton.setChecked(false);
            } else {
                onTrafficButton.setChecked(false);
                offTrafficButton.setChecked(true);
            }
            contextMenuDialog.show();
        });
    }

    public void OnTrafficRadioButtonClick(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.action_traffic_on:
               if(!mMap.isTrafficEnabled()) {
                   setShowTraffic();
                   ((RadioButton) view).setChecked(true);
               }
                    break;
            case R.id.action_traffic_off:
                if (mMap.isTrafficEnabled()){
                    setTurnOffTraffic();
                    ((RadioButton) view).setChecked(true);
                }
                    break;
        }
    }

    /**
     * Set map in a mode showing trafic
     */
    private void setShowTraffic() {
        mMap.setTrafficEnabled(true);
    }

    /**
     * Turn off trafic mode
     */
    private void setTurnOffTraffic() {
        mMap.setTrafficEnabled(false);
    }

    /**
     * Method inits PlaceAutocompleteFragment wich register a listener to receive callbacks when
     * a place has been selected or an error has occurred.
     */
    private void initSearchPlacesFragment() {
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener(){

            @Override
            public void onPlaceSelected(Place place) {
                Log.i(TAG, "Place Selected: " + place.getName());
                mSearchPlaceSelected = place;
                animateToSelectedPlace();
            }

            @Override
            public void onError(Status status) {
                Log.e(TAG, "onError: Status = " + status.toString());
                Toast.makeText(getApplicationContext(),
                        "Place selection failed: " + status.getStatusMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void animateToSelectedPlace() {
        LatLng latLng = mSearchPlaceSelected.getLatLng();

        Marker actuallMarker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(mSearchPlaceSelected.getName().toString()));

        addToMarkersList(actuallMarker);
        actuallMarker.showInfoWindow();
    }

    private void addToMarkersList(Marker marker) {
        mListOfMarkers.add(marker);
    }

    /**
     * This callback is called only when there is a saved instance previously saved using
     * onSaveInstanceState(). We restore some state in onCreate() while we can optionally restore
     * other state here, possibly usable after onStart() has completed.
     * The savedInstanceState Bundle is same as the one used in onCreate().
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        setMarkers(
                savedInstanceState.getStringArrayList(BUNDLE_LIST_LAT),
                savedInstanceState.getStringArrayList(BUNDLE_LIST_LONG));
        calculateZoom(null);
    }

    private void setMarkers(ArrayList<String> latitudes, ArrayList<String> longitudes) {
        if(latitudes != null && longitudes != null) {
            List<Marker> oldMarkers = new ArrayList<>();

            for(int i = 0; i < latitudes.size() && i < longitudes.size(); i++) {
                oldMarkers.add(mMap.addMarker(new MarkerOptions().position(
                        new LatLng(
                                Integer.parseInt(latitudes.get(i)),
                                Integer.parseInt(longitudes.get(i))))));
            }
        }
    }

    // invoked when the activity may be temporarily destroyed, save the instance state here
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putStringArrayList(BUNDLE_LIST_LAT, getLatitudes());
        outState.putStringArrayList(BUNDLE_LIST_LONG, getLongitudes());
        super.onSaveInstanceState(outState, outPersistentState);
    }

    private ArrayList<String> getLatitudes() {
        ArrayList<String> result = new ArrayList<>();
        for (Marker mark: mListOfMarkers) {
            result.add(String.valueOf(mark.getPosition().latitude));
        }
        return result;
    }

    private ArrayList<String> getLongitudes() {
        ArrayList<String> result = new ArrayList<>();
        for (Marker mark: mListOfMarkers) {
            result.add(String.valueOf(mark.getPosition().longitude));
        }
        return  result;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        onMarkerClickAndMarkerInfoListener(marker);
        return true;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        onMarkerClickAndMarkerInfoListener(marker);
    }

    private void onMarkerClickAndMarkerInfoListener(final Marker marker) {
        //show title of the mark
        if(marker.isInfoWindowShown()) {
            marker.hideInfoWindow();
        } else {
            marker.showInfoWindow();
        }

        // displays dialog menu
        final Marker tempMarker = marker;
        final Dialog markerDialog = new Dialog(MapsActivity.this, R.style.SettingsDialogStyle);
        markerDialog.setContentView(R.layout.dialog_map_marker_click);
        markerDialog.setTitle(R.string.dialog_map_marker_click_choose_option);

        Button addToActuallRouteButton = (Button) markerDialog.findViewById(R.id.buttonAddToRoute);
        addToActuallRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markerDialog.dismiss();
                addPointToActualRoute(marker);
            }
        });

        Button newRouteButton = (Button) markerDialog.findViewById(R.id.buttonCreateNewRoute);
        newRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markerDialog.dismiss();
                routeService.createNewPlannedRoute(marker);
            }
        });

        Button deleteMarkerButton = (Button) markerDialog.findViewById(R.id.buttonDeleteMarker);
        deleteMarkerButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                markerDialog.dismiss();
                mListOfMarkers.remove(tempMarker);
                tempMarker.remove();
            }

        });

        Button anulujButton = (Button) markerDialog.findViewById(R.id.buttonAnuluj);
        anulujButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v)
            {
                markerDialog.dismiss();
            }

        });

        markerDialog.show();
    }

    private void addPointToActualRoute(Marker marker) {
        if (savePlannedRoute != null) {
            routeService.addPointToRoute(marker, savePlannedRoute);
        } else {
            routeService.addPointToActualRoute(marker);
        }
        Toast.makeText(getApplicationContext(),
                getApplicationContext().getString(R.string.map_new_point_created),
                Toast.LENGTH_SHORT).show();
    }

    private MapsActivity getActivity() {
        return this;
    }

    public Bitmap bitmap;

    public void screenShot() {
        mMap.setOnMapLoadedCallback(() -> {
            GoogleMap.SnapshotReadyCallback callback = snapshot -> {
                if (report != null) {
                    bitmap = snapshot;
                    report.createPdf(getActivity(), getApplicationContext(), bitmap);
                    report = null;
                }
            };
            mMap.snapshot(callback);
        });
    }

}
