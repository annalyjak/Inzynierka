package lyjak.anna.inzynierka.view.fragments;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import lyjak.anna.inzynierka.R;
import lyjak.anna.inzynierka.databinding.DialogActualRouteCardClickBinding;
import lyjak.anna.inzynierka.service.respository.OnMarkersOperations;
import lyjak.anna.inzynierka.view.activities.MainActivity;
import lyjak.anna.inzynierka.view.activities.MapsActivity;
import lyjak.anna.inzynierka.view.adapters.ActualRouteAdapter;
import lyjak.anna.inzynierka.databinding.FragmentActualRoutesBinding;
import lyjak.anna.inzynierka.service.model.realm.Route;
import lyjak.anna.inzynierka.view.callbacks.ActualRouteCallback;
import lyjak.anna.inzynierka.viewmodel.report.GenerateReport;
import lyjak.anna.inzynierka.viewmodel.vm.ActualRouteListViewModel;

public class ActualRoutesFragment extends Fragment {

    private static final String TAG = ActualRoutesFragment.class.getSimpleName();

    private static ActualRouteListViewModel viewModel;
    private static OnMarkersOperations operations;
    private FragmentActualRoutesBinding binding;
//    public RecyclerView mRecyclerView;
    private ActualRouteAdapter mAdapter;
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_actual_routes,
                container, false);
        binding.recycleViewActualRoutes.setHasFixedSize(true);

        myDataset=new ArrayList<>();
        operations = new OnMarkersOperations(getActivity());
        mAdapter = new ActualRouteAdapter(actualRouteClickCallback, getActivity(), myDataset);

        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        binding.recycleViewActualRoutes.setLayoutManager(mLayoutManager);
        binding.recycleViewActualRoutes.setItemAnimator(new DefaultItemAnimator());
        binding.recycleViewActualRoutes.setAdapter(mAdapter);

//        getRoutesFromDatabase();

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ActualRouteListViewModel.Factory factory = new ActualRouteListViewModel.Factory(
                getActivity());

        viewModel = ViewModelProviders.of(this, factory)
                .get(ActualRouteListViewModel.class);

        observeViewModel(viewModel);
    }

    private void observeViewModel(ActualRouteListViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getRouteListObservable().observe(this, new Observer<List<Route>>() {
            @Override
            public void onChanged(@Nullable List<Route> projects) {
                if (projects != null) {
                    mAdapter.setRoadList(projects);
                }
            }
        });
    }

    private static void removeThisItemFromDatabase(Route route) {
        if (route != null) {
            viewModel.removeRoute(route);
        }
    }

    private final ActualRouteCallback actualRouteClickCallback = new ActualRouteCallback() {
        @Override
        public void onClick(Route route) {
            final Dialog dialog = new Dialog(getActivity(), R.style.SettingsDialogStyle);
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity().getApplicationContext());
            DialogActualRouteCardClickBinding viewDataBinding = DataBindingUtil
                    .inflate(layoutInflater,
                            R.layout.dialog_actual_route_card_click,
                            null, false);

            viewDataBinding.buttonGenerateReport.setOnClickListener(v2 -> {
                dialog.dismiss();
                final TransportSelectionFragment fragment = TransportSelectionFragment
                        .newInstance(route);
                MainActivity.attachNewFragment(fragment);
            });
            viewDataBinding.buttonShowRouteOnMap.setOnClickListener(v12 -> {
                dialog.dismiss();
                Intent openMapIntent = new Intent(((Dialog) dialog).getContext(),
                        MapsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("title", "@ACTUALL_ROUTE@");
                bundle.putLong("date", route.getDate().getTime());
                if (route.getStartDate() != null) {
                    bundle.putLong("startDate", route.getStartDate().getTime());
                }
                if (route.getEndDate() != null) {
                    bundle.putLong("endDate", route.getEndDate().getTime());
                }
                openMapIntent.putExtras(bundle);
                getActivity().startActivity(openMapIntent);
            });
            viewDataBinding.buttonDeleteRoute.setOnClickListener(view2 -> {
                dialog.dismiss();
                removeThisItemFromDatabase(route);
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).notyfyDataSetChange();
                }
            });
            viewDataBinding.buttonAnuluj.setOnClickListener(v2 -> dialog.dismiss());

            dialog.setContentView(viewDataBinding.getRoot());
            dialog.show();
        }
    };

}
