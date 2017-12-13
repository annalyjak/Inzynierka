package lyjak.anna.inzynierka.view.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lyjak.anna.inzynierka.R;
import lyjak.anna.inzynierka.databinding.DialogMapMarkerClickBinding;
import lyjak.anna.inzynierka.databinding.MapContextDialogBinding;
import lyjak.anna.inzynierka.model.realmObjects.PlannedRoute;
import lyjak.anna.inzynierka.model.realmObjects.PointOfRoute;
import lyjak.anna.inzynierka.model.realmObjects.Route;
import lyjak.anna.inzynierka.view.fragments.TransportSelectionFragment;
import lyjak.anna.inzynierka.viewmodel.MapsViewModel;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

    private static final String TAG = MapsActivity.class.getSimpleName();

    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final String BUNDLE_LIST_LAT = "LatList";
    private static final String BUNDLE_LIST_LONG = "LongList";
    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private boolean locationPermissionGranted;

    private GoogleMap map;
    private CameraPosition cameraPosition;

    // The entry point to Google Play services, used by the Places API and Fused Location Provider.
    private GoogleApiClient googleApiClient;

    private MapsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new MapsViewModel(this);

        if (savedInstanceState != null) {
            viewModel.setLastKnownLocation(savedInstanceState.getParcelable(KEY_LOCATION));
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
            Log.i(TAG, "savedInstanceState isnt null");
            if (viewModel.isPlannedRouteNull()) {
                String title = savedInstanceState.getString("title");
                int distance = savedInstanceState.getInt("distance");
                int duration = savedInstanceState.getInt("duration");
                if (title != null) {
                    viewModel.findPlannedRoute(title, distance, duration);
                }
            }
        } else {
            Bundle extras = getIntent().getExtras();
            if(extras!= null && extras.getString("STANDARDREPORT")!=null) {
                viewModel.setGenerate(extras.getBoolean("REPORT")); //if report should be generated
            }
            if (extras != null && extras.getBoolean("REPORT")) {
                viewModel.setGenerate(extras.getBoolean("REPORT")); //if report should be generated
            }
            // if bundle had been put -> get infromations about route to display
            if(extras != null && extras.getBoolean("editPlannedRoute")) {
                String title = extras.getString("title");
                int distance = extras.getInt("distance");
                int duration = extras.getInt("duration");
                viewModel.findPlannedRoute(title, distance, duration);
            }
            if(extras != null && extras.getBoolean("editActuallRoute")) {
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
                viewModel.findRoute(date, startDate, endDate);
            }
        }
        setContentView(R.layout.activity_maps);

        // Build the Play services client for use by the Fused Location Provider and the Places API.
        // Use the addApi() method to request the Google Places API and the Fused Location Provider.
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();

        initSearchPlacesFragment();
    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (map != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, map.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, viewModel.getLastKnownLocation());
            if(!viewModel.isPlannedRouteNull()) {
                PlannedRoute savePlannedRoute = viewModel.getSavePlannedRoute();
                outState.putString("title", savePlannedRoute.getTitle());
                outState.putInt("distance", savePlannedRoute.getDistance());
                outState.putInt("duration", savePlannedRoute.getDuration());
            }
            if(!viewModel.isActuallRouteNull()) {
                Route savedRoute = viewModel.getSavedRoute();
                outState.putLong("date", savedRoute.getDate().getTime());
                outState.putLong("startDate", savedRoute.getStartDate().getTime());
                outState.putLong("endDate", savedRoute.getEndDate().getTime());
            }
            super.onSaveInstanceState(outState);
        }
    }

    /**
     * Builds the map when the Google Play services client is successfully connected.
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
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
        map = googleMap;
        setMapStyle();
        setLongClickContextMenu();

        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

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
        map.setOnMarkerClickListener(this);
        map.setOnInfoWindowClickListener(this);

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();
        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        if(!viewModel.isPlannedRouteNull() && !viewModel.isActuallRouteNull()) {
            drawRoutesOnMap();
        } else {
            if (!viewModel.isPlannedRouteNull()) {
                drawPlannedRouteOnMap();
            }
            if (!viewModel.isActuallRouteNull()) {
                drawRouteOnMap();
            }
        }
    }

    private void drawRoutesOnMap() {
        Log.i(TAG, "drawRouteOnMap");
        List<LatLng> firstLine = viewModel.createLatListFromLocations(viewModel.getSavedRouteLocations());
        map.addPolyline(new PolylineOptions().color(Color.BLACK).addAll(firstLine));

        List<PointOfRoute> points = viewModel.getSavedPlannedRoutePoints();
        if (points!= null) {
            for (PointOfRoute point : points) {
                LatLng latLng = new LatLng(point.getPoint().getLatitude(),
                        point.getPoint().getLongitude());
                Marker tempMarker = map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(point.getName()));
                viewModel.addToMarkersList(tempMarker);
            }
        }
        List<LatLng> secondLine = viewModel.createLatListFromSavedLocations();
        if(secondLine.size() != 0) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(secondLine.get(0), DEFAULT_ZOOM));
        } else {
            viewModel.createLine();
        }
        map.addPolyline(new PolylineOptions().color(Color.RED).addAll(secondLine));

        calculateZoom(firstLine, secondLine);
    }

    private void drawRouteOnMap() {
        Log.i(TAG, "drawRouteOnMap");
        List<LatLng> line = viewModel.createLatListFromLocations(viewModel.getSavedRouteLocations());
        map.addPolyline(new PolylineOptions()
                .color(Color.BLACK)
                .addAll(line)
        );
        calculateZoom(line);
    }

    private void drawPlannedRouteOnMap() {
        Log.i(TAG, "drawPlannedRouteOnMap");
        List<PointOfRoute> points = viewModel.getSavedPlannedRoutePoints();
        for (PointOfRoute point : points) {
            LatLng latLng = new LatLng(point.getPoint().getLatitude(),
                    point.getPoint().getLongitude());
            Marker tempMarker = map.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(point.getName()));
            viewModel.addToMarkersList(tempMarker);
        }
        List<LatLng> line = viewModel.createLatListFromSavedLocations();
        if(line.size() != 0) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(line.get(0), DEFAULT_ZOOM));
        } else {
            viewModel.createLine();
        }

        map.addPolyline( new PolylineOptions()
                .color(Color.RED)
                .addAll(line)
        );

        calculateZoom(null);
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        if (locationPermissionGranted) {
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            map.setMyLocationEnabled(false);
            map.getUiSettings().setMyLocationButtonEnabled(false);
            viewModel.setLastKnownLocation(null);
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
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        if (locationPermissionGranted) {
            viewModel.setLastKnownLocation(LocationServices.FusedLocationApi
                    .getLastLocation(googleApiClient));
        }

        // Set the map's camera position to the current location of the device.
        if (cameraPosition != null) {
            map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        } else if (viewModel.getLastKnownLocation() != null) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(viewModel.getLastKnownLocation().getLatitude(),
                            viewModel.getLastKnownLocation().getLongitude()), DEFAULT_ZOOM));
        } else {
            Log.d(TAG, "Current location is null. Using defaults.");
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(MapsViewModel.DEFAULT_LOCATION,
                    DEFAULT_ZOOM));
            map.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }

    // setting style of the map, which I had created using the website:
    // https://developers.google.com/maps/documentation/android-api/styling
    // This style you can find in raw/map_style_json.json
    private void setMapStyle() {
        try {
            map.setMapStyle(
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
            viewModel.includeMarkersToBuilder(builder);
        } else {
            viewModel.includeLatLngToBuilder(builder, line);
        }
        LatLngBounds bounds = builder.build();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.15);
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        map.animateCamera(cu);
        if (viewModel.isGenerated()) {
            screenShot();
        }
    }

    private void calculateZoom(List<LatLng> firstLine, List<LatLng> secondLine) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
//        viewModel.includeMarkersToBuilder(builder);
        viewModel.includeLatLngToBuilder(builder, firstLine);
        viewModel.includeLatLngToBuilder(builder, secondLine);
        LatLngBounds bounds = builder.build();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.15);
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        map.animateCamera(cu);
        if (viewModel.isGenerated()) {
            screenShot();
        }
    }


    private void setLongClickContextMenu() {
        map.setOnMapLongClickListener(latLng -> {
            final Dialog contextMenuDialog = new Dialog(MapsActivity.this, R.style.SettingsDialogStyle);
            LayoutInflater layoutInflater = LayoutInflater.from(MapsActivity.this);
            MapContextDialogBinding viewDataBinding = DataBindingUtil
                    .inflate(layoutInflater,
                            R.layout.map_context_dialog,
                            null, false);

            contextMenuDialog.setTitle("@string/map_setting_traffic");
            viewDataBinding.actionTrafficOn.setOnClickListener(v -> {
                contextMenuDialog.dismiss();
                OnTrafficRadioButtonClick(v);
            });
            viewDataBinding.actionTrafficOff.setOnClickListener(v -> {
                contextMenuDialog.dismiss();
                OnTrafficRadioButtonClick(v);
            });

            if(map.isTrafficEnabled()) {
                viewDataBinding.actionTrafficOn.setChecked(true);
                viewDataBinding.actionTrafficOff.setChecked(false);
            } else {
                viewDataBinding.actionTrafficOn.setChecked(false);
                viewDataBinding.actionTrafficOff.setChecked(true);
            }

            contextMenuDialog.setContentView(viewDataBinding.getRoot());
            contextMenuDialog.show();
        });
    }

    public void OnTrafficRadioButtonClick(View view) {
        switch(view.getId()) {
            case R.id.action_traffic_on:
               if(!map.isTrafficEnabled()) {
                   map.setTrafficEnabled(true);
                   ((RadioButton) view).setChecked(true);
               }
                    break;
            case R.id.action_traffic_off:
                if (map.isTrafficEnabled()){
                    map.setTrafficEnabled(false);
                    ((RadioButton) view).setChecked(true);
                }
                    break;
        }
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
                viewModel.setSearchPlaceSelected(place);
                animateToSelectedPlace(place);
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

    private void animateToSelectedPlace(Place place) {
        LatLng latLng = place.getLatLng();
        Marker actuallMarker = map.addMarker(new MarkerOptions()
                .position(latLng)
                .title(place.getName().toString()));
        viewModel.addToMarkersList(actuallMarker);
        actuallMarker.showInfoWindow();
        CameraUpdate cu = CameraUpdateFactory.newLatLng(latLng);
        map.animateCamera(cu);
    }

    /**
     * This callback is called only when there is a saved instance previously saved using
     * onSaveInstanceState(). We restore some state in onCreate() while we can optionally restore
     * other state here, possibly usable after onStart() has completed.
     * The savedInstanceState Bundle is same as the one used in onCreate().
     * @param savedInstanceState - saved instance
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
            for(int i = 0; i < latitudes.size() && i < longitudes.size(); i++) {
                map.addMarker(new MarkerOptions().position(
                        new LatLng(
                                Integer.parseInt(latitudes.get(i)),
                                Integer.parseInt(longitudes.get(i)))));
            }
        }
    }

    // invoked when the activity may be temporarily destroyed, save the instance state here
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putStringArrayList(BUNDLE_LIST_LAT, viewModel.getLatitudes());
        outState.putStringArrayList(BUNDLE_LIST_LONG, viewModel.getLongitudes());
        super.onSaveInstanceState(outState, outPersistentState);
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
        if(marker.isInfoWindowShown()) {
            marker.hideInfoWindow();
        } else {
            marker.showInfoWindow();
        }

        final Dialog markerDialog = new Dialog(MapsActivity.this, R.style.SettingsDialogStyle);
        LayoutInflater layoutInflater = LayoutInflater.from(MapsActivity.this);
        DialogMapMarkerClickBinding viewDataBinding = DataBindingUtil
                .inflate(layoutInflater,
                        R.layout.dialog_map_marker_click,
                        null, false);

        markerDialog.setTitle(R.string.dialog_map_marker_click_choose_option);

        viewDataBinding.buttonAddToRoute.setOnClickListener(v -> {
            markerDialog.dismiss();
            addPointToActualRoute(marker);
        });

        viewDataBinding.buttonCreateNewRoute.setOnClickListener(v -> {
            markerDialog.dismiss();
            viewModel.createNewPlannedRoute(getActivity(), marker);
        });

        viewDataBinding.buttonDeleteMarker.setOnClickListener(v -> {
            markerDialog.dismiss();
            viewModel.removeMarkerFromList(marker);
            marker.remove();
        });

        viewDataBinding.buttonAnuluj.setOnClickListener(v -> markerDialog.dismiss());

        markerDialog.setContentView(viewDataBinding.getRoot());
        markerDialog.show();
    }

    private void addPointToActualRoute(Marker marker) {
        viewModel.addPointToActualRoute(marker);
        Toast.makeText(getApplicationContext(),
                getApplicationContext().getString(R.string.map_new_point_created),
                Toast.LENGTH_SHORT).show();
    }

    private MapsActivity getActivity() {
        return this;
    }

    public void screenShot() {
        map.setOnMapLoadedCallback(() -> {
            GoogleMap.SnapshotReadyCallback callback = snapshot -> {
                if (viewModel.isReportMode()) {
                    Log.i(TAG, "Wykonuję report z podróży służbowej");
                    viewModel.doReport(getActivity(), getApplicationContext(), snapshot);
                }
                if (viewModel.isActualRouteReportMode()) {
                    Log.i(TAG, "Wykonuję report zrealizowanej trasy");
                    viewModel.doActualRouteReport(getActivity(), snapshot);
                }
                if (viewModel.isPlannedRouteReportMode()) {
                    Log.i(TAG, "Wykonuję report zrealizowanej trasy");
                    viewModel.doPlannedRouteReport(getActivity(), snapshot);
                }
            };
            map.snapshot(callback);
        });
    }

}
