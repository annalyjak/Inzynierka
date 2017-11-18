package lyjak.anna.inzynierka.view.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lyjak.anna.inzynierka.R;
import lyjak.anna.inzynierka.view.activities.MainActivity;
import lyjak.anna.inzynierka.databinding.FragmentTimeOfTravelBinding;
import lyjak.anna.inzynierka.viewmodel.report.TimeTripInfo;
import lyjak.anna.inzynierka.viewmodel.report.GenerateReport;

public class TimeOfTravelFragment extends Fragment {

    private GenerateReport mGenerateReport;

    public TimeOfTravelFragment() {
        // Required empty public constructor
    }

    public static TimeOfTravelFragment newInstance(GenerateReport report) {
        TimeOfTravelFragment fragment = new TimeOfTravelFragment();
        fragment.mGenerateReport = report;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentTimeOfTravelBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_time_of_travel, container, false);

        binding.buttonNext.setOnClickListener(v -> {
            TimeTripInfo timeInfo = new TimeTripInfo();
            if (!binding.editTextHours.getText().toString().equals("")) {
                timeInfo.setBreakNumberOfHours(
                        Double.parseDouble(binding.editTextHours.getText().toString())
                );
            } else {
                timeInfo.setBreakNumberOfHours(0.0);
            }
            if (!binding.editTextDays.getText().toString().equals("")) {
                timeInfo.setTripNumberOfDays(
                        Integer.parseInt(binding.editTextDays.getText().toString()));
            } else {
                timeInfo.setTripNumberOfDays(0);
            }
            mGenerateReport.setTimeTripInfo(timeInfo);

            AdditionalNotesFragment nextFragment = AdditionalNotesFragment
                    .newInstance(mGenerateReport);
            ((MainActivity)getActivity()).attachNewFragment(nextFragment);
        });
        return binding.getRoot();
    }
}
