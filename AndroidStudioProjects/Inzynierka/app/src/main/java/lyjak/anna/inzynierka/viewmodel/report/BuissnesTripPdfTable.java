package lyjak.anna.inzynierka.viewmodel.report;

import android.content.Context;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

import java.io.IOException;

import lyjak.anna.inzynierka.R;

/**
 * Created by Anna on 25.11.2017.
 */

public class BuissnesTripPdfTable {

    private Context context;
    private Font headerFont;
    private Font normalFont;
    private Font tableCellFont;
    private Font tableCellMiniFont;

    public BuissnesTripPdfTable(Context context) throws IOException, DocumentException {
        this.context = context;
        init();
    }

    private void init() throws IOException, DocumentException {
        BaseFont baseFont = BaseFont.createFont("assets/fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.CACHED);
        headerFont = new Font(baseFont, 16);
        normalFont = new Font(baseFont, 11);
        tableCellFont = new Font(baseFont, 10);
        tableCellMiniFont = new Font(baseFont, 8);
    }

    private String getString(int i) {
        return context.getString(i);
    }

    Paragraph getBasicTitleHeader() {
        Paragraph paragraph = new Paragraph(getString(R.string.report_head), headerFont);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(new Paragraph(" "));
        return paragraph;
    }

    PdfPTable createTypeOfTransport(String type) {
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

    PdfPTable createAccommodation() {
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
        cell = new PdfPCell(new Phrase(" ", tableCellFont));
        cell.setColspan(5);
        table.addCell(cell);
        table.addCell("");
        cell = new PdfPCell(new Phrase(" ", tableCellFont));
        cell.setColspan(5);
        table.addCell(cell);
        table.addCell("");
        cell = new PdfPCell(new Phrase(" ", tableCellFont));
        cell.setColspan(5);
        table.addCell(cell);
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
}
