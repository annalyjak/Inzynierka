package lyjak.anna.inzynierka.view.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lyjak.anna.inzynierka.R;
import lyjak.anna.inzynierka.view.activities.MainActivity;
import lyjak.anna.inzynierka.databinding.FragmentTransportSelectionBinding;
import lyjak.anna.inzynierka.service.model.realm.PlannedRoute;
import lyjak.anna.inzynierka.service.model.realm.Route;
import lyjak.anna.inzynierka.service.model.Bus;
import lyjak.anna.inzynierka.service.model.Car;
import lyjak.anna.inzynierka.viewmodel.report.GenerateReport;
import lyjak.anna.inzynierka.service.model.Tir;
import lyjak.anna.inzynierka.service.model.TypeOfTransport;

public class TransportSelectionFragment extends Fragment {

    private PlannedRoute mPlannedRoute;
    private Route mRoute;
    private TypeOfTransport mTypeOfTransport;

    public PlannedRoute getPlannedRoute() {
        return mPlannedRoute;
    }

    public void setPlannedRoute(PlannedRoute mPlannedRoute) {
        this.mPlannedRoute = mPlannedRoute;
    }

    public Route getRoute() {
        return mRoute;
    }

    public void setRoute(Route mRoute) {
        this.mRoute = mRoute;
    }

    public TransportSelectionFragment() {
        // Required empty public constructor
    }

    public static TransportSelectionFragment newInstance(PlannedRoute plannedRoute) {
        final TransportSelectionFragment fragment = new TransportSelectionFragment();
        fragment.setPlannedRoute(plannedRoute);
        return fragment;
    }

    public static TransportSelectionFragment newInstance(Route route) {
        TransportSelectionFragment fragment = new TransportSelectionFragment();
        fragment.mRoute = route;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentTransportSelectionBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_transport_selection, container, false);
        binding.imageButtonCarTransport.setOnClickListener(v1 -> setCarTransport());
        binding.imageButtonBusTransport.setOnClickListener(v12 -> setBusTransport());
        binding.imageButtonTirTransport.setOnClickListener(v13 -> setTirTransport());
        return binding.getRoot();
    }

    public void setCarTransport() {
        mTypeOfTransport = new Car();
        openNextFragment();
    }

    public void setBusTransport() {
        mTypeOfTransport = new Bus();
        openNextFragment();
    }

    public void setTirTransport() {
        mTypeOfTransport = new Tir();
        openNextFragment();
    }

    private void openNextFragment() {
        GenerateReport generateReport = new GenerateReport();
        if (mPlannedRoute != null) {
            generateReport.setPlannedRoute(mPlannedRoute);
        }
        if (mRoute != null) {
            generateReport.addActualRoute(mRoute);
        }
        generateReport.setTypeOfTransport(mTypeOfTransport);
        CombustionFragment fragment = CombustionFragment.newInstance(generateReport);
        MainActivity.attachNewFragment(fragment);
    }
}
