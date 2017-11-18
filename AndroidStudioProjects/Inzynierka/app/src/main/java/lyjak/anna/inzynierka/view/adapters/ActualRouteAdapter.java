package lyjak.anna.inzynierka.view.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import lyjak.anna.inzynierka.R;
import lyjak.anna.inzynierka.databinding.CardActualRouteBinding;
import lyjak.anna.inzynierka.service.model.realm.Route;
import lyjak.anna.inzynierka.view.callbacks.ActualRouteCallback;
import lyjak.anna.inzynierka.service.utils.DistanceAndDurationUtil;

/**
 * Created by Anna on 14.10.2017.
 */

public class ActualRouteAdapter extends RecyclerView.Adapter<ActualRouteAdapter.ViewHolder> {

    private static final String TAG = ActualRouteAdapter.class.getSimpleName();
    private static final String DATE_FORMAT = "%1$ta, %1$te %1$tB %1$tY"; // Pon, 12 października 2017

    private static List<Route> mDataset;
    private static Activity activity;
    //TODO przenieść inicjację tego Callbacka do MainActivity
    private ActualRouteCallback actualRouteClickCallback;
//    = new ActualRouteCallback() {
//        @Override
//        public void onClick(Route route) {
//            final Dialog dialog = new Dialog(activity, R.style.SettingsDialogStyle);
//            LayoutInflater layoutInflater = LayoutInflater.from(activity.getApplicationContext());
//            DialogActualRouteCardClickBinding viewDataBinding = DataBindingUtil
//                    .inflate(layoutInflater,
//                            R.layout.dialog_actual_route_card_click,
//                            null, false);
//
//            viewDataBinding.buttonGenerateReport.setOnClickListener(v2 -> {
//                dialog.dismiss();
//                final TransportSelectionFragment fragment = TransportSelectionFragment
//                        .newInstance(route);
//                MainActivity.attachNewFragment(fragment);
//            });
//            viewDataBinding.buttonShowRouteOnMap.setOnClickListener(v12 -> {
//                dialog.dismiss();
//                Intent openMapIntent = new Intent(((Dialog) dialog).getContext(),
//                        MapsActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("title", "@ACTUALL_ROUTE@");
//                bundle.putLong("date", route.getDate().getTime());
//                if (route.getStartDate() != null) {
//                    bundle.putLong("startDate", route.getStartDate().getTime());
//                }
//                if (route.getEndDate() != null) {
//                    bundle.putLong("endDate", route.getEndDate().getTime());
//                }
//                openMapIntent.putExtras(bundle);
//                activity.startActivity(openMapIntent);
//            });
//            viewDataBinding.buttonDeleteRoute.setOnClickListener(view2 -> {
//                dialog.dismiss();
//                removeThisItemFromDatabase(route);
//                if (activity instanceof MainActivity) {
//                    ((MainActivity) activity).notyfyDataSetChange();
//                }
//            });
//            viewDataBinding.buttonAnuluj.setOnClickListener(v2 -> dialog.dismiss());
//
//            dialog.setContentView(viewDataBinding.getRoot());
//            dialog.show();
//        }
//    };

    public ActualRouteAdapter(ActualRouteCallback actualRouteClickCallback, Activity activity, List<Route> myDataset) {
        this.actualRouteClickCallback = actualRouteClickCallback;
        this.activity = activity;
        this.mDataset = myDataset;
    }

    public void setRoadList(final List<Route> mDataset) {
        if (this.mDataset == null) {
            this.mDataset = mDataset;
            notifyItemRangeInserted(0, mDataset.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return ActualRouteAdapter.this.mDataset.size();
                }

                @Override
                public int getNewListSize() {
                    return mDataset.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return ActualRouteAdapter.this.mDataset.get(oldItemPosition).getDate().equals(
                            mDataset.get(newItemPosition).getDate());
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Route route = mDataset.get(newItemPosition);
                    Route old = mDataset.get(oldItemPosition);
                    return route.getDate().equals(old.getDate()) && route.equals(old);
                }
            });
            this.mDataset = mDataset;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public ActualRouteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardActualRouteBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.card_actual_route,
                        parent, false);

        binding.setCallback(actualRouteClickCallback);

        return new ActualRouteAdapter.ViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mDataset.get(position).isValid()) {
            Route route = mDataset.get(position);
            Resources resources = activity.getResources();

            holder.position = position;
            holder.binding.setRoute(route);
            holder.binding.textViewRouteId.setText(resources.getString(R.string.cardview_id) + " " + position);
            holder.binding.textViewActualDistance.setText(resources.getString(R.string.cardview_distance) + " " +
                    DistanceAndDurationUtil.calculateDistance(route.getLocations()));
            holder.binding.executePendingBindings();
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void addNewRoute(Route route) {
        mDataset.add(route);
    }

//    @Deprecated
//    private static void removeThisItemFromDatabase(int position) {
//        if (position >= 0) {
//            Route routeToRemove = mDataset.get(position);
//            Log.i(TAG, "Usuwam trasę o id: " + position);
//            OnMarkersOperations operations = new OnMarkersOperations(activity);
//            operations.removeRouteFromDatabase(routeToRemove);
//        }
//    }

//    private static void removeThisItemFromDatabase(Route route) {
//        if (route != null) {
//            OnMarkersOperations operations = new OnMarkersOperations(activity);
//            operations.removeRouteFromDatabase(route);
//        }
//    }

    private static Route getSelectedRoute(int position) {
        if (position >= 0) {
            Route route = mDataset.get(position);
            return route;
        }
        return null;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public int position;
        final CardActualRouteBinding binding;

        public ViewHolder(CardActualRouteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }
}
