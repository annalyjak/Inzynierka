package lyjak.anna.inzynierka.view.adapters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import java.util.List;

import io.realm.RealmList;
import lyjak.anna.inzynierka.R;
import lyjak.anna.inzynierka.view.activities.MainActivity;
import lyjak.anna.inzynierka.view.activities.MapsActivity;
import lyjak.anna.inzynierka.databinding.DialogAddingRouteToReportConfirmBinding;
import lyjak.anna.inzynierka.service.model.realm.PlannedRoute;
import lyjak.anna.inzynierka.service.model.realm.RealmLocation;
import lyjak.anna.inzynierka.view.fragments.PointsFragment;
import lyjak.anna.inzynierka.view.fragments.TransportSelectionFragment;
import lyjak.anna.inzynierka.service.respository.OnMarkersOperations;
import lyjak.anna.inzynierka.viewmodel.report.GenerateReport;
import lyjak.anna.inzynierka.viewmodel.tasks.PointImageFromUrlAsyncTask;
import lyjak.anna.inzynierka.viewmodel.tasks.PolylineImageFromUrlAsyncTask;
import lyjak.anna.inzynierka.viewmodel.utils.CreateModelDataUtil;
import lyjak.anna.inzynierka.viewmodel.utils.GoogleMapsStaticUtil;

/**
 * Created by Anna Łyjak on 08.10.2017.
 */

public class PlannedRouteAdapter extends RecyclerView.Adapter<PlannedRouteAdapter.ViewHolder> {

    private static final String TAG = PlannedRouteAdapter.class.getSimpleName();
    private static final String DATE_FORMAT = "%1$ta, %1$te %1$tB %1$tY"; // example: Pon, 12 października 2017

    private static List<PlannedRoute> mDataset;
    private static Activity activity;

    private static GenerateReport generateReport;

    public PlannedRouteAdapter(Activity activity, List<PlannedRoute> myDataset) {
        this.activity = activity;
        this.mDataset = myDataset;
    }

    public PlannedRouteAdapter(Activity activity, List<PlannedRoute> myDataset,
                               GenerateReport generateReport) {
        this.activity = activity;
        this.mDataset = myDataset;
        this.generateReport = generateReport;
    }

    @Override
    public PlannedRouteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.card_planned_route, parent, false);
        return new ViewHolder(v, parent.getContext());
    }

    @Override
    public void onBindViewHolder(final PlannedRouteAdapter.ViewHolder holder, int position) {
        PlannedRoute route = mDataset.get(position);
        Resources resources = activity.getApplicationContext().getResources();

        holder.position = position;
        holder.title.setText((resources.getString(R.string.cardview_title) + " " + route.getTitle()));
        holder.date.setText(((resources.getString(R.string.cardview_date) + " "
                + String.format(DATE_FORMAT, route.getDate()))));
        holder.points.setText((resources.getString(R.string.cardview_points) + " "
                + String.valueOf(route.getSize())));
        holder.duration.setText((resources.getString(R.string.cardview_duartion) + " " + String.valueOf(route.getDuration())));
        holder.distance.setText((resources.getString(R.string.cardview_distance) + " " + String.valueOf(route.getDistance())));

        if (route.getSize() > 0 && ContextCompat.checkSelfPermission(
                this.activity.getApplicationContext(), Manifest.permission.INTERNET)
                == PackageManager.PERMISSION_GRANTED) {
            setImageFromUrl(holder, route);
        }

    }

    private String getPolyline(RealmList<RealmLocation> line) {
        List<LatLng> latLngs = CreateModelDataUtil.transferListOfRealmLocationsToLatLng(line);
        String encodePolyline = PolyUtil.encode(latLngs);
        return encodePolyline;
    }

    private void setImageFromUrl(final PlannedRouteAdapter.ViewHolder holder, PlannedRoute route) {
        if (route.getPoints().size() > 1) { // if route is to short - displays only first element
            PolylineImageFromUrlAsyncTask setImageFromUrl = new PolylineImageFromUrlAsyncTask(holder.picture);
            setImageFromUrl.execute(getPolyline(route.getLine()));
        } else {
            PointImageFromUrlAsyncTask setImageFromUrl = new PointImageFromUrlAsyncTask(holder.picture);
            LatLng latLng = new LatLng(
                    route.getPoints().get(0).getPoint().getLatitude(),
                    route.getPoints().get(0).getPoint().getLongitude());
            setImageFromUrl.execute(latLng);
        }
    }

    public void addNewPlannedRoute(PlannedRoute route) {
        mDataset.add(route);
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private static void removeThisItemFromDatabase(int position) {
        if (position >= 0) {
            PlannedRoute routeToRemove = mDataset.get(position);
            Log.i(TAG, "Usuwam trasę o id: " + position);
            OnMarkersOperations operations = new OnMarkersOperations(activity);
            operations.removePlannedRouteFromDatabase(routeToRemove);
//            mDataset.remove(routeToRemove);
        }
    }

    private static PlannedRoute getSelectedRoute(int position) {
        if (position >= 0) {
            PlannedRoute route = mDataset.get(position);
            return route;
        }
        return null;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public int position;
        public TextView title, date, distance, duration, points;
        public ImageView picture;
        private Context context;

        public ViewHolder(View v, final Context context) {
            super(v);
            this.context = context;
            title = (TextView) v.findViewById(R.id.textViewTitlePlannedRoute);
            date = (TextView) v.findViewById(R.id.textViewDate);
            points = (TextView) v.findViewById(R.id.textViewPoints);
            distance = (TextView) v.findViewById(R.id.textViewDistance);
            duration = (TextView) v.findViewById(R.id.textViewDuration);
            picture = (ImageView) v.findViewById(R.id.imageViewCardMap);

            if (generateReport == null) {
                setBasicOnClickListener(v);
            } else {
                setSelectionOnClickListener(v);
            }
        }

        private void setSelectionOnClickListener(View v) {
            v.setOnClickListener((View v1) -> {
                final Dialog dialog = new Dialog(context, R.style.SettingsDialogStyle);
                LayoutInflater layoutInflater = LayoutInflater.from(activity.getApplicationContext());
                DialogAddingRouteToReportConfirmBinding viewDataBinding = DataBindingUtil
                        .inflate(layoutInflater,
                                R.layout.dialog_adding_route_to_report_confirm,
                                null, false);
                viewDataBinding.buttonNo.setOnClickListener(v2 -> {
                    dialog.dismiss();
                });
                viewDataBinding.buttonYes.setOnClickListener(v2 -> {
                    dialog.dismiss();
                    PlannedRoute route = getSelectedRoute(position);
                    generateReport.setPlannedRoute(route);
                    //TODO otworz kolejne pytanie "Czy chcesz przejść do generowania rapotu?"
                    Intent openMapIntent = new Intent(activity,
                            MapsActivity.class);
                    MapsActivity.report = generateReport;
                    Bundle bundle = new Bundle();
                    bundle.putString("title", route.getTitle());
                    bundle.putInt("duration", route.getDuration());
                    bundle.putInt("distance", route.getDistance());
                    bundle.putBoolean("REPORT", true);
                    openMapIntent.putExtras(bundle);
                    activity.startActivity(openMapIntent);
                });
                dialog.setContentView(viewDataBinding.getRoot());
                dialog.show();
            });
        }

        private void setBasicOnClickListener(View v) {
            v.setOnClickListener(v13 -> {
                final Dialog dialog = new Dialog(context, R.style.SettingsDialogStyle);
                dialog.setContentView(R.layout.dialog_planned_route_card_click);

                Button generateReportButton = (Button) dialog.findViewById(R.id.buttonGenerateReport);
                generateReportButton.setOnClickListener(v1 -> {
                    dialog.dismiss();
                    final TransportSelectionFragment fragment = TransportSelectionFragment
                            .newInstance(getSelectedRoute(position));
                    MainActivity.attachNewFragment(fragment);
                });

                Button showRouteOnMapButton = (Button) dialog.findViewById(R.id.buttonShowRouteOnMap);
                showRouteOnMapButton.setOnClickListener(v12 -> {
                    dialog.dismiss();
                    Intent openMapIntent = new Intent(((Dialog) dialog).getContext(),
                            MapsActivity.class);
                    PlannedRoute route = getSelectedRoute(position);
                    Bundle bundle = new Bundle();
                    bundle.putString("title", route.getTitle());
                    bundle.putInt("duration", route.getDuration());
                    bundle.putInt("distance", route.getDistance());
                    openMapIntent.putExtras(bundle);
                    activity.startActivity(openMapIntent);
                });
                Button showPointsButton = (Button) dialog.findViewById(R.id.buttonShowAllPoints);
                showPointsButton.setOnClickListener(v131 -> {
                    dialog.dismiss();
                    Fragment newFragment = PointsFragment.newInstance(getSelectedRoute(position));
                    ((MainActivity) activity).attachNewFragment(newFragment);
                });

                Button deleteMarkerButton = (Button) dialog.findViewById(R.id.buttonDeleteRoute);
                deleteMarkerButton.setOnClickListener(view -> {
                    dialog.dismiss();
                    removeThisItemFromDatabase(position);
                    if (activity instanceof MainActivity) {
                        ((MainActivity) activity).notyfyDataSetChange();
                    }
                });

                Button anulujButton = (Button) dialog.findViewById(R.id.buttonAnuluj);
                anulujButton.setOnClickListener(v1312 -> dialog.dismiss());

                dialog.show();
            });
        }
    }

}
