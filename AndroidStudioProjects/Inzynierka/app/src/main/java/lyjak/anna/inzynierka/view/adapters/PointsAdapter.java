package lyjak.anna.inzynierka.view.adapters;

import android.app.Activity;
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
import lyjak.anna.inzynierka.service.model.realm.PlannedRoute;
import lyjak.anna.inzynierka.service.model.realm.PointOfRoute;
import lyjak.anna.inzynierka.view.listeners.ItemTouchHelperViewHolder;
import lyjak.anna.inzynierka.view.listeners.OnCardViewTouchListener;
import lyjak.anna.inzynierka.view.listeners.OnStartDragListener;
import lyjak.anna.inzynierka.service.respository.OnMarkersOperations;
import lyjak.anna.inzynierka.service.utils.GoogleMapsStaticUtil;

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
        View v = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.card_point_of_route, parent, false);
        return new PointsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final PointsAdapter.ViewHolder holder, int position) {
        PointOfRoute pointOfRoute = mDataset.get(position);

        holder.title.setText(pointOfRoute.getId() + " - " + pointOfRoute.getName());

        AsyncTask<LatLng, Void, Bitmap> setImageFromUrl = new AsyncTask<LatLng, Void, Bitmap>(){

            @Override
            protected Bitmap doInBackground(LatLng... points) {
                return GoogleMapsStaticUtil.getGoogleMapStaticPicture(
                        points[0].latitude,
                        points[0].longitude
                );
            }

            protected void onPostExecute(Bitmap bmp) {
                if (bmp!=null) {
                    holder.picture.setImageBitmap(bmp);
                }

            }
        };
        LatLng latLng = new LatLng(
                pointOfRoute.getPoint().getLatitude(),
                pointOfRoute.getPoint().getLongitude());
        setImageFromUrl.execute(latLng);

        holder.picture.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
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
//        return true;
    }

    @Override
    public void onCardDismiss(int position) {
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {
        public TextView title;
        public ImageView picture;

        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.textViewRouteId);
            picture = (ImageView) v.findViewById(R.id.imageViewCardMap);
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
