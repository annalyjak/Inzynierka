package lyjak.anna.inzynierka.view.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lyjak.anna.inzynierka.R;
import lyjak.anna.inzynierka.databinding.FragmentPlannedRoutesBinding;
import lyjak.anna.inzynierka.view.adapters.PlannedRouteAdapter;
import lyjak.anna.inzynierka.view.callbacks.ListPlannedRouteCallback;
import lyjak.anna.inzynierka.view.callbacks.PlannedRouteCallback;
import lyjak.anna.inzynierka.view.callbacks.ReportPlannedRouteCallback;
import lyjak.anna.inzynierka.viewmodel.PlannedRoutesCardListViewModel;
import lyjak.anna.inzynierka.viewmodel.report.GenerateReport;

public class PlannedRoutesFragment extends Fragment {

    private RecyclerView.Adapter mAdapter;
    private PlannedRoutesCardListViewModel viewModel;

    private GenerateReport mGenerateReport;
    private PlannedRouteCallback plannedRouteClickCallback;

    public PlannedRoutesFragment() {
        // Required empty public constructor
    }

    public static PlannedRoutesFragment newInstance(GenerateReport mGenerateReport) {
        PlannedRoutesFragment fragment = new PlannedRoutesFragment();
        fragment.viewModel = new PlannedRoutesCardListViewModel(fragment.getContext());
        fragment.mGenerateReport = mGenerateReport;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentPlannedRoutesBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_planned_routes, container, false);

        if (viewModel == null) {
            viewModel = new PlannedRoutesCardListViewModel(getContext());
        }
        if (mGenerateReport == null) {
            plannedRouteClickCallback = new ListPlannedRouteCallback(getActivity(), viewModel);
//            mAdapter = new PlannedRouteAdapter(getActivity(), viewModel, plannedRouteClickCallback);
        } else {
            plannedRouteClickCallback = new ReportPlannedRouteCallback(getActivity(), mGenerateReport);
        }
        mAdapter = new PlannedRouteAdapter(getActivity(), viewModel, plannedRouteClickCallback);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        binding.recycleViewPlannedRoutes.setLayoutManager(mLayoutManager);
        binding.recycleViewPlannedRoutes.setItemAnimator(new DefaultItemAnimator());
        binding.recycleViewPlannedRoutes.setAdapter(mAdapter);

        getPlannedRoutesFromDatabase();
        return binding.getRoot();
    }

    public void getPlannedRoutesFromDatabase() {
        viewModel.getPlannedRoutesFromDatabase();
        mAdapter.notifyDataSetChanged();
    }
}
