package com.ingic.driveuser.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ingic.driveuser.R;
import com.ingic.driveuser.fragments.abstracts.BaseFragment;

import com.google.android.gms.location.places.Place;
import com.jota.autocompletelocation.AutoCompleteLocation;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 6/22/2017.
 */

public class PickupSelectionFragment extends BaseFragment {
    public static PickupSelectionFragment newInstance() {
        return new PickupSelectionFragment();
    }
    @BindView(R.id.edt_pickup)
    AutoCompleteLocation edtPickup;
    @BindView(R.id.input_layout_pickup)
    TextInputLayout inputLayoutPickup;
    @BindView(R.id.edt_destination)
    AutoCompleteLocation edtDestination;
    @BindView(R.id.input_layout_destination)
    TextInputLayout inputLayoutDestination;
    @BindView(R.id.recent_places)
    ListView recentPlaces;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pickup_location, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListeners();
    }

    private void setListeners() {
        edtDestination.setAutoCompleteTextListener(new AutoCompleteLocation.AutoCompleteLocationListener() {
            @Override
            public void onTextClear() {

            }

            @Override
            public void onItemSelected(Place selectedPlace) {
              /*  if (origin == null) {
                    getCurrentLocation();
                }
                if (selectedPlace != null) {
                    destination = selectedPlace.getLatLng();
                    setdestinationMarkerOption(destination);
                    InitRideSelection();
                }*/


            }
        });
        edtPickup.setAutoCompleteTextListener(new AutoCompleteLocation.AutoCompleteLocationListener() {
            @Override
            public void onTextClear() {

            }

            @Override
            public void onItemSelected(Place selectedPlace) {
              /*  if (selectedPlace != null) {
                    origin = selectedPlace.getLatLng();
                    setoriginMarkerOption(origin);
                    setRoute();

                }*/

            }
        });
    }


}
