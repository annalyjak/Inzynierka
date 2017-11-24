package lyjak.anna.inzynierka.view.callbacks;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;

import lyjak.anna.inzynierka.R;
import lyjak.anna.inzynierka.databinding.DialogAddingRouteToReportConfirmBinding;
import lyjak.anna.inzynierka.service.model.realm.PlannedRoute;
import lyjak.anna.inzynierka.view.activities.MapsActivity;
import lyjak.anna.inzynierka.viewmodel.MapsViewModel;
import lyjak.anna.inzynierka.viewmodel.report.GenerateReport;

/**
 * Created by Anna on 20.11.2017.
 */

public class ReportPlannedRouteCallback implements PlannedRouteCallback {

    Activity activity;
    private GenerateReport generateReport;

    public ReportPlannedRouteCallback(Activity activity, GenerateReport generateReport) {
        this.activity = activity;
        this.generateReport = generateReport;
    }

    @Override
    public void onClick(PlannedRoute route) {
        final Dialog dialog = new Dialog(activity, R.style.SettingsDialogStyle);
        LayoutInflater layoutInflater = LayoutInflater.from(activity.getApplicationContext());
        DialogAddingRouteToReportConfirmBinding viewDataBinding = DataBindingUtil
                .inflate(layoutInflater,
                        R.layout.dialog_adding_route_to_report_confirm,
                        null, false);
        viewDataBinding.buttonNo.setOnClickListener(v2 -> {
            dialog.dismiss();
        });
        viewDataBinding.buttonYes.setOnClickListener(v2 -> {
            dialog.dismiss();
            generateReport.setPlannedRoute(route);
            //TODO otworz kolejne pytanie "Czy chcesz przejść do generowania rapotu?"
            Intent openMapIntent = new Intent(activity,
                    MapsActivity.class);
            MapsViewModel.report = generateReport;
            Bundle bundle = new Bundle();
            bundle.putString("title", route.getTitle());
            bundle.putInt("duration", route.getDuration());
            bundle.putInt("distance", route.getDistance());
            bundle.putBoolean("REPORT", true);
            openMapIntent.putExtras(bundle);
            activity.startActivity(openMapIntent);
        });
        dialog.setContentView(viewDataBinding.getRoot());
        dialog.show();
    }
}
