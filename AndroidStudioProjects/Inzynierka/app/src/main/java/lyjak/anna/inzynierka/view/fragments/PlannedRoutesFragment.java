package lyjak.anna.inzynierka.view.fragments;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import lyjak.anna.inzynierka.R;
import lyjak.anna.inzynierka.view.adapters.PlannedRouteAdapter;
import lyjak.anna.inzynierka.databinding.FragmentPlannedRoutesBinding;
import lyjak.anna.inzynierka.service.model.realm.PlannedRoute;
import lyjak.anna.inzynierka.viewmodel.report.GenerateReport;

public class PlannedRoutesFragment extends Fragment {

//    public RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private List<PlannedRoute> myDataset;

    private GenerateReport mGenerateReport;

    public PlannedRoutesFragment() {
        // Required empty public constructor
    }

    public static PlannedRoutesFragment newInstance(GenerateReport mGenerateReport) {
        PlannedRoutesFragment fragment = new PlannedRoutesFragment();
        fragment.mGenerateReport = mGenerateReport;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentPlannedRoutesBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_planned_routes, container, false);
//        View view = inflater.inflate(R.layout.fragment_planned_routes, container, false);
//        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycleViewPlannedRoutes);
        //  mRecyclerView.setHasFixedSize(true);

//        Log.i("", mRecyclerView == null? "null" : mRecyclerView.toString());
        myDataset=new ArrayList<>();
        if (mGenerateReport == null) {
            mAdapter = new PlannedRouteAdapter(getActivity(), myDataset);
        } else {
            mAdapter = new PlannedRouteAdapter(getActivity(), myDataset, mGenerateReport);
        }

        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());//(getActivity());
        binding.recycleViewPlannedRoutes.setLayoutManager(mLayoutManager);
        binding.recycleViewPlannedRoutes.setItemAnimator(new DefaultItemAnimator());
        binding.recycleViewPlannedRoutes.setAdapter(mAdapter);

        getPlannedRoutesFromDatabase();
        return binding.getRoot();
    }

    public void getPlannedRoutesFromDatabase() {
        Realm.init(getActivity());
        Realm realm = Realm.getDefaultInstance();
        RealmResults<PlannedRoute> results = realm.where(PlannedRoute.class).findAllAsync();
        results.load();

        myDataset = new ArrayList<>(results);

        Log.i("TAG", "SIZE : " + myDataset.size());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            results.stream().forEach(route -> ((PlannedRouteAdapter) mAdapter).addNewPlannedRoute(route));
        } else {
            for(PlannedRoute route : results) {
                ((PlannedRouteAdapter) mAdapter).addNewPlannedRoute(route);
            }
        }

        mAdapter.notifyDataSetChanged();
    }
}
