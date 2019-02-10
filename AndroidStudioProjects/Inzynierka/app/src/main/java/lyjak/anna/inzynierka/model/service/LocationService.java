package lyjak.anna.inzynierka.model.service;

import android.Manifest;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

import io.realm.Realm;

/**
 * Service stores current user location every 10 seconds and 100 meters minimum change.
 * Created by Anna Łyjak on 22.09.2017.
 */

public class LocationService extends Service {

    private static final String TAG = LocationService.class.getSimpleName();

    private Realm mRealm;

    private LocationManager lm;
    private PendingIntent pendingIntent;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onStartCommand - LocationService");

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Intent intent1 = new Intent(this, LocationReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, "Permision fine location and/or coarse location denayed!",
                    Toast.LENGTH_SHORT).show();
            return START_STICKY;
        }

        lm.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                10, // minimum time interval between location updates, in milliseconds
                100, // minimum distance between location updates, in meters
                pendingIntent);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        save(getApplicationContext());
        lm.removeUpdates(pendingIntent);

        super.onDestroy();
        Toast.makeText(this, "Service destroyed", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onDestroy - LocationService");
    }

    public void save(Context context) {
        Log.i(TAG, "start save");
        LocationReceiver.actuallRoute.setEndDate(new Date(System.currentTimeMillis()));
        initRealm(context);
        mRealm.beginTransaction();
        mRealm.copyToRealm(LocationReceiver.actuallRoute);
        mRealm.commitTransaction();
        closeRealm();
        Log.i(TAG, "end save");
    }

    public void initRealm(Context context) {
        Realm.init(context);
        mRealm = Realm.getDefaultInstance();
    }

    public void closeRealm() {
        mRealm.close();
    }

}
