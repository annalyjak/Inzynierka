package lyjak.anna.inzynierka.view.adapters;

import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import lyjak.anna.inzynierka.R;
import lyjak.anna.inzynierka.databinding.CardHistoricalReportBinding;
import lyjak.anna.inzynierka.model.realmObjects.HistoricalReport;
import lyjak.anna.inzynierka.viewmodel.HistoricalReportCardListViewModel;

/**
 *
 * Created by Anna on 02.12.2017.
 */

public class HistoricalReportAdapter extends RecyclerView.Adapter<HistoricalReportAdapter.ViewHolder> {


    private final HistoricalReportCardListViewModel viewModel;

    public HistoricalReportAdapter(FragmentActivity activity, HistoricalReportCardListViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public HistoricalReportAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardHistoricalReportBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.card_historical_report,
                        parent, false);
        return new HistoricalReportAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final HistoricalReportAdapter.ViewHolder holder, int position) {
        HistoricalReport report = viewModel.getHistoricalReports(position);
        holder.binding.setReport(report);
    }

    @Override
    public int getItemCount() {
        return viewModel.getDatasetSize();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardHistoricalReportBinding binding;

        public ViewHolder(CardHistoricalReportBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
