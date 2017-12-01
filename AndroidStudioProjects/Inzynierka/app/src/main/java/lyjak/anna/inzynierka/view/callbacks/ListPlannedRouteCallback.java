package lyjak.anna.inzynierka.view.callbacks;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

import lyjak.anna.inzynierka.R;
import lyjak.anna.inzynierka.databinding.DialogPlannedRouteCardClickBinding;
import lyjak.anna.inzynierka.service.model.realm.PlannedRoute;
import lyjak.anna.inzynierka.view.activities.MainActivity;
import lyjak.anna.inzynierka.view.activities.MapsActivity;
import lyjak.anna.inzynierka.view.fragments.PointsFragment;
import lyjak.anna.inzynierka.view.fragments.TransportSelectionFragment;
import lyjak.anna.inzynierka.viewmodel.MapsViewModel;
import lyjak.anna.inzynierka.viewmodel.PlannedRoutesCardListViewModel;
import lyjak.anna.inzynierka.viewmodel.report.GeneratePlannedRouteReport;

/**
 *
 * Created by Anna on 20.11.2017.
 */

public class ListPlannedRouteCallback implements PlannedRouteCallback {

    private Activity activity;
    private PlannedRoutesCardListViewModel viewModel;

    public ListPlannedRouteCallback(Activity activity, PlannedRoutesCardListViewModel viewModel) {
        this.activity = activity;
        this.viewModel = viewModel;
    }

    @Override
    public void onClick(PlannedRoute route) {
        final Dialog dialog = new Dialog(activity, R.style.SettingsDialogStyle);
        LayoutInflater layoutInflater = LayoutInflater.from(activity.getApplicationContext());
        DialogPlannedRouteCardClickBinding viewDataBinding = DataBindingUtil
                .inflate(layoutInflater,
                        R.layout.dialog_planned_route_card_click,
                        null, false);
        viewDataBinding.buttonGenerateReport.setOnClickListener(v2 -> {
            dialog.dismiss();
            final TransportSelectionFragment fragment = TransportSelectionFragment
                    .newInstance(route);
            MainActivity.attachNewFragment(fragment);
        });
        viewDataBinding.buttonGeneratePlannedRouteReport.setOnClickListener((View v2) -> {
            dialog.dismiss();

            GeneratePlannedRouteReport report = new GeneratePlannedRouteReport(route);
            Intent openMapIntent = new Intent(activity,
                    MapsActivity.class);
            MapsViewModel.reportPlannedRoute = report;
            Bundle bundle = new Bundle();
//            bundle.putString("title", route.getTitle());
//            bundle.putInt("duration", route.getDuration());
//            bundle.putInt("distance", route.getDistance());
            bundle.putBoolean("REPORT", true);
            openMapIntent.putExtras(bundle);
            activity.startActivity(openMapIntent);
        });
        viewDataBinding.buttonShowRouteOnMap.setOnClickListener(v -> {
            dialog.dismiss();
            Intent openMapIntent = new Intent(dialog.getContext(),
                    MapsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("title", route.getTitle());
            bundle.putInt("duration", route.getDuration());
            bundle.putInt("distance", route.getDistance());
            openMapIntent.putExtras(bundle);
            activity.startActivity(openMapIntent);
        });
        viewDataBinding.buttonShowAllPoints.setOnClickListener(v -> {
            dialog.dismiss();
            Fragment newFragment = PointsFragment.newInstance(route);
            ((MainActivity) activity).attachNewFragment(newFragment);
        });
        viewDataBinding.buttonDeleteRoute.setOnClickListener(v -> {
            dialog.dismiss();
            viewModel.removePlannedRoute(route);
            if (activity instanceof MainActivity) {
                ((MainActivity) activity).notyfyDataSetChange();
            }
        });
        viewDataBinding.buttonAnuluj.setOnClickListener(v -> dialog.dismiss());
        dialog.setContentView(viewDataBinding.getRoot());
        dialog.show();

    }
}
