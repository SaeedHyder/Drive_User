package com.ingic.driveuser.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.ingic.driveuser.R;
import com.ingic.driveuser.entities.ProgressEnt;
import com.ingic.driveuser.fragments.abstracts.BaseFragment;
import com.ingic.driveuser.ui.views.AnyTextView;
import com.ingic.driveuser.ui.views.TitleBar;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by saeedhyder on 4/5/2018.
 */
public class MyRideDetailFragment extends BaseFragment {


    @BindView(R.id.iv_map)
    ImageView ivMap;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.txt_pickup)
    AnyTextView txtPickup;
    @BindView(R.id.txt_pick_text)
    AnyTextView txtPickText;
    @BindView(R.id.imageView_destination)
    ImageView imageViewDestination;
    @BindView(R.id.txt_destination)
    AnyTextView txtDestination;
    @BindView(R.id.txt_destination_text)
    AnyTextView txtDestinationText;
    @BindView(R.id.txt_duration)
    AnyTextView txtDuration;
    @BindView(R.id.txt_car_type)
    AnyTextView txtCarType;
    @BindView(R.id.txt_fare)
    AnyTextView txtFare;
    @BindView(R.id.txt_payment_type)
    AnyTextView txtPaymentType;
    @BindView(R.id.txt_captain)
    AnyTextView txtCaptain;
    @BindView(R.id.txt_car)
    AnyTextView txtCar;
    @BindView(R.id.txt_ride)
    AnyTextView txtRide;
    @BindView(R.id.txt_plate_no)
    AnyTextView txtPlateNo;
    @BindView(R.id.ll_pastTripDetail)
    LinearLayout llPastTripDetail;
    @BindView(R.id.btn_report_a_problem)
    Button btnReportAProblem;
    Unbinder unbinder;



    Picasso picasso;
    private static String rideDetailKey = "rideDetailKey";
    private ProgressEnt entity;

    public static MyRideDetailFragment newInstance() {
        Bundle args = new Bundle();
        MyRideDetailFragment fragment = new MyRideDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static MyRideDetailFragment newInstance(ProgressEnt entity) {
        Bundle args = new Bundle();
        args.putString(rideDetailKey, new Gson().toJson(entity));
        MyRideDetailFragment fragment = new MyRideDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        picasso = Picasso.with(getDockActivity());
        if (getArguments() != null) {
            rideDetailKey = getArguments().getString(rideDetailKey);
        }
        if (rideDetailKey != null) {
            entity = new Gson().fromJson(rideDetailKey, ProgressEnt.class);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ride_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setRideDetail();

    }

    private void setRideDetail() {

        picasso.load(entity.getImageUrl() == null || entity.getImageUrl().trim().equals("")
                ? "asd" : entity.getImageUrl())
                .fit().into(ivMap);

        if (entity!= null) {
            String pickup = entity.getPickupAddress();
            if (pickup.contains("null")) {
                pickup = pickup.replace("null,", "");
            }
            txtPickText.setText(pickup);
        }
        if (entity.getDestinationAddress() != null) {
            String destination = entity.getDestinationAddress();
            if (destination.contains("null")) {
                destination = destination.replace("null,", "");
            }
            txtDestinationText.setText(destination);
        }

        txtDuration.setText(entity.getTimeDuration() + " min");
        txtCarType.setText(entity.getVechicleDetail().getVehicleTypeDetail().getType() + "");
        if (!entity.getTotalAmount().equals("")) {
            Double amount = Double.parseDouble(entity.getTotalAmount());
            txtFare.setText("AED " + amount.intValue());
        }else{
            txtFare.setText("-");

        }
        txtPaymentType.setText(entity.getPaymentType() + "");
        txtCaptain.setText(entity.getDriverDetail().getFullName() + "");
        txtCar.setText(entity.getVechicleDetail().getVehicleName());
        txtPlateNo.setText(entity.getVechicleDetail().getVehicleNumber() + "");
        txtRide.setText(entity.getType()+"");

    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.my_ride));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_report_a_problem)
    public void onViewClicked() {
        getDockActivity().replaceDockableFragment(ContactUsFragment.newInstance(), "ContactUsFragment");
    }
}
