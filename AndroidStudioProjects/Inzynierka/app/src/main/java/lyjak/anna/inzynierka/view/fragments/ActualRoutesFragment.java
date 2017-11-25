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
import lyjak.anna.inzynierka.view.adapters.ActualRouteAdapter;
import lyjak.anna.inzynierka.databinding.FragmentActualRoutesBinding;
import lyjak.anna.inzynierka.view.callbacks.ActualRouteCallback;
import lyjak.anna.inzynierka.view.callbacks.ListActualRouteCallback;
import lyjak.anna.inzynierka.view.callbacks.ReportActualRouteCallback;
import lyjak.anna.inzynierka.viewmodel.ActualRouteListViewModel;
import lyjak.anna.inzynierka.viewmodel.report.GenerateStandardReport;

public class ActualRoutesFragment extends Fragment {

    private static final String TAG = ActualRoutesFragment.class.getSimpleName();

    private ActualRouteListViewModel viewModel;
    private RecyclerView.Adapter mAdapter;
    private ActualRouteCallback actualRouteClickCallback;
    private GenerateStandardReport mGenerateStandardReport;

    public ActualRoutesFragment() {
        // Required empty public constructor
    }

    public static ActualRoutesFragment newInstance() {
        ActualRoutesFragment fragment = new ActualRoutesFragment();
        fragment.viewModel = new ActualRouteListViewModel(fragment.getContext());
        return fragment;
    }

    public static ActualRoutesFragment newInstance(GenerateStandardReport mGenerateStandardReport) {
        ActualRoutesFragment fragment = new ActualRoutesFragment();
        fragment.viewModel = new ActualRouteListViewModel(fragment.getContext());
        fragment.mGenerateStandardReport = mGenerateStandardReport;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentActualRoutesBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_actual_routes, container, false);
        binding.recycleViewActualRoutes.setHasFixedSize(true);

        if (viewModel == null) {
            viewModel = new ActualRouteListViewModel(getContext());
        }
        if (mGenerateStandardReport == null) {
            actualRouteClickCallback = new ListActualRouteCallback(getActivity(), viewModel);
        } else {
            actualRouteClickCallback = new ReportActualRouteCallback(getActivity(),
                    mGenerateStandardReport);
        }
        mAdapter = new ActualRouteAdapter(getActivity(), viewModel, actualRouteClickCallback);

        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        binding.recycleViewActualRoutes.setLayoutManager(mLayoutManager);
        binding.recycleViewActualRoutes.setItemAnimator(new DefaultItemAnimator());
        binding.recycleViewActualRoutes.setAdapter(mAdapter);

        getRoutesFromDatabase();

        return binding.getRoot();
    }

    private void getRoutesFromDatabase() {
        viewModel.getRoutes();
        mAdapter.notifyDataSetChanged();
    }

}
