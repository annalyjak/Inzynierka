package lyjak.anna.inzynierka.viewmodel.report;

import android.content.Context;
import android.graphics.Bitmap;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import lyjak.anna.inzynierka.R;
import lyjak.anna.inzynierka.service.model.utils.DistanceAndDurationUtil;
import lyjak.anna.inzynierka.viewmodel.report.modelDTO.ActualRouteForReportDTO;

/**
 * For Report of AcctualRoute
 * Created by Anna on 25.11.2017.
 */

public class AcctualRoutePdfTable {

    private Context context;

    private BaseFont baseFont;
    private Font headerFont;
    private Font normalFont;
    private Font tableCellFont;
    private Font tableCellMiniFont;

    private Bitmap bitmap;

    public AcctualRoutePdfTable(Context context, Bitmap bitmap) {
        this.context = context;
        this.bitmap = bitmap;
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

    Paragraph createAcctuallInfoHeader() {
        Paragraph paragraph = new Paragraph("   " + getString(R.string.report_acctual_route_title),
                headerFont);
        return paragraph;
    }

    Paragraph createAcctuallInfo(ActualRouteForReportDTO route) {
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

    private Image createMapImage() {
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
