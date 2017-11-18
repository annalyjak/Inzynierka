package lyjak.anna.inzynierka.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lyjak.anna.inzynierka.R;
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
    public static final String SERVICE_LOCATION_STATUS_STARTED = "STARTED";
    public static final String SERVICE_LOCATION_STATUS_STOPED = "STOPED";
    //TODO zmienić przyciski we fragmencie z nagrywaniem aktualnej trasy
    private String mParamServiceStatus = "STOPED";

    private OnLocationServiceListener mListener;

    /**
     * Fragment required empty public constructor
     */
    public LocationListenerFragment() {    }

    /**
     * Factory method to create a new instance of this fragment using the provided parameters.
     *
     * @param mParamServiceStatus Info about LocationService status (STARTED/STOPED)
     * @return A new instance of fragment LocationListenerFragment.
     */
    public static LocationListenerFragment newInstance(String mParamServiceStatus) {
        LocationListenerFragment fragment = new LocationListenerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SERVICE_STATUS, mParamServiceStatus);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamServiceStatus = getArguments().getString(ARG_SERVICE_STATUS);
        }
    }

    /**
     * Inflate the layout for this fragment
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_location_listener, container, false);
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
