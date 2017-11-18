package lyjak.anna.inzynierka.view.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lyjak.anna.inzynierka.R;
import lyjak.anna.inzynierka.view.activities.MainActivity;
import lyjak.anna.inzynierka.databinding.DialogAddingRouteToReportBinding;
import lyjak.anna.inzynierka.databinding.FragmentAdditionalNotesBinding;
import lyjak.anna.inzynierka.viewmodel.report.AdditionalFields;
import lyjak.anna.inzynierka.viewmodel.report.GenerateReport;

public class AdditionalNotesFragment extends Fragment {

    private GenerateReport mGenerateReport;

    public AdditionalNotesFragment() {
        // Required empty public constructor
    }

    public static AdditionalNotesFragment newInstance(GenerateReport mGenerateReport) {
        AdditionalNotesFragment fragment = new AdditionalNotesFragment();
        fragment.mGenerateReport = mGenerateReport;
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

            final Dialog infoDialog = new Dialog(getActivity(),
                    R.style.SettingsDialogStyle);
//            infoDialog.setContentView(R.layout.dialog_adding_route_to_report);
//            TextView textView = (TextView) infoDialog.findViewById(R.id.textViewQuestion);
//            Button yesButton = (Button) infoDialog.findViewById(R.id.buttonYes);

            LayoutInflater layoutInflater = LayoutInflater.from(getActivity().getApplicationContext());
            DialogAddingRouteToReportBinding viewDataBinding = DataBindingUtil
                    .inflate(layoutInflater,
                            R.layout.dialog_adding_route_to_report,
                            null, false);

//                if (mGenerateReport.plannedRouteSelected()) {
//                    textView.setText("Czy chcesz dodać porównanie z rzeczywiście przebytą trasą?");
//                    yesButton.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            infoDialog.dismiss();
//                            // TODO otwórz listę rzeczywistych tras
//
//                            Fragment next = PlannedRoutesFragment.newInstance(mGenerateReport);
//                            ((MainActivity)getActivity()).attachNewFragment(next);
//                        }
//                    });
//                } else {
//                    textView.setText("Czy chcesz dodać porównanie z zaplanowaną trasą?");
//                    yesButton.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            infoDialog.dismiss();
//                            Fragment next = PlannedRoutesFragment.newInstance(mGenerateReport);
//                            ((MainActivity)getActivity()).attachNewFragment(next);
//                        }
//                    });
//                }
            viewDataBinding.buttonYes.setOnClickListener(v11 -> {
                infoDialog.dismiss();
                // TODO otwórz listę rzeczywistych tras
                Fragment next = PlannedRoutesFragment.newInstance(mGenerateReport);
                ((MainActivity)getActivity()).attachNewFragment(next);
            });
            viewDataBinding.buttonNo.setOnClickListener(v112 -> {
                infoDialog.dismiss();
                ProgressDialog progressDialog = ProgressDialog.show(getActivity(),
                        "Please wait ...",  "Task in progress ...", true);
                progressDialog.setCancelable(true);
                new Thread(() -> {
                    try {
                        //TODO generuj raport
                        Thread.sleep(100000);
                    } catch (Exception e) {
                        Log.e("error: ", e.getMessage());
                    }
                    progressDialog.dismiss();
                }).start();
            });
            infoDialog.setContentView(viewDataBinding.getRoot());
            infoDialog.show();
        });

        return binding.getRoot();
    }

}
