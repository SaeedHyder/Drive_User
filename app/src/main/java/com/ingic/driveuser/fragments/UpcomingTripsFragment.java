package com.ingic.driveuser.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ingic.driveuser.R;
import com.ingic.driveuser.entities.ProgressEnt;
import com.ingic.driveuser.entities.ResponseWrapper;
import com.ingic.driveuser.entities.RideTrackEnt;
import com.ingic.driveuser.fragments.abstracts.BaseFragment;
import com.ingic.driveuser.global.WebServiceConstants;
import com.ingic.driveuser.helpers.DateHelper;
import com.ingic.driveuser.helpers.InternetHelper;
import com.ingic.driveuser.helpers.UIHelper;
import com.ingic.driveuser.helpers.UpdateToken;
import com.ingic.driveuser.interfaces.OnItemClickListner;
import com.ingic.driveuser.ui.adapters.ArrayListAdapter;
import com.ingic.driveuser.ui.viewbinder.UpcomingTripsBinder;
import com.ingic.driveuser.ui.views.AnyTextView;
import com.ingic.driveuser.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by saeedhyder on 7/3/2017.
 */

public class UpcomingTripsFragment extends BaseFragment implements View.OnClickListener, OnItemClickListner {


    @BindView(R.id.txt_no_data)
    AnyTextView txtNoData;
    @BindView(R.id.UpcomingRides_ListView)
    ListView UpcomingRidesListView;

    private ArrayListAdapter<ProgressEnt> adapter;

    private ArrayList<ProgressEnt> userCollection = new ArrayList<>();

    public static UpcomingTripsFragment newInstance() {
        return new UpcomingTripsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new ArrayListAdapter<ProgressEnt>(getDockActivity(), new UpcomingTripsBinder(getDockActivity(), this));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upcoming_trips, container, false);

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity()))
            getUpcomingTrips();

        setListners();

    }

    private void getUpcomingTrips() {
        getDockActivity().onLoadingStarted();
        Call<ResponseWrapper<ArrayList<ProgressEnt>>> call = headerWebService.getUserRideInProgress();
        call.enqueue(new Callback<ResponseWrapper<ArrayList<ProgressEnt>>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<ArrayList<ProgressEnt>>> call, Response<ResponseWrapper<ArrayList<ProgressEnt>>> response) {
                getDockActivity().onLoadingFinished();
                if (response.body().getResponse().equals(WebServiceConstants.TOKEN_EXPIRE_CODE) || prefHelper.get_TOKEN().equals("")) {
                    UpdateToken token = new UpdateToken(prefHelper.getUserId(), prefHelper.getUser().getEmail(), prefHelper.getUser().getRoleId(), getDockActivity(), prefHelper);
                    token.updateTokenService();
                } else if (response.body().getResponse().equals(WebServiceConstants.SUCCESS_RESPONSE_CODE)) {
                    getUpcomingTripsData(response.body().getResult());
                } else {
                    UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<ArrayList<ProgressEnt>>> call, Throwable t) {
                getDockActivity().onLoadingFinished();
                Log.e(UpcomingTripsFragment.class.getSimpleName(), t.toString());
            }
        });
    }

    private void getUpcomingTripsData(ArrayList<ProgressEnt> result) {

        if (result.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
            UpcomingRidesListView.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.GONE);
            UpcomingRidesListView.setVisibility(View.VISIBLE);
        }

        userCollection = new ArrayList<>();
        /*userCollection.add(new UpcomingTripsEnt("055082595", "AED 15.00", "Wed, June 15 at 4:30 Am - 4:45 Am", "Business", "drawable://" + R.drawable.trip));
        userCollection.add(new UpcomingTripsEnt("055082595", "AED 15.00", "Wed, June 15 at 4:30 Am - 4:45 Am", "Business", "drawable://" + R.drawable.trip));
        userCollection.add(new UpcomingTripsEnt("055082595", "AED 15.00", "Wed, June 15 at 4:30 Am - 4:45 Am", "Business", "drawable://" + R.drawable.trip));
*/
        userCollection.addAll(result);
        bindData(userCollection);
    }

    private void bindData(ArrayList<ProgressEnt> userCollection) {

        adapter.clearList();
        if (UpcomingRidesListView != null)
            UpcomingRidesListView.setAdapter(adapter);
        adapter.addAll(userCollection);
        adapter.notifyDataSetChanged();
    }

    private void setListners() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.Your_Trips));
    }


    @Override
    public void onClick(ProgressEnt entity) {

        if (DateHelper.timeComparision(entity.getTime(), entity.getDate())) {
            UIHelper.showShortToastInCenter(getDockActivity(), "Your ride is not started");
        } else {

            getDockActivity().onLoadingStarted();
            Call<ResponseWrapper<RideTrackEnt>> call = headerWebService.rideTracking(entity.getId() + "");
            call.enqueue(new Callback<ResponseWrapper<RideTrackEnt>>() {
                @Override
                public void onResponse(Call<ResponseWrapper<RideTrackEnt>> call, Response<ResponseWrapper<RideTrackEnt>> response) {
                    getDockActivity().onLoadingFinished();
                    if (response.body().getResponse().equals(WebServiceConstants.TOKEN_EXPIRE_CODE) || prefHelper.get_TOKEN().equals("")) {
                        UpdateToken token = new UpdateToken(prefHelper.getUserId(), prefHelper.getUser().getEmail(), prefHelper.getUser().getRoleId(), getDockActivity(), prefHelper);
                        token.updateTokenService();
                    } else if (response.body().getResponse().equals(WebServiceConstants.SUCCESS_RESPONSE_CODE)) {
                        getDockActivity().replaceDockableFragment(HomeMapFragment.newInstance(response.body().getResult()));

                    } else {
                        UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ResponseWrapper<RideTrackEnt>> call, Throwable t) {
                    getDockActivity().onLoadingFinished();
                    Log.e(UpcomingTripsFragment.class.getSimpleName(), t.toString());
                }
            });
        }
    }
}
