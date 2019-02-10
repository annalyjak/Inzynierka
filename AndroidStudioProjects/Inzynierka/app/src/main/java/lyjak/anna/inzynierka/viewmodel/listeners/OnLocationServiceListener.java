package lyjak.anna.inzynierka.viewmodel.listeners;

import android.view.View;

import lyjak.anna.inzynierka.view.fragments.LocationListenerFragment;

/**
 * This interface must be implemented by activities that contain {@link LocationListenerFragment}
 * fragment to allow an interaction in this fragment to be communicated
 * to the activity and potentially other fragments contained in that
 * activity.
 * Created by Anna Łyjak on 01.10.2017.
 */

public interface OnLocationServiceListener {
    void onStartLocationService(View view);
    void onStopLocationService(View view);
}
