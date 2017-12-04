package lyjak.anna.inzynierka.view.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lyjak.anna.inzynierka.R;
import lyjak.anna.inzynierka.view.activities.MainActivity;
import lyjak.anna.inzynierka.view.adapters.CurrencyAdapter;
import lyjak.anna.inzynierka.databinding.FragmentCombustionBinding;
import lyjak.anna.inzynierka.viewmodel.report.Combustion;
import lyjak.anna.inzynierka.model.reports.Currency;
import lyjak.anna.inzynierka.viewmodel.report.GenerateStandardReport;

public class CombustionFragment extends Fragment {

    private final static int PLN_CURRENCY = 17;

    private GenerateStandardReport mGenerateStandardReport;

    public CombustionFragment() {
        // Required empty public constructor
    }

    public static CombustionFragment newInstance(GenerateStandardReport generateStandardReport) {
        CombustionFragment fragment = new CombustionFragment();
        fragment.mGenerateStandardReport = generateStandardReport;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentCombustionBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_combustion,
                container, false);

        final CurrencyAdapter spinnerAdapter = new CurrencyAdapter(getActivity().getApplicationContext());
        binding.spinnerCurrency.setAdapter(spinnerAdapter);
        binding.spinnerCurrency.setSelection(PLN_CURRENCY);
        binding.buttonNext.setOnClickListener(v1 -> {
            if (binding.switchCombustionAutomatic.isSelected()) {
                Combustion combustion = new Combustion();
                combustion.setTypeOfTransportConverter(mGenerateStandardReport.getTypeOfTransport());
                combustion.setBurnedFuel(Integer.parseInt(
                        binding.editTextFuel.getText().toString()));
                if (binding.editTextFuelCost.getText() != null) {
                    Combustion.mFuelCost = Double.parseDouble(
                            binding.editTextFuelCost.getText().toString());
                    Combustion.mFuelCostCurrency = (Currency)
                            binding.spinnerCurrency.getSelectedItem();
                }
                mGenerateStandardReport.setCombustion(combustion);
            }
            TimeOfTravelFragment next = TimeOfTravelFragment.newInstance(mGenerateStandardReport);
            MainActivity.attachNewFragment(next);
        });

        return binding.getRoot();
    }


}
