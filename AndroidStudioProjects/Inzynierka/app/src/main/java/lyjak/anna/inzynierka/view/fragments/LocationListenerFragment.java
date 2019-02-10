package lyjak.anna.inzynierka.view.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lyjak.anna.inzynierka.R;
import lyjak.anna.inzynierka.databinding.FragmentLocationBinding;
import lyjak.anna.inzynierka.databinding.FragmentLocationListenerBinding;
import lyjak.anna.inzynierka.viewmodel.MainActivityViewModel;
import lyjak.anna.inzynierka.viewmodel.listeners.OnLocationServiceListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnLocationServiceListener} interface
 * to handle interaction events.
 * Use the {@link LocationListenerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationListenerFragment extends Fragment {

    private static final String ARG_SERVICE_STATUS = "paramService";
    private FragmentLocationListenerBinding binding;

    private OnLocationServiceListener mListener;

    /**
     * Fragment required empty public constructor
     */
    public LocationListenerFragment() {    }

    /**
     * Factory method to create a new instance of this fragment using the provided parameters.
     *
     * @return A new instance of fragment LocationListenerFragment.
     */
    public static LocationListenerFragment newInstance() {
        LocationListenerFragment fragment = new LocationListenerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_location_listener, container, false);
        if (MainActivityViewModel.notyficationOn) {
            binding.buttonLocationListenerStart.setEnabled(false);
            binding.buttonLocationListenerStop.setEnabled(true);
        } else {
            binding.buttonLocationListenerStart.setEnabled(true);
            binding.buttonLocationListenerStop.setEnabled(false);
        }
        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLocationServiceListener) {
            mListener = (OnLocationServiceListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLocationServiceListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
