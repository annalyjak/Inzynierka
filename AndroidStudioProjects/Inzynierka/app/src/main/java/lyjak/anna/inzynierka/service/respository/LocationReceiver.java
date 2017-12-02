package lyjak.anna.inzynierka.service.respository;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

import io.realm.RealmList;
import lyjak.anna.inzynierka.service.model.realm.RealmLocation;
import lyjak.anna.inzynierka.service.model.realm.Route;

/**
 * Created by Anna Łyjak on 22.09.2017.
 */

public class LocationReceiver extends BroadcastReceiver {

    private static final String TAG = LocationReceiver.class.getSimpleName();

    public static final Route actuallRoute = new Route();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Start onRecive");

        if(actuallRoute.getStartDate() == null) {
            actuallRoute.setStartDate(new Date(System.currentTimeMillis()));
            actuallRoute.setLocations(new RealmList<>());
        }

        String locationKey = LocationManager.KEY_LOCATION_CHANGED;
        String providerKey = LocationManager.KEY_PROVIDER_ENABLED;
        if (intent.hasExtra(providerKey)) {
            if (!intent.getBooleanExtra(providerKey, true)) {
                Toast.makeText(context, "disabled", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "disabled");
            } else {
                Toast.makeText(context, "enabled", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "enabled");
            }
        }

        if (intent.hasExtra(locationKey)) {
            Location loc = (Location) intent.getExtras().get(locationKey);
            Toast.makeText(context, "Location changed: Lat: " + loc.getLatitude() +
                    " Lng: " + loc.getLongitude(), Toast.LENGTH_SHORT).show();
            store(loc);
        }
    }

    private void store(Location loc) {
        Log.i(TAG, "start store");
        RealmLocation tempRelLoc = new RealmLocation();
        tempRelLoc.setLatitude(loc.getLatitude());
        tempRelLoc.setLongitude(loc.getLongitude());
        actuallRoute.addLocationToList(tempRelLoc);
        Log.i(TAG, "end store");
    }
}
