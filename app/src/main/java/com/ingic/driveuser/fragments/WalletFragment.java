package com.ingic.driveuser.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.ingic.driveuser.R;
import com.ingic.driveuser.entities.ResponseWrapper;
import com.ingic.driveuser.entities.WalletInfoEnt;
import com.ingic.driveuser.fragments.abstracts.BaseFragment;
import com.ingic.driveuser.global.WebServiceConstants;
import com.ingic.driveuser.helpers.UIHelper;
import com.ingic.driveuser.helpers.UpdateToken;
import com.ingic.driveuser.ui.views.AnyTextView;
import com.ingic.driveuser.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by saeedhyder on 4/5/2018.
 */
public class WalletFragment extends BaseFragment {
    @BindView(R.id.txt_wallet_credit)
    AnyTextView txtWalletCredit;
    @BindView(R.id.toggle_useWallet)
    ToggleButton toggleUseWallet;
    @BindView(R.id.ll_userCreditFirst)
    LinearLayout llUserCreditFirst;
    @BindView(R.id.ll_payment_method)
    LinearLayout llPaymentMethod;
    Unbinder unbinder;
    View mainFrame;

    public static WalletFragment newInstance() {
        Bundle args = new Bundle();

        WalletFragment fragment = new WalletFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainFrame = view;
        mainFrame.setVisibility(View.GONE);
        getWalletInfo();

        setListners();

    }

    private void getWalletInfo() {

        getDockActivity().onLoadingStarted();
        Call<ResponseWrapper<WalletInfoEnt>> call = headerWebService.getWalletInfo();
        call.enqueue(new Callback<ResponseWrapper<WalletInfoEnt>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<WalletInfoEnt>> call, Response<ResponseWrapper<WalletInfoEnt>> response) {
                getDockActivity().onLoadingFinished();
                if (response.body().getResponse().equals(WebServiceConstants.TOKEN_EXPIRE_CODE) || prefHelper.get_TOKEN().equals("")) {
                    UpdateToken token = new UpdateToken(prefHelper.getUserId(), prefHelper.getUser().getEmail(), prefHelper.getUser().getRoleId(), getDockActivity(), prefHelper);
                    token.updateTokenService();
                } else if (response.body().getResponse().equals(WebServiceConstants.SUCCESS_RESPONSE_CODE)) {
                    setWalletData(response.body().getResult());
                } else {
                    UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<WalletInfoEnt>> call, Throwable t) {
                getDockActivity().onLoadingFinished();
                Log.e(WalletFragment.class.getSimpleName(), t.toString());
            }
        });
    }

    private void setWalletData(WalletInfoEnt result) {

        mainFrame.setVisibility(View.VISIBLE);

        if (txtWalletCredit != null) {
            if (result.getWalletAmount() != null && !result.getWalletAmount().equals("")) {
                Double amount = Double.parseDouble(result.getWalletAmount());
                txtWalletCredit.setText("AED " + (int) Math.ceil(amount));
            } else {
                txtWalletCredit.setText("-");

            }
        }
        if (result.getWalletStatus().equals("0")) {
            toggleUseWallet.setChecked(false);

        } else {
            toggleUseWallet.setChecked(true);
        }
    }

    private void setListners() {

        toggleUseWallet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getDockActivity().onLoadingStarted();
                Call<ResponseWrapper> call = headerWebService.useWallet(isChecked ? 1 : 0);
                call.enqueue(new Callback<ResponseWrapper>() {
                    @Override
                    public void onResponse(Call<ResponseWrapper> call, Response<ResponseWrapper> response) {
                        getDockActivity().onLoadingFinished();
                        if (response.body().getResponse().equals(WebServiceConstants.TOKEN_EXPIRE_CODE) || prefHelper.get_TOKEN().equals("")) {
                            UpdateToken token = new UpdateToken(prefHelper.getUserId(), prefHelper.getUser().getEmail(), prefHelper.getUser().getRoleId(), getDockActivity(), prefHelper);
                            token.updateTokenService();
                        } else if (response.body().getResponse().equals(WebServiceConstants.SUCCESS_RESPONSE_CODE)) {

                        } else {
                            UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseWrapper> call, Throwable t) {
                        getDockActivity().onLoadingFinished();
                        Log.e(LoginFragment.class.getSimpleName(), t.toString());
                    }
                });


            }
        });
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showMenuButton();
        titleBar.setSubHeading(getString(R.string.wallet));
    }


    @OnClick(R.id.ll_payment_method)
    public void onViewClicked() {
        getDockActivity().replaceDockableFragment(PaymentMethodFragment.newInstance(), "PaymentMethodFragment");
    }
}
