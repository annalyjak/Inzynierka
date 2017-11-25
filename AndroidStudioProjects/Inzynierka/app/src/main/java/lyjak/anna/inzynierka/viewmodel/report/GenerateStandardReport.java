package lyjak.anna.inzynierka.viewmodel.report;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import lyjak.anna.inzynierka.service.model.TypeOfTransport;
import lyjak.anna.inzynierka.service.model.realm.PlannedRoute;
import lyjak.anna.inzynierka.service.model.realm.Route;
import lyjak.anna.inzynierka.viewmodel.report.modelDTO.PlannedRouteForReportDTO;

/**
 *
 * Created by Anna on 21.10.2017.
 */

public class GenerateStandardReport {

    private static final String TAG = GenerateStandardReport.class.getSimpleName();

    private PlannedRoute mPlannedRoute;
    private List<Route> mActualRoutes;
    private TypeOfTransport mTypeOfTransport;
    private Combustion mCombustion;
    private TimeTripInfo mTimeTripInfo;
    private AdditionalFields mAdditionalFields;

    public GenerateStandardReport() {}

    public void setTypeOfTransport(TypeOfTransport typeOfTransport) {
        this.mTypeOfTransport = typeOfTransport;
    }

    public TypeOfTransport getTypeOfTransport() {
        return mTypeOfTransport;
    }

    public void setPlannedRoute(PlannedRoute plannedRoute) {
        this.mPlannedRoute = plannedRoute;
    }

    public void setActualRoute(List<Route> actualRoutes) {
        this.mActualRoutes = actualRoutes;
    }

    public void addActualRoute(Route route) {
        if (mActualRoutes == null) {
            mActualRoutes = new ArrayList<>();
        }
        mActualRoutes.add(route);
    }

    public void setCombustion(Combustion combustion) {
        this.mCombustion = combustion;
    }

    public void setTimeTripInfo(TimeTripInfo timeTripInfo) {
        this.mTimeTripInfo = timeTripInfo;
    }

    public void setAdditionalFields(AdditionalFields mAdditionalFields) {
        this.mAdditionalFields = mAdditionalFields;
    }

    public boolean plannedRouteSelected() {
        return mPlannedRoute == null;
    }

    public GenerateStandardReport getGenerateReportObject() {
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void createPdf(Activity activity, Context context, Bitmap bitmap) {
        String targetPdf = "/LogMilesRaport" + System.currentTimeMillis() + ".pdf";
        GeneratePdf pdf = new GeneratePdf(activity);
        pdf.setBitmap(bitmap);
        PlannedRouteForReportDTO prfr = PlannedRouteForReportDTO.getInstance(mPlannedRoute);
        ProgressDialog progressDialog = ProgressDialog.show(activity,
                "Please wait ...",  "Task in progress ...", true);
        progressDialog.setCancelable(true);
        new Thread(() -> {
            try {
                File savedFile = pdf.generateBuissnesTripReport(prfr, targetPdf);
                sendFile(activity, savedFile);
            } catch (Exception e) {
                Log.e("error: ", e.getMessage());
            }
            progressDialog.dismiss();
        }).start();
    }

    private void sendFile(Activity activity, File file) {
        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        if(file.exists()) {
            intentShareFile.setType("application/pdf");
            Log.i(TAG, file.getAbsolutePath());
            intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+file.getAbsolutePath()));

            intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                    "Raport");
            intentShareFile.putExtra(Intent.EXTRA_TEXT, "Witam, przesyłam raport z podróży służbowej. Z poważaniem, ");

            activity.startActivity(Intent.createChooser(intentShareFile, "Share File"));
        } else {
            Log.i(TAG, "Plik nie istnieje");
        }
    }

}
