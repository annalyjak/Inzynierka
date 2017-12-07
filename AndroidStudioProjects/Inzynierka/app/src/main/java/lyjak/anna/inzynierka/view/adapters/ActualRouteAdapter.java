package lyjak.anna.inzynierka.view.adapters;

import android.app.Activity;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import lyjak.anna.inzynierka.R;
import lyjak.anna.inzynierka.databinding.CardActualRouteBinding;
import lyjak.anna.inzynierka.model.realmObjects.Route;
import lyjak.anna.inzynierka.model.utils.DistanceAndDurationUtil;
import lyjak.anna.inzynierka.view.callbacks.ActualRouteCallback;
import lyjak.anna.inzynierka.viewmodel.ActualRouteListViewModel;

/**
 * Created by Anna on 14.10.2017.
 */

public class ActualRouteAdapter extends RecyclerView.Adapter<ActualRouteAdapter.ViewHolder> {

    private static final String TAG = ActualRouteAdapter.class.getSimpleName();
//    private static final String DATE_FORMAT = "%1$ta, %1$te %1$tB %1$tY"; // Pon, 12 października 2017

    private Activity activity;
    private static ActualRouteListViewModel viewModel;

    private ActualRouteCallback actualRouteClickCallback;

    public ActualRouteAdapter(Activity activity, ActualRouteListViewModel viewModel,
                              ActualRouteCallback callback) {
        this.activity = activity;
        ActualRouteAdapter.viewModel = viewModel;
        this.actualRouteClickCallback = callback;
    }

    @Override
    public ActualRouteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardActualRouteBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.card_actual_route,
                        parent, false);

        binding.setCallback(actualRouteClickCallback);

        return new ActualRouteAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Route route = viewModel.getRoute(position);
        if (route.isValid()) {
            Resources resources = activity.getResources();
            holder.position = position;
            holder.binding.setRoute(route);
            holder.binding.textViewRouteId.setText((resources.getString(R.string.cardview_id) + " " + position));
            holder.binding.textViewActualDistance.setText((resources.getString(R.string.cardview_distance) + " " +
                    DistanceAndDurationUtil.calculateDistanceWithTextResult(route.getLocations())));
            holder.binding.executePendingBindings();
        }
    }

    @Override
    public int getItemCount() {
        return viewModel.getRoutesSize();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public int position;
        final CardActualRouteBinding binding;

        ViewHolder(CardActualRouteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }
}
