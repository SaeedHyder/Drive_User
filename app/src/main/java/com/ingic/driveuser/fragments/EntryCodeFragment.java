package com.ingic.driveuser.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ingic.driveuser.R;
import com.ingic.driveuser.entities.ResponseWrapper;
import com.ingic.driveuser.entities.UserEnt;
import com.ingic.driveuser.entities.VerifyForgotPassEnt;
import com.ingic.driveuser.fragments.abstracts.BaseFragment;
import com.ingic.driveuser.global.AppConstants;
import com.ingic.driveuser.global.WebServiceConstants;
import com.ingic.driveuser.helpers.InternetHelper;
import com.ingic.driveuser.helpers.TokenUpdater;
import com.ingic.driveuser.helpers.UIHelper;
import com.ingic.driveuser.ui.views.AnyTextView;
import com.ingic.driveuser.ui.views.PinEntryEditText;
import com.ingic.driveuser.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by saeedhyder on 6/20/2017.
 */

public class EntryCodeFragment extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.txtverificaiton)
    AnyTextView txtverificaiton;
    @BindView(R.id.txtverify_message)
    AnyTextView txtverifyMessage;
    @BindView(R.id.txt_pin_entry)
    PinEntryEditText txtPinEntry;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.txt_reset_code)
    AnyTextView txtResetCode;

    private static String ForgotPasswordKey = "ForgotPasswordKey";
    private static String EmailKey = "EmailKey";
    private boolean isFromForgorPass;

    public static EntryCodeFragment newInstance() {
        return new EntryCodeFragment();
    }

    public static EntryCodeFragment newInstance(boolean isForgotPass,String email) {
        Bundle args = new Bundle();
        args.putBoolean(ForgotPasswordKey, isForgotPass);
        args.putString(EmailKey, email);
        EntryCodeFragment fragment = new EntryCodeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isFromForgorPass = getArguments().getBoolean(ForgotPasswordKey);
            EmailKey = getArguments().getString(EmailKey);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entry_code, container, false);

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setListners();
    }

    private void setListners() {
        btnSubmit.setOnClickListener(this);
        txtResetCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_reset_code:
                if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                    if (!isFromForgorPass) {
                        resendCode();
                    } else {
                       resendCodeForgotPassword();
                    }

                }
                break;
            case R.id.btn_submit:
                if (validater())
                    if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                        if (!isFromForgorPass) {
                            sendEntryCodeToServer();
                        } else {
                            sendEntryCodeForgotPassword();
                        }
                    }

                break;
        }
    }

    private void resendCodeForgotPassword() {
        loadingStarted();
        Call<ResponseWrapper> call = webService.resendForgotPassword(EmailKey);

        call.enqueue(new Callback<ResponseWrapper>() {
            @Override
            public void onResponse(Call<ResponseWrapper> call, Response<ResponseWrapper> response) {
                loadingFinished();
                UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
            }

            @Override
            public void onFailure(Call<ResponseWrapper> call, Throwable t) {
                loadingFinished();
                Log.e("EntryCodeFragment", t.toString());
            }
        });
    }


    private void resendCode() {
        loadingStarted();
        Call<ResponseWrapper<UserEnt>> call = webService.VerifyNumber(prefHelper.getUserId(), prefHelper.getUser().getPhoneNo());
        call.enqueue(new Callback<ResponseWrapper<UserEnt>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<UserEnt>> call, Response<ResponseWrapper<UserEnt>> response) {
                loadingFinished();
               /* if(response.body().getResponse().equals(WebServiceConstants.TOKEN_EXPIRE_CODE) ||  prefHelper.get_TOKEN().equals("")){
                    UpdateToken token=new UpdateToken(prefHelper.getUserId(),prefHelper.getUser().getEmail(),prefHelper.getUser().getRoleId(),getDockActivity(),prefHelper);
                    token.updateTokenService();
                }
              else */
                if (response.body().getResponse().equals(WebServiceConstants.SUCCESS_RESPONSE_CODE)) {
                    prefHelper.putUser(response.body().getResult());
                    prefHelper.setUsrId(response.body().getResult().getId() + "");

                    UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                } else {
                    UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<UserEnt>> call, Throwable t) {
                loadingFinished();
                Log.e(VerifyNumFragment.class.getSimpleName(), t.toString());
            }
        });

    }

    public void sendEntryCodeToServer() {
        loadingStarted();
        Call<ResponseWrapper<UserEnt>> call = webService.VerifyCode(prefHelper.getUserId(), txtPinEntry.getText().toString());
        call.enqueue(new Callback<ResponseWrapper<UserEnt>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<UserEnt>> call, Response<ResponseWrapper<UserEnt>> response) {
                loadingFinished();
               /* if(response.body().getResponse().equals(WebServiceConstants.TOKEN_EXPIRE_CODE) ||  prefHelper.get_TOKEN().equals("")){
                    UpdateToken token=new UpdateToken(prefHelper.getUserId(),prefHelper.getUser().getEmail(),prefHelper.getUser().getRoleId(),getDockActivity(),prefHelper);
                    token.updateTokenService();
                }
               else*/
                if (response.body().getResponse().equals(WebServiceConstants.SUCCESS_RESPONSE_CODE)) {
                    prefHelper.putUser(response.body().getResult());
                    prefHelper.setUsrId(response.body().getResult().getId() + "");
                    prefHelper.set_TOKEN(response.body().getResult().getToken());
                    prefHelper.setLoginStatus(true);
                    TokenUpdater.getInstance().UpdateToken(getDockActivity(),
                            AppConstants.Device_Type,
                            prefHelper.getFirebase_TOKEN());
                    getDockActivity().popBackStackTillEntry(0);
                    UIHelper.hideSoftKeyboard(getDockActivity(),getView());
                    UIHelper.hideSoftKeyboard(getDockActivity(),txtPinEntry);
                    getDockActivity().replaceDockableFragment(HomeMapFragment.newInstance(), HomeFragment.class.getSimpleName());
                } else {
                    UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<UserEnt>> call, Throwable t) {
                loadingFinished();
                Log.e(VerifyNumFragment.class.getSimpleName(), t.toString());
            }
        });
    }

    private void sendEntryCodeForgotPassword() {

        loadingStarted();
        Call<ResponseWrapper<VerifyForgotPassEnt>> call = webService.verifyForgotPassword(txtPinEntry.getText().toString());

        call.enqueue(new Callback<ResponseWrapper<VerifyForgotPassEnt>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<VerifyForgotPassEnt>> call, Response<ResponseWrapper<VerifyForgotPassEnt>> response) {
                loadingFinished();
                if (response.body().getResponse().equals(WebServiceConstants.SUCCESS_RESPONSE_CODE)) {
                    getDockActivity().replaceDockableFragment(ChangePasswordFragment.newInstance(true,response.body().getResult().getId()+""), "ChangePasswordFragment");

                } else {
                    UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<VerifyForgotPassEnt>> call, Throwable t) {
                loadingFinished();
                Log.e("EntryCodeFragment", t.toString());
            }
        });


    }

    private boolean validater() {
        if (txtPinEntry.getText().toString().trim().equals("") || txtPinEntry.getText().toString().length() > 5) {
            UIHelper.showShortToastInCenter(getDockActivity(), getResources().getString(R.string.entrycode_error));
            return false;
        } else
            return true;
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.verification_code));
    }
}
