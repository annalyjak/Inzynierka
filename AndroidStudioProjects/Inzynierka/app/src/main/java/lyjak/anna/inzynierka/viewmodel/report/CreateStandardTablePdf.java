package lyjak.anna.inzynierka.viewmodel.report;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPTableEvent;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import lyjak.anna.inzynierka.service.model.realm.PlannedRoute;

/**
 * Created by Anna on 29.10.2017.
 */

@Deprecated
public class CreateStandardTablePdf implements PdfPTableEvent {

    public File createPdf(PlannedRoute route, File directory, String fileName) throws DocumentException, IOException {
        // step 1
        Document document = new Document(PageSize.A4);
        File file = new File(directory, fileName);
        if(!file.exists())
        {
            file.createNewFile();
        } else {
            file.delete();
            file.createNewFile();

        }
        // step 2
        PdfWriter instance = PdfWriter.getInstance(document, new FileOutputStream(file));
        // step 3
        document.open();
        // step 4
        PdfPCell cellVal = new PdfPCell(new Paragraph("Header1"));
        document.add(cellVal);

//        PdfPTable table = getTable(route);
//        document.add(table);
        document.newPage();
        // step 5
        document.close();

        return file;
    }

    public PdfPTable getTable(PlannedRoute route) {
        PdfPTable table = new PdfPTable(new float[] { 2, 1, 2, 5, 1 });
        table.setWidthPercentage(100f);
        table.getDefaultCell().setPadding(3);
        table.getDefaultCell().setUseAscender(true);
        table.getDefaultCell().setUseDescender(true);
        table.getDefaultCell().setColspan(5);
        table.getDefaultCell().setBackgroundColor(BaseColor.RED);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(route.getTitle());
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setColspan(1);
        table.getDefaultCell().setBackgroundColor(BaseColor.ORANGE);
        for (int i = 0; i < 2; i++) {
            table.addCell("Location");
            table.addCell("Time");
            table.addCell("Run Length");
            table.addCell("Title");
            table.addCell("Year");
        }
        table.getDefaultCell().setBackgroundColor(null);
        table.setHeaderRows(3);
        table.setFooterRows(1);
        return table;
    }

    /**
     * Draws a background for every other row.
     * @see com.itextpdf.text.pdf.PdfPTableEvent#tableLayout(
     *      com.itextpdf.text.pdf.PdfPTable, float[][], float[], int, int,
     *      com.itextpdf.text.pdf.PdfContentByte[])
     */
    public void tableLayout(PdfPTable table, float[][] widths, float[] heights,
                            int headerRows, int rowStart, PdfContentByte[] canvases) {
        int columns;
        Rectangle rect;
        int footer = widths.length - table.getFooterRows();
        int header = table.getHeaderRows() - table.getFooterRows() + 1;
        for (int row = header; row < footer; row += 2) {
            columns = widths[row].length - 1;
            rect = new Rectangle(widths[row][0], heights[row],
                    widths[row][columns], heights[row + 1]);
            rect.setBackgroundColor(BaseColor.GRAY);
            rect.setBorder(Rectangle.NO_BORDER);
            canvases[PdfPTable.BASECANVAS].rectangle(rect);
        }
    }
}
