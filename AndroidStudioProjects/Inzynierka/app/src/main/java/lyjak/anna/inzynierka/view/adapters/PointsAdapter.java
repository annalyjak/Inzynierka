package lyjak.anna.inzynierka.view.adapters;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;

import lyjak.anna.inzynierka.R;
import lyjak.anna.inzynierka.databinding.CardPointOfRouteBinding;
import lyjak.anna.inzynierka.model.realmObjects.PointOfRoute;
import lyjak.anna.inzynierka.viewmodel.PointsCardListViewModel;
import lyjak.anna.inzynierka.viewmodel.listeners.ItemTouchHelperViewHolder;
import lyjak.anna.inzynierka.viewmodel.listeners.OnCardViewTouchListener;
import lyjak.anna.inzynierka.viewmodel.listeners.OnStartDragListener;
import lyjak.anna.inzynierka.viewmodel.tasks.PointImageFromUrlAsyncTask;

/**
 * Created by Anna on 20.10.2017.
 */

public class PointsAdapter extends RecyclerView.Adapter<PointsAdapter.ViewHolder> implements OnCardViewTouchListener {

    private PointsCardListViewModel viewModel;

    private final OnStartDragListener mDragStartListener;

    public PointsAdapter(OnStartDragListener dragStartListener, PointsCardListViewModel viewModel) {
        this.mDragStartListener = dragStartListener;
        this.viewModel = viewModel;
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
        PointOfRoute pointOfRoute = viewModel.getPoint(position);

        holder.binding.textViewRouteId.setText((pointOfRoute.getId() + " - " + pointOfRoute.getName()));
        setImage(holder, pointOfRoute);

        holder.binding.imageViewCardMap.setOnTouchListener((v, event) -> {
            if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                mDragStartListener.onStartDrag(holder);
            }
            v.performClick();
            return true;
        });

    }

    @Override
    public int getItemCount() {
        return viewModel.getDatasetSize();
    }

    @Override
    public void onCardMove(int fromPosition, int toPosition) {
        viewModel.onCardMove(fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onCardDismiss(int position) {
        viewModel.removePoint(position);
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
