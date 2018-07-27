package com.ingic.driveuser.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ingic.driveuser.R;
import com.ingic.driveuser.fragments.abstracts.BaseFragment;
import com.ingic.driveuser.ui.views.AnyTextView;
import com.ingic.driveuser.ui.views.CustomRatingBar;
import com.ingic.driveuser.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by saeedhyder on 6/29/2017.
 */

public class SubmitRatingFragment extends BaseFragment  {

    @BindView(R.id.txtDriverName)
    AnyTextView txtDriverName;
    @BindView(R.id.txtCarNo)
    AnyTextView txtCarNo;
    @BindView(R.id.txt_pick_text)
    AnyTextView txtPickText;
    @BindView(R.id.txt_destination_text)
    AnyTextView txtDestinationText;
    @BindView(R.id.txtFareAmount)
    AnyTextView txtFareAmount;
    @BindView(R.id.rbAddRating)
    CustomRatingBar rbAddRating;
    @BindView(R.id.SubmitButton)
    Button SubmitButton;
    @BindView(R.id.CircularImageSharePop)
    CircleImageView CircularImageSharePop;

    public static SubmitRatingFragment newInstance() {
        return new SubmitRatingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_submit_rating, container, false);

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setListners();
    }

    private void setListners() {

    }



    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        titleBar.setSubHeading(getString(R.string.rate));
    }



    @OnClick(R.id.SubmitButton)
    public void onViewClicked() {
        getDockActivity().popBackStackTillEntry(0);
        getMainActivity().replaceDockableFragment(HomeMapFragment.newInstance(),HomeMapFragment.class.getSimpleName());
    }
}
