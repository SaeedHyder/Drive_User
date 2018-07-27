package com.ingic.driveuser.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ingic.driveuser.R;
import com.ingic.driveuser.fragments.abstracts.BaseFragment;
import com.ingic.driveuser.global.AppConstants;
import com.ingic.driveuser.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by saeedhyder on 6/22/2017.
 */

public class PaymentMethodFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.iv_selectCOD)
    ImageView ivSelectCOD;
    @BindView(R.id.ll_cashOnDelivery)
    LinearLayout llCashOnDelivery;
    @BindView(R.id.iv_selectCreditCard)
    ImageView ivSelectCreditCard;
    @BindView(R.id.ll_creditCard)
    LinearLayout llCreditCard;

    private static String isbottomSheet = "isbottomSheet";
    private String isFromBottomSheet="";

    public static PaymentMethodFragment newInstance() {
        return new PaymentMethodFragment();
    }

    public static PaymentMethodFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString(isbottomSheet, type);
        PaymentMethodFragment fragment = new PaymentMethodFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isFromBottomSheet = getArguments().getString(isbottomSheet);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_methods, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (isFromBottomSheet.equals(AppConstants.bottomsheet)) {
            prefHelper.isFromBottomSheet(true);
            if (prefHelper.getPaymentType().equals(AppConstants.cash)) {
                ivSelectCOD.setVisibility(View.VISIBLE);
                ivSelectCreditCard.setVisibility(View.GONE);
            } else {
                ivSelectCOD.setVisibility(View.GONE);
                ivSelectCreditCard.setVisibility(View.VISIBLE);
            }
        } else if (isFromBottomSheet.equals(AppConstants.bottomsheetLater)) {
            prefHelper.isFromBottomSheetLater(true);
            if (prefHelper.getPaymentType().equals(AppConstants.cash)) {
                ivSelectCOD.setVisibility(View.VISIBLE);
                ivSelectCreditCard.setVisibility(View.GONE);
            } else {
                ivSelectCOD.setVisibility(View.GONE);
                ivSelectCreditCard.setVisibility(View.VISIBLE);
            }

        }
        setListners();
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
        titleBar.setBackgroundColor(getResources().getColor(R.color.transparent));
        titleBar.showBackButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFromBottomSheet.equals(AppConstants.bottomsheet)) {
                    getMainActivity().popFragment();
                } else if (isFromBottomSheet.equals(AppConstants.bottomsheetLater)) {
                    getMainActivity().popFragment();
                } else {
                   // getDockActivity().popBackStackTillEntry(0);
                   // getDockActivity().replaceDockableFragment(HomeMapFragment.newInstance(), "HomeMapFragment");
                    getMainActivity().popFragment();
                }

            }
        });
        titleBar.setSubHeading(getResources().getString(R.string.payment_method));
    }


    @OnClick({R.id.ll_cashOnDelivery, R.id.ll_creditCard})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_cashOnDelivery:
                if (isFromBottomSheet.equals(AppConstants.bottomsheet)) {
                    prefHelper.setPaymentType(AppConstants.cash);
                    ivSelectCOD.setVisibility(View.VISIBLE);
                    ivSelectCreditCard.setVisibility(View.GONE);

                } else if (isFromBottomSheet.equals(AppConstants.bottomsheetLater)) {
                    prefHelper.setPaymentType(AppConstants.cash);
                    ivSelectCOD.setVisibility(View.VISIBLE);
                    ivSelectCreditCard.setVisibility(View.GONE);

                } else {
                    ivSelectCOD.setVisibility(View.VISIBLE);
                    ivSelectCreditCard.setVisibility(View.GONE);
                }
                break;
            case R.id.ll_creditCard:
                if (isFromBottomSheet.equals(AppConstants.bottomsheet)) {
                    prefHelper.setPaymentType(AppConstants.creditCard);
                    ivSelectCOD.setVisibility(View.GONE);
                    ivSelectCreditCard.setVisibility(View.VISIBLE);

                } else if (isFromBottomSheet.equals(AppConstants.bottomsheetLater)) {
                    prefHelper.setPaymentType(AppConstants.creditCard);
                    ivSelectCOD.setVisibility(View.GONE);
                    ivSelectCreditCard.setVisibility(View.VISIBLE);

                } else {
                    ivSelectCOD.setVisibility(View.GONE);
                    ivSelectCreditCard.setVisibility(View.VISIBLE);
                    getDockActivity().replaceDockableFragment(CreditCardDetailFragment.newInstance(), CreditCardDetailFragment.class.getSimpleName());

                }
                break;
        }
    }
}
