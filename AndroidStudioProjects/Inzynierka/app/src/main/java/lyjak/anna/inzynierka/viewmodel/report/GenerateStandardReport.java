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

import lyjak.anna.inzynierka.model.modelDTO.ActualRouteForReportDTO;
import lyjak.anna.inzynierka.model.reports.TypeOfTransport;
import lyjak.anna.inzynierka.model.realmObjects.PlannedRoute;
import lyjak.anna.inzynierka.model.realmObjects.Route;
import lyjak.anna.inzynierka.model.repository.RouteRepository;
import lyjak.anna.inzynierka.model.modelDTO.PlannedRouteForReportDTO;

/**
 *
 * Created by Anna on 21.10.2017.
 */

public class GenerateStandardReport {

    private static final String TAG = GenerateStandardReport.class.getSimpleName();

    private PlannedRoute plannedRoute;
    private Route actualRoute;
    private TypeOfTransport typeOfTransport;
    private Combustion combustion;
    private TimeTripInfo timeTripInfo;
    private AdditionalFields additionalFields;

    public GenerateStandardReport() {}

    public void setTypeOfTransport(TypeOfTransport typeOfTransport) {
        this.typeOfTransport = typeOfTransport;
    }

    public TypeOfTransport getTypeOfTransport() {
        return typeOfTransport;
    }

    public void setPlannedRoute(PlannedRoute plannedRoute) {
        this.plannedRoute = plannedRoute;
    }

    public void setActualRoute(Route actualRoutes) {
        this.actualRoute = actualRoutes;
    }

    public void addActualRoute(Route route) {
        if (actualRoute == null) {
            actualRoute = route;
        }
    }

    public void setCombustion(Combustion combustion) {
        this.combustion = combustion;
    }

    public void setTimeTripInfo(TimeTripInfo timeTripInfo) {
        this.timeTripInfo = timeTripInfo;
    }

    public void setAdditionalFields(AdditionalFields mAdditionalFields) {
        this.additionalFields = mAdditionalFields;
    }

    public boolean plannedRouteSelected() {
        return plannedRoute == null;
    }

    public Route getActualRoute() {
        return actualRoute;
    }

    public GenerateStandardReport getGenerateReportObject() {
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void createPdf(Activity activity, Context context, Bitmap bitmap) {
        String targetPdf = "/LogMilesRaport" + System.currentTimeMillis() + ".pdf";
        GeneratePdf pdf = new GeneratePdf(activity, additionalFields);
        pdf.setBitmap(bitmap);
        if (timeTripInfo != null) {
            pdf.setTimeTripInfo(timeTripInfo);
        }
        if (combustion != null) {
            Log.i(TAG, "combustion != null");
            pdf.setCombustion(combustion);
        } else {
            Log.i(TAG, "combustion == null");
        }
        PlannedRouteForReportDTO prfr = PlannedRouteForReportDTO.getInstance(plannedRoute);
        Log.i(TAG, prfr.toString());

        ActualRouteForReportDTO arfr = ActualRouteForReportDTO.getInstance(actualRoute);
        Log.i(TAG, arfr.toString());
        ProgressDialog progressDialog = ProgressDialog.show(activity,
                "Please wait ...",  "Task in progress ...", true);
        progressDialog.setCancelable(false);
        RouteRepository service = new RouteRepository(context);
        service.createReportInDatabase(getPlannedRoute(), getActualRoute(), targetPdf);
        new Thread(() -> {
            try {
                File savedFile = pdf.generateBuissnesTripReport(
                        getTypeOfTransport(),
                        prfr,
                        arfr,
                        targetPdf);
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

    public PlannedRoute getPlannedRoute() {
        return plannedRoute;
    }
}
