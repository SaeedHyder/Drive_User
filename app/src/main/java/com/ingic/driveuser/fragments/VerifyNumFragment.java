package com.ingic.driveuser.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ingic.driveuser.R;
import com.ingic.driveuser.entities.ResponseWrapper;
import com.ingic.driveuser.entities.UserEnt;
import com.ingic.driveuser.fragments.abstracts.BaseFragment;
import com.ingic.driveuser.global.WebServiceConstants;
import com.ingic.driveuser.helpers.InternetHelper;
import com.ingic.driveuser.helpers.UIHelper;
import com.ingic.driveuser.helpers.UpdateToken;
import com.ingic.driveuser.ui.views.AnyEditTextView;
import com.ingic.driveuser.ui.views.AnyTextView;
import com.ingic.driveuser.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by saeedhyder on 6/20/2017.
 */

public class VerifyNumFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.iv_LogoIcon)
    ImageView ivLogoIcon;
    @BindView(R.id.iv_emailIcon)
    ImageView ivEmailIcon;
    @BindView(R.id.edtphone)
    AnyEditTextView edtphone;
    @BindView(R.id.ll_email)
    LinearLayout llEmail;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.txtResetPass)
    AnyTextView txtResetPass;

    private static String PhoneKey="PhoneKey";
    String phoneNumber;

    public static VerifyNumFragment newInstance() {

        return new VerifyNumFragment();
    }


    public static VerifyNumFragment newInstance(String phone) {
        Bundle arg=new Bundle();
        arg.putString(PhoneKey,phone);
        VerifyNumFragment fragment=new VerifyNumFragment();
        fragment.setArguments(arg);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            phoneNumber=getArguments().getString(PhoneKey);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_verify_number, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       // edtphone.setText(getDockActivity().getCountryCode());
        if(phoneNumber!=null){
            edtphone.setText(phoneNumber);
        }
        setListners();



    }

    private void setListners() {
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getResources().getString(R.string.verufy_number));

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_submit:
                UIHelper.hideSoftKeyboard(getDockActivity(),edtphone);
                if (validated())
                    if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                        setupVerifyNumber();
                    }

                break;

        }

    }

    public void setupVerifyNumber() {
        loadingStarted();
        Call<ResponseWrapper<UserEnt>> call = webService.VerifyNumber(prefHelper.getUserId(),edtphone.getText().toString());
        call.enqueue(new Callback<ResponseWrapper<UserEnt>>() {
        @Override
        public void onResponse(Call<ResponseWrapper<UserEnt>> call, Response<ResponseWrapper<UserEnt>> response) {
            loadingFinished();
           /* if(response.body().getResponse().equals(WebServiceConstants.TOKEN_EXPIRE_CODE)){
                UpdateToken token=new UpdateToken(prefHelper.getUserId(),prefHelper.getUser().getEmail(),prefHelper.getUser().getRoleId(),getDockActivity(),prefHelper);
                token.updateTokenService();
            }
            else*/ if (response.body().getResponse().equals(WebServiceConstants.SUCCESS_RESPONSE_CODE)) {
                UIHelper.hideSoftKeyboard(getDockActivity(),getView());
                UIHelper.hideSoftKeyboard(getDockActivity(),edtphone);
                prefHelper.putUser(response.body().getResult());
                prefHelper.setUsrId(response.body().getResult().getId()+"");
                getDockActivity().replaceDockableFragment(EntryCodeFragment.newInstance(), "EntryCodeFragment");
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

    private boolean validated() {
        if (edtphone.getText().toString().isEmpty()) {
            if (edtphone.requestFocus()) {
                getMainActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
            edtphone.setError(getString(R.string.enter_phone));
            return false;
        } else if (edtphone.getText().toString().length() < 11) {
            if (edtphone.requestFocus()) {
                getMainActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
            edtphone.setError(getString(R.string.numberLength));
            return false;
        } else
            return true;
    }
}
