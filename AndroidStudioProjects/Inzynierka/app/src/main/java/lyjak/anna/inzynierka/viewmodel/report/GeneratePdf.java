package lyjak.anna.inzynierka.viewmodel.report;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import lyjak.anna.inzynierka.model.reports.Car;
import lyjak.anna.inzynierka.model.reports.TypeOfTransport;
import lyjak.anna.inzynierka.model.realmObjects.Route;
import lyjak.anna.inzynierka.model.modelDTO.ActualRouteForReportDTO;
import lyjak.anna.inzynierka.model.modelDTO.PlannedRouteForReportDTO;

/**
 *
 * Created by Anna on 28.10.2017.
 */

public class GeneratePdf {

    private static final String TAG = GeneratePdf.class.getSimpleName();
    private AdditionalFields additionalFields;
    private Combustion combustion;
    private TimeTripInfo timeTripInfo;

    private Bitmap bitmap;

    private int x = 70;
    private int y = 70;
    private Document document;
    private File directory;
    private File file;
    private PdfWriter writer;
//    private int pageNumber;
    private Context context;

    GeneratePdf(Context context) {
        this.context = context;
    }

    public GeneratePdf(Context context, AdditionalFields mAdditionalFields) {
        this.context = context;
        this.additionalFields = mAdditionalFields;
    }

    void init(String fileName) throws IOException, DocumentException {
        directory = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS);
//        pageNumber = 1;
        file = new File(directory, fileName);
        if(!file.exists())
        {
            file.createNewFile();
        } else {
            file.delete();
            file.createNewFile();
        }
        document = new Document(PageSize.A4);
        writer = PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();
    }

    private File save() {
        document.close();
        Log.i(TAG, "Plik " + file  + " zapisany poprawnie");
        return file;
    }

    private Chunk getEmptyParagraph() {
        return Chunk.NEWLINE;
    }

    void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setCombustion(Combustion combustion) {
        this.combustion = combustion;
    }

    public void setTimeTripInfo(TimeTripInfo timeTripInfo) {
        this.timeTripInfo = timeTripInfo;
    }

    File generateBuissnesTripReport(TypeOfTransport type,
                                    PlannedRouteForReportDTO route,
                                    ActualRouteForReportDTO actualRoute,
                                    String fileName) {
        try {
            BuissnesTripPdfTable buissnesTripReport = new BuissnesTripPdfTable(context);
            PlannedRoutePdfTable plannedReport = new PlannedRoutePdfTable(context);
            AcctualRoutePdfTable acctualReport = new AcctualRoutePdfTable(context, bitmap);

            init(fileName);
            document.add(buissnesTripReport.getBasicTitleHeader());
            document.add(buissnesTripReport.createTypeOfTransport(type.getShortName()));
            if (combustion != null) {
                document.add(buissnesTripReport.createCombustionInfo(combustion));
            }
            if (additionalFields.isPersonalDataAboutEmployee()) {
                document.add(buissnesTripReport.createPersonalDataTable());
            } else {
                Log.i("", "additionalFields.isPersonalDataAboutEmployee() - is false");
            }
            if (additionalFields.isPurposeOfTravel()) {
                document.add(buissnesTripReport.createPurposeOfTravelTable());
            }
            if (additionalFields.isFeeding()) {
                document.add(buissnesTripReport.createFeedingTable());
            }
            if (additionalFields.isAccomodation()) {
                document.add(buissnesTripReport.createAccommodation());
            }
            if (additionalFields.isPublicTransport()) {
                document.add(buissnesTripReport.createTravelCost());
            }
            if (additionalFields.isHospital()) {
                document.add(buissnesTripReport.createHospitlization());
            }
            if(additionalFields.isOther()) {
                document.add(buissnesTripReport.createAnother());
            } else {
                Log.i("", "additionalFields.isOther() - is false");
            }
            document.add(buissnesTripReport.createEndingSummary());
            document.add(getEmptyParagraph());
//            document.add(getEmptyParagraph());
            document.add(buissnesTripReport.createFinalSignaturesSpace());
            document.add(buissnesTripReport.createFinalSignaturesExplanation());
            document.newPage(); //TRASA ZAPLANOWANA
            document.add(plannedReport.createTripReportTitle());
            document.add(getEmptyParagraph());
            document.add(plannedReport.createPlannedInfoHeader());
            document.add(getEmptyParagraph());
            document.add(plannedReport.createPlannedInfo(route, type));
            document.add(getEmptyParagraph());
            document.add(plannedReport.createPlannedTable(new PlannedRouteReportInfo(route)));
            document.newPage(); //TRASA ZREALIZOWANA
            document.add(acctualReport.createAcctuallInfoHeader());
            document.add(getEmptyParagraph());
            document.add(acctualReport.createAcctuallInfo(actualRoute));
//            document.add(createMapImage());

            return save();
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    File generatePlannedRouteReport(PlannedRouteForReportDTO route, String fileName) {
        try {
            PlannedRoutePdfTable plannedReport = new PlannedRoutePdfTable(context);

            init(fileName);
            document.add(plannedReport.createTripReportTitle());
            document.add(getEmptyParagraph());
            document.add(plannedReport.createPlannedInfoHeader());
            document.add(getEmptyParagraph());
            document.add(plannedReport.createPlannedInfo(route, new Car()));
            document.add(getEmptyParagraph());
            document.add(plannedReport.createPlannedTable(new PlannedRouteReportInfo(route)));
            if(bitmap != null) {
                document.newPage();
                document.add(plannedReport.createBitmap(bitmap));
            }
            return save();
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    File generateAcctualRouteReport(ActualRouteForReportDTO route, String fileName) {
        try {
            AcctualRoutePdfTable acctualReport = new AcctualRoutePdfTable(context, bitmap);
            init(fileName);
            document.add(acctualReport.createAcctuallInfoHeader());
            document.add(acctualReport.createAcctuallInfo(route));
            return save();
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

}
