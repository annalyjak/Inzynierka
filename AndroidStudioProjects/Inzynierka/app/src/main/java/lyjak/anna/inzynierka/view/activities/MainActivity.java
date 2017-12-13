package lyjak.anna.inzynierka.view.activities;

import android.Manifest;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import lyjak.anna.inzynierka.R;
import lyjak.anna.inzynierka.databinding.DialogLanguageSettingsBinding;
import lyjak.anna.inzynierka.model.service.LocationService;
import lyjak.anna.inzynierka.view.fragments.ActualRoutesFragment;
import lyjak.anna.inzynierka.view.fragments.HistoricalReportFragment;
import lyjak.anna.inzynierka.view.fragments.LocationListenerFragment;
import lyjak.anna.inzynierka.view.fragments.PlannedRoutesFragment;
import lyjak.anna.inzynierka.viewmodel.MainActivityViewModel;
import lyjak.anna.inzynierka.viewmodel.listeners.NotifyDataSetChangedListener;
import lyjak.anna.inzynierka.viewmodel.listeners.OnLocationServiceListener;
import lyjak.anna.inzynierka.viewmodel.others.ChangeLanguageContextWrapper;

//TODO dodać testy
//TODO usunąć komentarze z klas
//TODO dodać jakiś komunikat jeśli trasy zrealizowane i planowane - lista jest pusta
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnLocationServiceListener, NotifyDataSetChangedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int NOTIFICATION_ID = 1;

    private static Fragment mFragment = null; // fragment actuall atached on FrameLayout in MainActivity
    private static FragmentManager fragmentManager;

    private MainActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewModel = new MainActivityViewModel(this);

        //Check if all permisions are granded and please to grand them
        checkPermissions();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(FloatingActionButton.VISIBLE);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(intent);
//                Snackbar.make(view, "Dodaj nowy punkt do trasy", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragmentManager = getSupportFragmentManager();
        if (mFragment == null) {
            setDefaultView(); //sets main view
        }
    }

    private void setDefaultView() {
        mFragment = new LocationListenerFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.frameLayoutMain, mFragment).commit();
    }

    /**
     * ACCESS_FINE_LOCATION & ACCESS_COARSE_LOCATION - Application requests location permission.
     * The location of the device is nesecesy for this app to get and store user's routes.
     * INTERNET - for static Google Maps (pictures on list of routes)
     * WRITE_EXTERNAL_STORAGE - to write report on telephone's memory card
     */
    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
//            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    viewModel.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
//            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    viewModel.PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.INTERNET)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET},
                    viewModel.PERMISSIONS_REQUEST_INTERNET);
        }
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    viewModel.PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (fragmentManager.getBackStackEntryCount() > 0) {
                Log.i("MainActivity", "popping backstack");
                fragmentManager.popBackStack();
            } else {
                Log.i("MainActivity", "nothing on backstack, calling super");
                notyfyDataSetChange();
                super.onBackPressed();
            }
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            if (mFragment != null) {
                fragmentManager.beginTransaction().detach(mFragment).commit();
            }
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            if (mFragment != null) {
                fragmentManager.beginTransaction().detach(mFragment).commit();
            }
            mFragment = new LocationListenerFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.frameLayoutMain, mFragment).commit();
        } else if (id == R.id.nav_slideshow) {
            if (mFragment != null) {
                fragmentManager.beginTransaction().detach(mFragment).commit();
            }
            mFragment = new ActualRoutesFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.frameLayoutMain, mFragment).commit();

        } else if (id == R.id.nav_history) {
            if (mFragment != null) {
                fragmentManager.beginTransaction().detach(mFragment).commit();
            }
            mFragment = HistoricalReportFragment.newInstance();
            fragmentManager.beginTransaction()
                    .replace(R.id.frameLayoutMain, mFragment).commit();

        } else if (id == R.id.nav_manage) {
            if (mFragment != null) {
                fragmentManager.beginTransaction().detach(mFragment).commit();
            }
            mFragment = new PlannedRoutesFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.frameLayoutMain, mFragment).commit();

        } else if (id == R.id.nav_settings) {
            final Dialog languageDialog = new Dialog(MainActivity.this, R.style.SettingsDialogStyle);
            LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
            DialogLanguageSettingsBinding viewDataBinding = DataBindingUtil
                    .inflate(layoutInflater,
                            R.layout.dialog_language_settings,
                            null, false);
            languageDialog.setTitle(R.string.choose_language);
            viewDataBinding.plLanguageButton.setOnClickListener(v -> {
                languageDialog.dismiss();
                if(!viewModel.isLanguagePolish()) {
                    viewModel.setPolishLanguage();
                    refresh();
                }
            });
            viewDataBinding.engLanguageButton.setOnClickListener(v -> {
                languageDialog.dismiss();
                if(!viewModel.isLanguageEng()) {
                    viewModel.setEnglishLanguage();
                    refresh();
                }
            });
            if(viewModel.isLanguagePolish()) {
                viewDataBinding.plLanguageButton.setChecked(true);
                viewDataBinding.engLanguageButton.setChecked(false);
            } else {
                viewDataBinding.plLanguageButton.setChecked(false);
                viewDataBinding.engLanguageButton.setChecked(true);
            }
            languageDialog.setContentView(viewDataBinding.getRoot());
            languageDialog.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if(mFragment != null && mFragment.isAdded()) {
            fragmentManager.beginTransaction().detach(mFragment).attach(mFragment).commit();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ChangeLanguageContextWrapper.wrap(newBase, MainActivityViewModel.getLanguage()));
    }

    /**
     * On clik button (id = button_location_listener_start)
     * The method starts LocationService - recives user's location and stores it in realms datebase
     * @param view
     */
    @Override
    public void onStartLocationService(View view) {
        Log.i(TAG, "Service started");
        startService(new Intent(this, LocationService.class));
        findViewById(R.id.buttonLocationListenerStart).setEnabled(false);
        findViewById(R.id.buttonLocationListenerStop).setEnabled(true);
        createNotification();
    }

    /**
     * On clik button (id = button_location_listener_stop)
     * The method stops LocationService
     * @param view
     */
    @Override
    public void onStopLocationService(View view) {
        Log.i(TAG, "Service stoped");
        stopService(new Intent(this, LocationService.class));
        findViewById(R.id.buttonLocationListenerStart).setEnabled(true);
        findViewById(R.id.buttonLocationListenerStop).setEnabled(false);
        cancelNotification();
    }

    private void createNotification() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.ic_satellite_black_24dp)
                .setContentTitle(getString(R.string.notification_titile))
                .setContentText(getString(R.string.notification_text));

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private void cancelNotification() {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(NOTIFICATION_ID);
    }

    private void refresh() {
        Intent i = new Intent(MainActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mFragment != null && mFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().detach(mFragment)
                    .attach(mFragment)
                    .commit();
        } else {
            setDefaultView();
        }
    }

    @Override
    public void notyfyDataSetChange() {
        if (mFragment != null && mFragment.isAdded()) {
            fragmentManager
                    .beginTransaction()
                    .detach(mFragment)
                    .attach(mFragment)
                    .commit();
        }
    }

    public static void attachNewFragment(Fragment fragment) {
        if (mFragment != null) {
            fragmentManager
                    .beginTransaction()
                    .detach(mFragment)
                    .replace(R.id.frameLayoutMain, fragment)
                    .commit();
            mFragment = fragment;
        } else {
            mFragment = fragment;
            fragmentManager.beginTransaction().attach(mFragment).commitNow();
        }
    }

    private void clearBundle() {

    }

}
