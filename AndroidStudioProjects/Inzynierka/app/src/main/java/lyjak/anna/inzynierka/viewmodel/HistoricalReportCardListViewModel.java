package lyjak.anna.inzynierka.viewmodel;

import android.content.Context;

import java.util.List;

import lyjak.anna.inzynierka.service.model.realm.HistoricalReports;

/**
 * Created by Anna on 02.12.2017.
 */

public class HistoricalReportCardListViewModel extends MainViewModel {

    private List<HistoricalReports> reports;

    public HistoricalReportCardListViewModel(Context context) {
        super(context);
    }

    public List<HistoricalReports> getReports() {
        reports = getReportsFromDatabase();
        return reports;
    }

    public List<HistoricalReports> getReportsFromDatabase() {
        return routeService.getAllHistoricalReports();
    }

    public HistoricalReports getHistoricalReports(int position) {
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
