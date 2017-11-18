package lyjak.anna.inzynierka.viewmodel.report;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import lyjak.anna.inzynierka.R;
import lyjak.anna.inzynierka.service.model.Car;
import lyjak.anna.inzynierka.service.model.TypeOfTransport;
import lyjak.anna.inzynierka.service.model.realm.PlannedRoute;
import lyjak.anna.inzynierka.service.model.realm.Route;
import lyjak.anna.inzynierka.viewmodel.utils.DistanceAndDurationUtil;

/**
 * Created by Anna on 28.10.2017.
 */

public class GeneratePdf {

    private static final String TAG = GeneratePdf.class.getSimpleName();

    private Bitmap bitmap;
    private BaseFont baseFont;
    private Font headerFont;
    private Font normalFont;
    private Font tableCellFont;
    private Font tableCellMiniFont;

    private int x = 70;
    private int y = 70;
    private Document document;
    private File directory;
    private File file;
    private PdfWriter writer;
    private int pageNumber;
    private Context context;

    public GeneratePdf(Context context) {
        this.context = context;
    }

    private String getString(int i) {
        return context.getString(i);
    }

    public void init(String fileName) throws IOException, DocumentException {
        baseFont = BaseFont.createFont("assets/fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.CACHED); // BaseFont.EMBEDED
        headerFont = new Font(baseFont, 16);
        normalFont = new Font(baseFont, 11);
        tableCellFont = new Font(baseFont, 10);
        tableCellMiniFont = new Font(baseFont, 8);


        directory = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS);
        pageNumber = 1;
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

    public File save() {
        document.close();
        Log.i(TAG, "Plik " + file  + " zapisany poprawnie");
        return file;
    }

    public void createNewPage() {
        document.newPage();
    }

    private Paragraph getBasicTitleHeader() {
        Paragraph paragraph = new Paragraph(getString(R.string.report_head), headerFont);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(new Paragraph(" "));
        return paragraph;
    }

//    private Paragraph getTypeOfTransport(String type) {
//        Paragraph paragraph = new Paragraph(getString(R.string.report_transport_type) + "   " +type, normalFont);
//        return paragraph;
//    }

    private Chunk getEmptyParagraph() {
        return Chunk.NEWLINE;
    }

    private PdfPTable createTypeOfTransport(String type) {
        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(510);
        table.setLockedWidth(true);

        PdfPCell cell = new PdfPCell(new Phrase(getString(R.string.report_transport_type), normalFont));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(type, normalFont));
        table.addCell(cell);
        return table;
    }

    public PdfPTable createPersonalDataTable() {
        // a table with three columns
        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(510);
        table.setLockedWidth(true);
        // the cell object
        PdfPCell cell;
        // we add a cell with colspan 3
//        cell = new PdfPCell(new Phrase("Cell with colspan 3"));
//        cell.setColspan(3);
//        table.addCell(cell);
        // now we add a cell with rowspan 2
        cell = new PdfPCell(new Phrase(getString(R.string.report_name), normalFont));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
//        cell.setRowspan(2);
        table.addCell(cell);
        // we add the four remaining cells with addCell()
        table.addCell("");
        cell = new PdfPCell(new Phrase(getString(R.string.report_occupation), normalFont));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
        table.addCell("");
        return table;
    }

    public PdfPTable createPurposeOfTravelTable() {
        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(510);
        table.setLockedWidth(true);
        PdfPCell cell;

        cell = new PdfPCell(new Phrase(getString(R.string.report_purpose), normalFont));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
        table.addCell("");
        return table;
    }

    public PdfPTable createFeedingTable() {
        PdfPTable table = new PdfPTable(6);
        table.setTotalWidth(510);
        table.setLockedWidth(true);
        PdfPCell cell;

        cell = new PdfPCell(new Phrase(getString(R.string.report_feeding), normalFont));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(6);
        table.addCell(cell);
        //a)
        cell = new PdfPCell(new Phrase(getString(R.string.report_feedingA1), normalFont));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(6);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(getString(R.string.report_feeding2), tableCellFont));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(getString(R.string.report_feeding3), tableCellFont));
        cell.setColspan(4);
        table.addCell(cell);
        table.addCell("");
        cell = new PdfPCell(new Phrase(getString(R.string.report_feeding4), tableCellFont));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("", normalFont));
        cell.setColspan(4);
        table.addCell(cell);
        table.addCell("");
        cell = new PdfPCell(new Phrase(getString(R.string.report_feeding5), tableCellFont));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("", normalFont));
        cell.setColspan(4);
        table.addCell(cell);
        table.addCell("");
        //b)
        cell = new PdfPCell(new Phrase(getString(R.string.report_feedingB1), normalFont));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(6);
        table.addCell(cell);//nowa linia
        table.addCell("");
        table.addCell("");
        cell = new PdfPCell(new Phrase(getString(R.string.report_feeding31), tableCellMiniFont));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(getString(R.string.report_feeding32), tableCellMiniFont));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(getString(R.string.report_feeding33), tableCellMiniFont));
        table.addCell(cell);
        table.addCell("");
        cell = new PdfPCell(new Phrase(getString(R.string.report_feeding2), tableCellFont));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(getString(R.string.report_feeding3), tableCellMiniFont));
        table.addCell(cell);
        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("");
        cell = new PdfPCell(new Phrase(getString(R.string.report_feeding4), tableCellFont));
        table.addCell(cell);
        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("");
        cell = new PdfPCell(new Phrase(getString(R.string.report_feeding5), tableCellFont));
        table.addCell(cell);
        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("");
        //c)
        cell = new PdfPCell(new Phrase(getString(R.string.report_feedingC1), normalFont));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(6);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(getString(R.string.report_feeding2), tableCellFont));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(getString(R.string.report_feeding3), tableCellFont));
        cell.setColspan(4);
        table.addCell(cell);
        table.addCell("");
        cell = new PdfPCell(new Phrase(getString(R.string.report_feeding4), tableCellFont));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("", normalFont));
        cell.setColspan(4);
        table.addCell(cell);
        table.addCell("");
        cell = new PdfPCell(new Phrase(getString(R.string.report_feeding5), tableCellFont));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("", normalFont));
        cell.setColspan(4);
        table.addCell(cell);
        table.addCell("");
        return table;
    }

    private PdfPTable createAccommodation() {
        PdfPTable table = new PdfPTable(6);
        table.setTotalWidth(510);
        table.setLockedWidth(true);
        PdfPCell cell;

        cell = new PdfPCell(new Phrase(getString(R.string.report_accomodation1), normalFont));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(6);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(getString(R.string.report_accomodation2), tableCellFont));
        cell.setColspan(5);
        table.addCell(cell);
        table.addCell("");
        cell = new PdfPCell(new Phrase("    " + getString(R.string.report_accomodation4), tableCellFont));
        cell.setColspan(5);
        table.addCell(cell);
        table.addCell("");
        cell = new PdfPCell(new Phrase("    " + getString(R.string.report_accomodation3), tableCellFont));
        cell.setColspan(2);
        table.addCell(cell);
        table.addCell("");
        cell = new PdfPCell(new Phrase(getString(R.string.report_accomodation5), tableCellFont));
        cell.setColspan(1);
        table.addCell(cell);
        table.addCell("");
        table.addCell("");
        return table;
    }

    public PdfPTable createTravelCost() {
        PdfPTable table = new PdfPTable(6);
        table.setTotalWidth(510);
        table.setLockedWidth(true);
        PdfPCell cell;
        cell = new PdfPCell(new Phrase(getString(R.string.report_transport_costs1), normalFont));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(6);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(getString(R.string.report_transport_costs2), tableCellFont));
        cell.setColspan(2);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("", tableCellFont));
        cell.setColspan(3);
        table.addCell(cell);
        table.addCell("");
        cell = new PdfPCell(new Phrase("    " + getString(R.string.report_transport_costs3), tableCellFont));
        cell.setColspan(3);
        table.addCell(cell);
        table.addCell("");
        table.addCell("");
        table.addCell("");
        cell = new PdfPCell(new Phrase("    " + getString(R.string.report_transport_costs4), tableCellFont));
        cell.setColspan(3);
        table.addCell(cell);
        table.addCell("");
        table.addCell("");
        table.addCell("");
        cell = new PdfPCell(new Phrase("    " + getString(R.string.report_transport_costs5), tableCellFont));
        cell.setColspan(3);
        table.addCell(cell);
        table.addCell("");
        table.addCell("");
        table.addCell("");
        cell = new PdfPCell(new Phrase("    " + getString(R.string.report_transport_costs6), tableCellFont));
        cell.setColspan(3);
        table.addCell(cell);
        table.addCell("");
        table.addCell("");
        table.addCell("");
        cell = new PdfPCell(new Phrase(getString(R.string.report_transport_costs7), tableCellFont));
        cell.setColspan(4);
        table.addCell(cell);
        table.addCell("");
        table.addCell("");
        cell = new PdfPCell(new Phrase(getString(R.string.report_transport_costs8), tableCellFont));
        cell.setColspan(5);
        table.addCell(cell);
        table.addCell("");
        cell = new PdfPCell(new Phrase(getString(R.string.report_transport_costs9), tableCellFont));
        cell.setColspan(5);
        table.addCell(cell);
        table.addCell("");
        return table;
    }

    public PdfPTable createHospitlization() {
        PdfPTable table = new PdfPTable(6);
        table.setTotalWidth(510);
        table.setLockedWidth(true);
        PdfPCell cell;
        cell = new PdfPCell(new Phrase(getString(R.string.report_hospitalization1), normalFont));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(6);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(getString(R.string.report_hospitalization2), tableCellFont));
        cell.setColspan(5);
        table.addCell(cell);
        table.addCell("");
        return table;
    }

    public PdfPTable createAnother() {
        PdfPTable table = new PdfPTable(6);
        table.setTotalWidth(510);
        table.setLockedWidth(true);
        PdfPCell cell;
        cell = new PdfPCell(new Phrase(getString(R.string.report_anotherCosts), normalFont));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(6);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("", tableCellFont));
        cell.setColspan(5);
        table.addCell("");
        cell = new PdfPCell(new Phrase("", tableCellFont));
        cell.setColspan(5);
        table.addCell("");
        cell = new PdfPCell(new Phrase("", tableCellFont));
        cell.setColspan(5);
        table.addCell("");
        return table;
    }

    public PdfPTable createEndingSummary() {
        PdfPTable table = new PdfPTable(6);
        table.setTotalWidth(510);
        table.setLockedWidth(true);
        PdfPCell cell;
        cell = new PdfPCell(new Phrase(getString(R.string.report_sum), normalFont));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(5);
        table.addCell(cell);
        table.addCell("");
        cell = new PdfPCell(new Phrase(getString(R.string.report_sum2), normalFont));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(5);
        table.addCell(cell);
        table.addCell("");
        return table;
    }

    public Paragraph createFinalSignaturesSpace() {
        Chunk glue = new Chunk(new VerticalPositionMark());
        Paragraph p = new Paragraph("           ...............................");
        p.add(new Chunk(glue));
        p.add("............................................            ");
        return p;
    }

    public Paragraph createFinalSignaturesExplanation() {
        Chunk glue = new Chunk(new VerticalPositionMark());
        Paragraph p = new Paragraph("                     (data)");
        p.add(new Chunk(glue));
        p.add("             (podpis)                            ");
        return p;
    }

    public Paragraph createTripReportTitle() {
        Paragraph paragraph = new Paragraph(getString(R.string.report_planned_title), headerFont);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(new Paragraph(" "));
        return paragraph;
    }

    public Paragraph createPlannedInfoHeader() {
        Paragraph paragraph = new Paragraph("   " + getString(R.string.report_planned_title2),
                headerFont);
        return paragraph;
    }

    public Paragraph createPlannedInfo(PlannedRoute route, TypeOfTransport transport) {
        Paragraph paragraph = new Paragraph(getString(R.string.report_planned_route_title)
                + " " + route.getTitle(), normalFont);
        paragraph.add(new Paragraph(getString(R.string.report_planned_route_points) + " "
                + route.getPoints().size(), normalFont));
        paragraph.add(new Paragraph(getString(R.string.report_planned_route_date) + " "
                + route.getDate().toString(), normalFont));
        paragraph.add(new Paragraph(getString(R.string.report_planned_route_type) + " "
                + transport.getName() + " (" + transport.getShortName() + ")", normalFont));
        paragraph.add(new Paragraph(getString(R.string.report_planned_route), normalFont));
        return paragraph;
    }

    public PdfPTable createPlannedTable(PlannedRouteReportInfo route) {
        PdfPTable table = new PdfPTable(7);
        table.setTotalWidth(510);
        table.setLockedWidth(true);
        PdfPCell cell;
        cell = new PdfPCell(new Phrase(getString(R.string.report_planned_table1), tableCellFont));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(getString(R.string.report_planned_table2), tableCellFont));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(getString(R.string.report_planned_table3), tableCellFont));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(getString(R.string.report_planned_table4), tableCellFont));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(getString(R.string.report_planned_table5), tableCellFont));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(getString(R.string.report_planned_table6), tableCellFont));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(getString(R.string.report_planned_table7), tableCellFont));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        List<PlannedRouteReportInfo.Point> infoForReport = route.getInfoForReport();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            infoForReport.stream().forEach(info -> {
                PdfPCell cellRow;
                cellRow = new PdfPCell(new Phrase(String.valueOf(info.getNumer()), tableCellMiniFont));
                table.addCell(cellRow);
                cellRow = new PdfPCell(new Phrase(String.valueOf(info.getName()), tableCellMiniFont));
                table.addCell(cellRow);
                cellRow = new PdfPCell(new Phrase(String.valueOf(info.getStartPoint()), tableCellMiniFont));
                table.addCell(cellRow);
                cellRow = new PdfPCell(new Phrase(String.valueOf(info.getEndPoint()), tableCellMiniFont));
                table.addCell(cellRow);
                table.addCell(" "); //TODO DURATION!
                cellRow = new PdfPCell(new Phrase(String.valueOf(info.getDistance()), tableCellMiniFont));
                table.addCell(cellRow);
                table.addCell(" ");
            });
        } else {
            for(PlannedRouteReportInfo.Point info : infoForReport) {
                PdfPCell cellRow;
                cellRow = new PdfPCell(new Phrase(String.valueOf(info.getNumer()), tableCellMiniFont));
                table.addCell(cellRow);
                cellRow = new PdfPCell(new Phrase(String.valueOf(info.getName()), tableCellMiniFont));
                table.addCell(cellRow);
                cellRow = new PdfPCell(new Phrase(String.valueOf(info.getStartPoint()), tableCellMiniFont));
                table.addCell(cellRow);
                cellRow = new PdfPCell(new Phrase(String.valueOf(info.getEndPoint()), tableCellMiniFont));
                table.addCell(cellRow);
                table.addCell(" "); //TODO DURATION!
                cellRow = new PdfPCell(new Phrase(String.valueOf(info.getDistance()) + " km", tableCellMiniFont));
                table.addCell(cellRow);
                table.addCell(" ");
            }
        }

        cell = new PdfPCell(new Phrase(getString(R.string.report_planned_table_sum), tableCellFont));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(4);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(String.valueOf(route.getAllDuration()), tableCellFont));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(String.valueOf(route.getAllDistance()/1000) + " km", tableCellFont));
        table.addCell(cell);
        table.addCell(" ");

        return table;
    }

    public Paragraph createAcctuallInfoHeader() {
        Paragraph paragraph = new Paragraph("   " + getString(R.string.report_acctual_route_title),
                headerFont);
        return paragraph;
    }

    public Paragraph createAcctuallInfo(Route route) {
        Paragraph paragraph = new Paragraph();
        if (route.getStartDate() != null) {
            paragraph = new Paragraph(getString(R.string.report_acctual_route_start)
                    + " " + route.getStartDate(), normalFont);
        }
        if (route.getEndDate() != null) {
            paragraph.add(new Paragraph(getString(R.string.report_acctual_route_end) + " "
                    + route.getEndDate(), normalFont));
        }
        if (route.getLocations() != null) {
            paragraph.add(new Paragraph(getString(R.string.report_acctual_route_distance) + " "
                    + DistanceAndDurationUtil.calculateDistanceInKm(route.getLocations()) +
                    " " + getString(R.string.report_acctual_route_distance2), normalFont));
        }
        if (bitmap != null) {
            paragraph.add(new Paragraph(getString(R.string.report_acctual_route_map), normalFont));
            paragraph.add(createMapImage());
            Paragraph par = new Paragraph(getString(R.string.report_acctual_legend),
                    tableCellMiniFont);
            par.setAlignment(Paragraph.ALIGN_CENTER);
            paragraph.add(par);
        }
        return paragraph;
    }

    public Image createMapImage() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100 , stream);
        try {
            Image image = Image.getInstance(stream.toByteArray());
            image.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight() - 200);
            image.setAlignment(Image.ALIGN_CENTER);
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BadElementException e) {
            e.printStackTrace();
        }
        return null;
    }

    public File generate(PlannedRoute route, String fileName) {
        try {
            init(fileName);
//            createNewPage();
            document.add(getBasicTitleHeader());
//            document.add(getEmptyParagraph());
            document.add(createTypeOfTransport("samochód"));
            document.add(createPersonalDataTable());
            document.add(createPurposeOfTravelTable());
            document.add(createFeedingTable());
            document.add(createAccommodation());
            document.add(createTravelCost());
            document.add(createHospitlization());
            document.add(createAnother());
            document.add(createEndingSummary());
            document.add(getEmptyParagraph());
//            document.add(getEmptyParagraph());
            document.add(createFinalSignaturesSpace());
            document.add(createFinalSignaturesExplanation());
            document.newPage(); //TRASA zaplanowana
            document.add(createTripReportTitle());
            document.add(getEmptyParagraph());
            document.add(createPlannedInfoHeader());
            document.add(getEmptyParagraph());
            document.add(createPlannedInfo(route, new Car()));
            document.add(getEmptyParagraph());
            document.add(createPlannedTable(new PlannedRouteReportInfo(route)));
            document.newPage(); //TRASA ZREALIZOWANA
            document.add(createAcctuallInfoHeader());
            document.add(getEmptyParagraph());
            document.add(createAcctuallInfo(new Route()));
//            document.add(createMapImage());

            return save();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
