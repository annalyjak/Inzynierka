package lyjak.anna.inzynierka.view.fragments;

import android.databinding.DataBindingUtil;
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
import lyjak.anna.inzynierka.view.adapters.ActualRouteAdapter;
import lyjak.anna.inzynierka.databinding.FragmentActualRoutesBinding;
import lyjak.anna.inzynierka.service.model.realm.Route;
import lyjak.anna.inzynierka.viewmodel.report.GenerateReport;

public class ActualRoutesFragment extends Fragment {

    private static final String TAG = ActualRoutesFragment.class.getSimpleName();

//    public RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private List<Route> myDataset;

    private GenerateReport mGenerateReport;

    public ActualRoutesFragment() {
        // Required empty public constructor
    }

    public static ActualRoutesFragment newInstance() {
        ActualRoutesFragment fragment = new ActualRoutesFragment();
        return fragment;
    }

    public static ActualRoutesFragment newInstance(GenerateReport mGenerateReport) {
        ActualRoutesFragment fragment = new ActualRoutesFragment();
        fragment.mGenerateReport = mGenerateReport;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentActualRoutesBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_actual_routes, container, false);
//        View view = inflater.inflate(R.layout.fragment_actual_routes, container, false);
//        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycleViewActualRoutes);
        binding.recycleViewActualRoutes.setHasFixedSize(true);

//        Log.i(TAG, mRecyclerView == null? "null" : mRecyclerView.toString());
        myDataset=new ArrayList<>();
        mAdapter = new ActualRouteAdapter(getActivity(), myDataset);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        binding.recycleViewActualRoutes.setLayoutManager(mLayoutManager);
        binding.recycleViewActualRoutes.setItemAnimator(new DefaultItemAnimator());
        binding.recycleViewActualRoutes.setAdapter(mAdapter);

        getRoutesFromDatabase();

        return binding.getRoot();
    }

    private void getRoutesFromDatabase() {
        Realm.init(getActivity());
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Route> results = realm.where(Route.class).findAllAsync();
        results.load();

        myDataset = new ArrayList<>(results);

        Log.i("TAG", "SIZE : " + myDataset.size());

        for(Route route : results) {
            ((ActualRouteAdapter) mAdapter).addNewRoute(route);
        }
        mAdapter.notifyDataSetChanged();
    }

}
