package lyjak.anna.inzynierka.view.adapters;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import lyjak.anna.inzynierka.R;
import lyjak.anna.inzynierka.databinding.CardActualRouteBinding;
import lyjak.anna.inzynierka.databinding.CardPointOfRouteBinding;
import lyjak.anna.inzynierka.service.model.realm.PlannedRoute;
import lyjak.anna.inzynierka.service.model.realm.PointOfRoute;
import lyjak.anna.inzynierka.viewmodel.listeners.ItemTouchHelperViewHolder;
import lyjak.anna.inzynierka.viewmodel.listeners.OnCardViewTouchListener;
import lyjak.anna.inzynierka.viewmodel.listeners.OnStartDragListener;
import lyjak.anna.inzynierka.service.respository.OnMarkersOperations;
import lyjak.anna.inzynierka.viewmodel.tasks.PointImageFromUrlAsyncTask;
import lyjak.anna.inzynierka.viewmodel.utils.GoogleMapsStaticUtil;

/**
 * Created by Anna on 20.10.2017.
 */

public class PointsAdapter extends RecyclerView.Adapter<PointsAdapter.ViewHolder> implements OnCardViewTouchListener {

    private List<PointOfRoute> mDataset;
    private PlannedRoute route;
    private static Activity activity;

    private final OnStartDragListener mDragStartListener;

    public PointsAdapter(Activity contexts, OnStartDragListener dragStartListener,
                         List<PointOfRoute> myDataset, PlannedRoute route) {
        this.activity = contexts;
        this.route = route;
        this.mDragStartListener = dragStartListener;
        this.mDataset = myDataset;
    }

    @Override
    public PointsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardPointOfRouteBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.card_point_of_route,
                        parent, false);
       return new PointsAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final PointsAdapter.ViewHolder holder, int position) {
        PointOfRoute pointOfRoute = mDataset.get(position);

        holder.binding.textViewRouteId.setText((pointOfRoute.getId() + " - " + pointOfRoute.getName()));
        setImage(holder, pointOfRoute);

        holder.binding.imageViewCardMap.setOnTouchListener((v, event) -> {
            if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                mDragStartListener.onStartDrag(holder);
                return true;
            }
            return false;
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public void onCardMove(int fromPosition, int toPosition) {
        OnMarkersOperations operations = new OnMarkersOperations(activity);
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                operations.swap(mDataset, i, i + 1);
                operations.calculateLine(route);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                operations.swap(mDataset, i, i - 1);
                operations.calculateLine(route);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onCardDismiss(int position) {
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    private void setImage(PointsAdapter.ViewHolder holder, PointOfRoute pointOfRoute) {
        LatLng latLng = new LatLng(
                pointOfRoute.getPoint().getLatitude(),
                pointOfRoute.getPoint().getLongitude());
        PointImageFromUrlAsyncTask setImageTask = new PointImageFromUrlAsyncTask(
                holder.binding.imageViewCardMap);
        setImageTask.execute(latLng);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        CardPointOfRouteBinding binding;

        public ViewHolder(CardPointOfRouteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
}
