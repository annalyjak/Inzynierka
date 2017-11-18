package lyjak.anna.inzynierka.view.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import lyjak.anna.inzynierka.R;
import lyjak.anna.inzynierka.view.adapters.PointsAdapter;
import lyjak.anna.inzynierka.databinding.FragmentPointsBinding;
import lyjak.anna.inzynierka.service.model.realm.PlannedRoute;
import lyjak.anna.inzynierka.service.model.realm.PointOfRoute;
import lyjak.anna.inzynierka.view.listeners.OnStartDragListener;
import lyjak.anna.inzynierka.view.callbacks.SimpleItemTouchHelperCallback;

public class PointsFragment extends Fragment implements OnStartDragListener {

    private static final String TAG = PointsFragment.class.getSimpleName();

//    public RecyclerView mRecyclerView;
    private FragmentPointsBinding binding;
    private PointsAdapter mAdapter;
    private List<PointOfRoute> myDataset;
    private PlannedRoute route;

    private ItemTouchHelper mItemTouchHelper;

    public PointsFragment() {
        // Required empty public constructor
    }

    public static PointsFragment newInstance(PlannedRoute plannedRoute) {
        PointsFragment fragment = new PointsFragment();
        fragment.route = plannedRoute;
        fragment.setDataset(plannedRoute.getPoints());
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_points, container, false);

//        View v = inflater.inflate(R.layout.fragment_points, container, false);
//        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycleViewPoints);

        if (myDataset == null) {
            Log.i(TAG, "Lista pusta, tworzę nową");
            myDataset = new ArrayList<>();
        } else {
            Log.i(TAG, "lista ma elemetów: " + myDataset.size());
        }
        mAdapter = new PointsAdapter(getActivity(), this, myDataset, route);

        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        binding.recycleViewPoints.setLayoutManager(mLayoutManager);
        binding.recycleViewPoints.setItemAnimator(new DefaultItemAnimator());
        binding.recycleViewPoints.setAdapter(mAdapter);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle save) {
        super.onViewCreated(view, save);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(binding.recycleViewPoints);
    }

    public void setDataset(List<PointOfRoute> list) {
        if (myDataset == null) {
            Log.i(TAG, "ustawiam nową listę " + list.size());
            myDataset = list;
        } else {
            Log.i(TAG, "dodaję elementy " + list.size());
            this.myDataset.addAll(list);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
