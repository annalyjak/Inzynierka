package lyjak.anna.inzynierka.viewmodel.report;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import lyjak.anna.inzynierka.R;
import lyjak.anna.inzynierka.service.model.TypeOfTransport;
import lyjak.anna.inzynierka.viewmodel.report.modelDTO.PlannedRouteForReportDTO;

import static java.lang.String.*;

/**
 * For Report of PlannedRoute
 * Created by Anna on 25.11.2017.
 */

public class PlannedRoutePdfTable {

    private Context context;

    private BaseFont baseFont;
    private Font headerFont;
    private Font normalFont;
    private Font tableCellFont;
    private Font tableCellMiniFont;

    public PlannedRoutePdfTable(Context context) {
        this.context = context;
        init();
    }

    private void init() {
        try {
            baseFont = BaseFont.createFont(
                    "assets/fonts/arial.ttf",
                    BaseFont.IDENTITY_H,
                    BaseFont.CACHED);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        headerFont = new Font(baseFont, 16);
        normalFont = new Font(baseFont, 11);
        tableCellFont = new Font(baseFont, 10);
        tableCellMiniFont = new Font(baseFont, 8);
    }

    private String getString(int i) {
        return context.getString(i);
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

    public Paragraph createPlannedInfo(PlannedRouteForReportDTO route, TypeOfTransport transport) {
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
            infoForReport.forEach(info -> {
                PdfPCell cellRow;
                cellRow = new PdfPCell(new Phrase(valueOf(info.getNumer()), tableCellMiniFont));
                table.addCell(cellRow);
                cellRow = new PdfPCell(new Phrase(valueOf(info.getName()), tableCellMiniFont));
                table.addCell(cellRow);
                cellRow = new PdfPCell(new Phrase(valueOf(info.getStartPoint()), tableCellMiniFont));
                table.addCell(cellRow);
                cellRow = new PdfPCell(new Phrase(valueOf(info.getEndPoint()), tableCellMiniFont));
                table.addCell(cellRow);
                table.addCell(new Phrase(info.getDurationHandMin(), tableCellMiniFont));
                table.addCell(cellRow);
                cellRow = new PdfPCell(new Phrase((format("%.2f", (info.getDistance()/1000))), tableCellMiniFont));
                table.addCell(cellRow);
                table.addCell(" ");
            });
        } else {
            for(PlannedRouteReportInfo.Point info : infoForReport) {
                PdfPCell cellRow;
                cellRow = new PdfPCell(new Phrase(valueOf(info.getNumer()), tableCellMiniFont));
                table.addCell(cellRow);
                cellRow = new PdfPCell(new Phrase(valueOf(info.getName()), tableCellMiniFont));
                table.addCell(cellRow);
                cellRow = new PdfPCell(new Phrase(valueOf(info.getStartPoint()), tableCellMiniFont));
                table.addCell(cellRow);
                cellRow = new PdfPCell(new Phrase(valueOf(info.getEndPoint()), tableCellMiniFont));
                table.addCell(cellRow);
                cellRow = new PdfPCell(new Phrase(info.getDurationHandMin(), tableCellMiniFont));
                table.addCell(cellRow);
                cellRow = new PdfPCell(new Phrase(format("%.2f", (info.getDistance()/1000)) + " km", tableCellMiniFont));
                table.addCell(cellRow);
                table.addCell(" ");
            }
        }

        cell = new PdfPCell(new Phrase(getString(R.string.report_planned_table_sum), tableCellFont));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(4);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(route.getAllFormatedDuration(), tableCellFont));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(valueOf(route.getAllDistance()/1000) + " km", tableCellFont));
        table.addCell(cell);
        table.addCell(" ");

        return table;
    }

    Paragraph createBitmap(Bitmap bitmap) {
        Paragraph paragraph = new Paragraph();
        if (bitmap != null) {
            paragraph.add(new Paragraph(getString(R.string.report_acctual_route_map), normalFont));
            paragraph.add(createMapImage(bitmap));
            Paragraph par = new Paragraph(getString(R.string.report_acctual_legend),
                    tableCellMiniFont);
            par.setAlignment(Paragraph.ALIGN_CENTER);
            paragraph.add(par);
        }
        return paragraph;
    }

    private Image createMapImage(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100 , stream);
        try {
            Image image = Image.getInstance(stream.toByteArray());
            image.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight() - 200);
            image.setAlignment(Image.ALIGN_CENTER);
            return image;
        } catch (IOException | BadElementException e) {
            e.printStackTrace();
        }
        return null;
    }
}
