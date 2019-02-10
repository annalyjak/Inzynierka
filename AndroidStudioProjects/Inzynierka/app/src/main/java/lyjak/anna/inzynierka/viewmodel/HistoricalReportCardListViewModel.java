package lyjak.anna.inzynierka.viewmodel;

import android.content.Context;

import java.util.List;

import lyjak.anna.inzynierka.model.realmObjects.HistoricalReport;

/**
 * Created by Anna on 02.12.2017.
 */

public class HistoricalReportCardListViewModel extends MainViewModel {

    private List<HistoricalReport> reports;

    public HistoricalReportCardListViewModel(Context context) {
        super(context);
    }

    public List<HistoricalReport> getReports() {
        reports = getReportsFromDatabase();
        return reports;
    }

    public List<HistoricalReport> getReportsFromDatabase() {
        return routeRepository.getAllHistoricalReports();
    }

    public HistoricalReport getHistoricalReports(int position) {
        return reports.get(position);
    }

    public int getDatasetSize() {
        if (reports != null) {
            return reports.size();
        } else {
            return 0;
        }
    }
}
