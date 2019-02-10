package lyjak.anna.inzynierka.view.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lyjak.anna.inzynierka.R;
import lyjak.anna.inzynierka.model.reports.Plane;
import lyjak.anna.inzynierka.model.reports.Railway;
import lyjak.anna.inzynierka.model.reports.Ship;
import lyjak.anna.inzynierka.view.activities.MainActivity;
import lyjak.anna.inzynierka.databinding.FragmentTransportSelectionBinding;
import lyjak.anna.inzynierka.model.realmObjects.PlannedRoute;
import lyjak.anna.inzynierka.model.realmObjects.Route;
import lyjak.anna.inzynierka.model.reports.Bus;
import lyjak.anna.inzynierka.model.reports.Car;
import lyjak.anna.inzynierka.viewmodel.report.GenerateStandardReport;
import lyjak.anna.inzynierka.model.reports.Tir;
import lyjak.anna.inzynierka.model.reports.TypeOfTransport;

public class TransportSelectionFragment extends Fragment {

    private PlannedRoute mPlannedRoute;
    private Route mRoute;
    private TypeOfTransport mTypeOfTransport;
    private Boolean setTypeOfTransportInRoute = false;

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
        binding.imageButtonPlaneTransport.setOnClickListener(v14 -> setPlaneTransport());
        binding.imageButtonRailwayTransport.setOnClickListener(v14 -> setRailwayTransport());
        binding.imageButtonShipTransport.setOnClickListener(v15 -> setShipTransport());
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

    public void setPlaneTransport() {
        mTypeOfTransport = new Plane();
        openNextFragment();
    }

    public void setShipTransport() {
        mTypeOfTransport = new Ship();
        openNextFragment();
    }

    public void setRailwayTransport() {
        mTypeOfTransport = new Railway();
        openNextFragment();
    }

    private void openNextFragment() {
        GenerateStandardReport generateStandardReport = new GenerateStandardReport();
        if (mPlannedRoute != null) {
            generateStandardReport.setPlannedRoute(mPlannedRoute);
        }
        if (mRoute != null) {
            generateStandardReport.addActualRoute(mRoute);
        }
        generateStandardReport.setTypeOfTransport(mTypeOfTransport);
        CombustionFragment fragment = CombustionFragment.newInstance(generateStandardReport);
        MainActivity.attachNewFragment(fragment);
    }
}
