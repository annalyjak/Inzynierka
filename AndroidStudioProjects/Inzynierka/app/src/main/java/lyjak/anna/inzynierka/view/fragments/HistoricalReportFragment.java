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
import lyjak.anna.inzynierka.databinding.FragmentHistoricalReportBinding;
import lyjak.anna.inzynierka.view.adapters.HistoricalReportAdapter;
import lyjak.anna.inzynierka.viewmodel.HistoricalReportCardListViewModel;

public class HistoricalReportFragment extends Fragment {

    private RecyclerView.Adapter adapter;
    private HistoricalReportCardListViewModel viewModel;

    public HistoricalReportFragment() {
        // Required empty public constructor
    }

    public static HistoricalReportFragment newInstance() {
        HistoricalReportFragment fragment = new HistoricalReportFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentHistoricalReportBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_historical_report, container, false);

        if (viewModel == null) {
            viewModel = new HistoricalReportCardListViewModel(getContext());
        }
        adapter = new HistoricalReportAdapter(getActivity(), viewModel);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        binding.recycleViewHistoricalReport.setLayoutManager(mLayoutManager);
        binding.recycleViewHistoricalReport.setItemAnimator(new DefaultItemAnimator());
        binding.recycleViewHistoricalReport.setAdapter(adapter);

        getReports();

        return binding.getRoot();
    }

    public void getReports() {
        viewModel.getReports();
        adapter.notifyDataSetChanged();
    }

}
