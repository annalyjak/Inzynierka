package lyjak.anna.inzynierka.view.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lyjak.anna.inzynierka.R;
import lyjak.anna.inzynierka.databinding.FragmentAdditionalNotesBinding;
import lyjak.anna.inzynierka.view.activities.MainActivity;
import lyjak.anna.inzynierka.viewmodel.report.AdditionalFields;
import lyjak.anna.inzynierka.viewmodel.report.GenerateStandardReport;

public class AdditionalNotesFragment extends Fragment {

    private GenerateStandardReport mGenerateStandardReport;

    public AdditionalNotesFragment() {
        // Required empty public constructor
    }

    public static AdditionalNotesFragment newInstance(GenerateStandardReport mGenerateStandardReport) {
        AdditionalNotesFragment fragment = new AdditionalNotesFragment();
        fragment.mGenerateStandardReport = mGenerateStandardReport;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentAdditionalNotesBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_additional_notes, container, false);

        binding.buttonNext.setOnClickListener(v1 -> {
            AdditionalFields.AdditionalFieldsBuilder builder =
                    new AdditionalFields.AdditionalFieldsBuilder();
            AdditionalFields additionalFields = builder
                    .personalDataAboutEmployee(binding.checkBoxPersonalData.isSelected())
                    .purposeOfTravel(binding.checkBoxGoal.isSelected())
                    .accomodation(binding.checkBoxAccomodation.isSelected())
                    .feeding(binding.checkBoxFeeding.isSelected())
                    .publicTransport(binding.checkBoxTransport.isSelected())
                    .hospital(binding.checkBoxHospital.isSelected())
                    .other(binding.checkBoxOther.isSelected())
                    .build();
            mGenerateStandardReport.setAdditionalFields(additionalFields);

            if (mGenerateStandardReport.plannedRouteSelected()) {
                Fragment next = PlannedRoutesFragment.newInstance(mGenerateStandardReport);
                ((MainActivity) getActivity()).attachNewFragment(next);
            } else {
                Fragment next = ActualRoutesFragment.newInstance(mGenerateStandardReport);
                ((MainActivity) getActivity()).attachNewFragment(next);
            }
        });

        return binding.getRoot();
    }

}
