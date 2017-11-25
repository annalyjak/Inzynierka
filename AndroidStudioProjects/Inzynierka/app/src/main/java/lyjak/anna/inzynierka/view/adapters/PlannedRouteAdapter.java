package lyjak.anna.inzynierka.view.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import java.util.List;

import io.realm.RealmList;
import lyjak.anna.inzynierka.R;
import lyjak.anna.inzynierka.databinding.CardPlannedRouteBinding;
import lyjak.anna.inzynierka.service.model.realm.PlannedRoute;
import lyjak.anna.inzynierka.service.model.realm.RealmLocation;
import lyjak.anna.inzynierka.view.callbacks.PlannedRouteCallback;
import lyjak.anna.inzynierka.viewmodel.PlannedRoutesCardListViewModel;
import lyjak.anna.inzynierka.viewmodel.tasks.PointImageFromUrlAsyncTask;
import lyjak.anna.inzynierka.viewmodel.tasks.PolylineImageFromUrlAsyncTask;
import lyjak.anna.inzynierka.service.model.utils.CreateModelDataUtil;

/**
 * Created by Anna Łyjak on 08.10.2017.
 */

public class PlannedRouteAdapter extends RecyclerView.Adapter<PlannedRouteAdapter.ViewHolder> {

    private static final String TAG = PlannedRouteAdapter.class.getSimpleName();
    private static final String DATE_FORMAT = "%1$ta, %1$te %1$tB %1$tY"; // example: Pon, 12 października 2017

    private static PlannedRoutesCardListViewModel viewModel;
    private static Activity activity;
    private PlannedRouteCallback plannedRouteClickCallback;

    public PlannedRouteAdapter(Activity activity, PlannedRoutesCardListViewModel viewModel,
                               PlannedRouteCallback callback) {
        this.activity = activity;
        this.viewModel = viewModel;
        this.plannedRouteClickCallback = callback;
    }

    @Override
    public PlannedRouteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardPlannedRouteBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.card_planned_route,
                        parent, false);
        binding.setCallback(plannedRouteClickCallback);

        return new PlannedRouteAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final PlannedRouteAdapter.ViewHolder holder, int position) {
        PlannedRoute route = viewModel.getPlannedRoute(position);
        Resources resources = activity.getApplicationContext().getResources();

        holder.binding.setRoute(route);
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

    @Override
    public int getItemCount() {
        return viewModel.getDatasetSize();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, date, distance, duration, points;
        public ImageView picture;
        private CardPlannedRouteBinding binding;

        public ViewHolder(CardPlannedRouteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            title = binding.textViewTitlePlannedRoute;
            date = binding.textViewDate;
            points = binding.textViewPoints;
            distance = binding.textViewDistance;
            duration = binding.textViewDuration;
            picture = binding.imageViewCardMap;
        }
    }

}
