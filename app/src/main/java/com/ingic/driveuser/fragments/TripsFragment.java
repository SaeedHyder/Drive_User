package com.ingic.driveuser.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ingic.driveuser.R;
import com.ingic.driveuser.fragments.abstracts.BaseFragment;
import com.ingic.driveuser.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ingic.driveuser.activities.DockActivity.KEY_FRAG_FIRST;

/**
 * Created by saeedhyder on 7/3/2017.
 */

public class TripsFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.btn_past)
    Button btnPast;
    @BindView(R.id.btn_upcoming)
    Button btnUpcoming;
    @BindView(R.id.ll_buttons)
    LinearLayout llButtons;
    @BindView(R.id.mainFrame)
    LinearLayout mainFrame;

    private static String isScheduleRideKey = "isScheduleRide";
    @BindView(R.id.history_view)
    View historyView;
    @BindView(R.id.upcoming_view)
    View upcomingView;
    private boolean isScheduleKey;

    public static TripsFragment newInstance() {
        return new TripsFragment();
    }

    public static TripsFragment newInstance(boolean isScheduleRide) {
        Bundle args = new Bundle();
        args.putBoolean(isScheduleRideKey, isScheduleRide);
        TripsFragment fragment = new TripsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isScheduleKey = getArguments().getBoolean(isScheduleRideKey);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trips, container, false);

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (isScheduleKey) {

            btnPast.setTextColor(getDockActivity().getResources().getColor(R.color.upcoming_color));
            historyView.setVisibility(View.INVISIBLE);

            btnUpcoming.setTextColor(getResources().getColor(R.color.black));
            upcomingView.setVisibility(View.VISIBLE);


            ReplaceListViewFragment(UpcomingTripsFragment.newInstance());
        } else {

            ReplaceListViewFragment(PastTripsFragment.newInstance());
        }

        setListners();
    }

    private void setListners() {

        btnPast.setOnClickListener(this);
        btnUpcoming.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_past:
                btnPast.setTextColor(getDockActivity().getResources().getColor(R.color.black));
                historyView.setVisibility(View.VISIBLE);

                btnUpcoming.setTextColor(getResources().getColor(R.color.upcoming_color));
                upcomingView.setVisibility(View.INVISIBLE);

                ReplaceListViewFragment(PastTripsFragment.newInstance());

                break;

            case R.id.btn_upcoming:

                btnPast.setTextColor(getDockActivity().getResources().getColor(R.color.upcoming_color));
                historyView.setVisibility(View.INVISIBLE);

                btnUpcoming.setTextColor(getResources().getColor(R.color.black));
                upcomingView.setVisibility(View.VISIBLE);

                ReplaceListViewFragment(UpcomingTripsFragment.newInstance());

                break;


        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showMenuButton();
        titleBar.setSubHeading(getString(R.string.Your_Trips));
    }

    private void ReplaceListViewFragment(BaseFragment frag) {

        FragmentTransaction transaction = getChildFragmentManager()
                .beginTransaction();

        transaction.replace(R.id.mainFrame, frag);
        transaction
                .addToBackStack(
                        getChildFragmentManager().getBackStackEntryCount() == 0 ? KEY_FRAG_FIRST
                                : null).commit();

    }


}
