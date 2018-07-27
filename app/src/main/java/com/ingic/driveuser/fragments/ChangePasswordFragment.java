package com.ingic.driveuser.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.ingic.driveuser.R;
import com.ingic.driveuser.entities.ResponseWrapper;
import com.ingic.driveuser.entities.UserEnt;
import com.ingic.driveuser.fragments.abstracts.BaseFragment;
import com.ingic.driveuser.global.WebServiceConstants;
import com.ingic.driveuser.helpers.InternetHelper;
import com.ingic.driveuser.helpers.UIHelper;
import com.ingic.driveuser.helpers.UpdateToken;
import com.ingic.driveuser.ui.views.AnyEditTextView;
import com.ingic.driveuser.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by saeedhyder on 6/22/2017.
 */

public class ChangePasswordFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.iv_LogoIcon)
    ImageView ivLogoIcon;
    @BindView(R.id.iv_currentPasswordIcon)
    ImageView ivCurrentPasswordIcon;
    @BindView(R.id.edtcurrentPassword)
    AnyEditTextView edtcurrentPassword;
    @BindView(R.id.ll_currentPassword)
    LinearLayout llCurrentPassword;
    @BindView(R.id.iv_NewPasswordIcon)
    ImageView ivNewPasswordIcon;
    @BindView(R.id.editNewPassword)
    AnyEditTextView editNewPassword;
    @BindView(R.id.ll_NewPassword)
    LinearLayout llNewPassword;
    @BindView(R.id.iv_ConfirmPasswordIcon)
    ImageView ivConfirmPasswordIcon;
    @BindView(R.id.editConfirmPassword)
    AnyEditTextView editConfirmPassword;
    @BindView(R.id.ll_ConfirmPassword)
    LinearLayout llConfirmPassword;
    @BindView(R.id.UpdateButton)
    Button UpdateButton;
    @BindView(R.id.togglePassword)
    ToggleButton togglePassword;
    @BindView(R.id.togglePassword1)
    ToggleButton togglePassword1;
    @BindView(R.id.currentPassView)
    View currentPassView;


    private static String ForgotPasswordKey = "ForgotPasswordKey";
    private static String UserIdKey = "UserId";
    private boolean isFromForgorPass;

    public static ChangePasswordFragment newInstance() {
        return new ChangePasswordFragment();
    }

    public static ChangePasswordFragment newInstance(boolean isForgotPass,String userId) {
        Bundle args = new Bundle();
        args.putBoolean(ForgotPasswordKey, isForgotPass);
        args.putString(UserIdKey, userId);
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isFromForgorPass = getArguments().getBoolean(ForgotPasswordKey);
            UserIdKey=getArguments().getString(UserIdKey);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setListners();
        setForgotPasswordView();
    }

    private void setForgotPasswordView() {
        if (isFromForgorPass) {
            currentPassView.setVisibility(View.GONE);
            llCurrentPassword.setVisibility(View.GONE);
        }
    }

    private void setListners() {
        UpdateButton.setOnClickListener(this);
        togglePassword.setOnClickListener(this);
        togglePassword1.setOnClickListener(this);

    }

    private boolean isvalidate() {
        if (edtcurrentPassword.getText() == null || (edtcurrentPassword.getText().toString().isEmpty())) {
            edtcurrentPassword.setError(getString(R.string.enter_password));
            return false;
        } else if (editNewPassword.getText() == null || (editNewPassword.getText().toString().isEmpty())) {
            editNewPassword.setError(getString(R.string.enter_password));
            return false;
        } else if (editNewPassword.getText().toString().equals(edtcurrentPassword.getText().toString())) {
            editNewPassword.setError(getString(R.string.samePassword));
            return false;
        } else if (editNewPassword.getText().toString().length() < 8) {
            editNewPassword.setError(getString(R.string.passwordLength));
            return false;
        } else if (editConfirmPassword.getText() == null || (editConfirmPassword.getText().toString().isEmpty()) || editConfirmPassword.getText().toString().length() < 8) {
            editConfirmPassword.setError(getString(R.string.enter_conform_password));
            return false;
        } else if (!editConfirmPassword.getText().toString().equals(editNewPassword.getText().toString())) {
            editConfirmPassword.setError("confirm password does not match");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.UpdateButton:
                if (!isFromForgorPass) {
                    if (isvalidate()) {
                        if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity()))
                            ChangeUserPassword();
                    }
                } else {
                    if (isvalidateForgot()) {
                        if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity()))
                            ChangeForgotPassword();
                    }
                }
                break;

            case R.id.togglePassword:

                boolean isCheck = togglePassword.isChecked();

                if (isCheck) {
                    editNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    editNewPassword.setSelection(editNewPassword.getText().length());
                } else {
                    editNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    editNewPassword.setSelection(editNewPassword.getText().length());
                }


                break;

            case R.id.togglePassword1:

                boolean isCheck1 = togglePassword1.isChecked();

                if (isCheck1) {
                    editConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    editConfirmPassword.setSelection(editConfirmPassword.getText().length());
                } else {
                    editConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    editConfirmPassword.setSelection(editConfirmPassword.getText().length());
                }


                break;


        }
    }

    private void ChangeForgotPassword() {
        loadingStarted();
        Call<ResponseWrapper> call = webService.resetForgotPassword(UserIdKey,editNewPassword.getText().toString());

        call.enqueue(new Callback<ResponseWrapper>() {
            @Override
            public void onResponse(Call<ResponseWrapper> call, Response<ResponseWrapper> response) {
                loadingFinished();
                if (response.body().getResponse().equals(WebServiceConstants.SUCCESS_RESPONSE_CODE)) {
                    UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                    getDockActivity().replaceDockableFragment(LoginFragment.newInstance(), "LoginFragment");

                } else {
                    UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper> call, Throwable t) {
                loadingFinished();
                Log.e(VerifyNumFragment.class.getSimpleName(), t.toString());
            }
        });


    }

    private boolean isvalidateForgot() {

        if (editNewPassword.getText() == null || (editNewPassword.getText().toString().isEmpty())) {
            editNewPassword.setError(getString(R.string.enter_password));
            return false;
        } else if (editNewPassword.getText().toString().length() < 6) {
            editNewPassword.setError(getString(R.string.passwordLength));
            return false;
        } else if (editConfirmPassword.getText() == null || (editConfirmPassword.getText().toString().isEmpty())) {
            editConfirmPassword.setError(getString(R.string.enter_password));
            return false;
        } else if (editConfirmPassword.getText().toString().length() < 6) {
            editNewPassword.setError(getString(R.string.passwordLength));
            return false;
        } else if (!editConfirmPassword.getText().toString().equals(editNewPassword.getText().toString())) {
            editConfirmPassword.setError(getString(R.string.conform_password_error));
            return false;
        } else {
            return true;
        }
    }

    public void ChangeUserPassword() {
        loadingStarted();
        Call<ResponseWrapper<UserEnt>> call = headerWebService.ChangePassword(edtcurrentPassword.getText().toString(),
                editNewPassword.getText().toString(),
                editConfirmPassword.getText().toString());
        call.enqueue(new Callback<ResponseWrapper<UserEnt>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<UserEnt>> call, Response<ResponseWrapper<UserEnt>> response) {
                loadingFinished();
                if (response.body().getResponse().equals(WebServiceConstants.TOKEN_EXPIRE_CODE) || prefHelper.get_TOKEN().equals("")) {
                    UpdateToken token = new UpdateToken(prefHelper.getUserId(), prefHelper.getUser().getEmail(), prefHelper.getUser().getRoleId(), getDockActivity(), prefHelper);
                    token.updateTokenService();
                } else if (response.body().getResponse().equals(WebServiceConstants.SUCCESS_RESPONSE_CODE)) {
                    prefHelper.putUser(response.body().getResult());
                    prefHelper.setUsrId(response.body().getResult().getId() + "");
                    UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                    UIHelper.hideSoftKeyboard(getDockActivity(),getView());
                    UIHelper.hideSoftKeyboard(getDockActivity(),editNewPassword);
                    UIHelper.hideSoftKeyboard(getDockActivity(),editConfirmPassword);
                    UIHelper.hideSoftKeyboard(getDockActivity(),edtcurrentPassword);
                    getDockActivity().replaceDockableFragment(SettingFragment.newInstance(), SettingFragment.class.getSimpleName());
                } else {
                    UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<UserEnt>> call, Throwable t) {
                loadingFinished();
                Log.e(LoginFragment.class.getSimpleName(), t.toString());
            }
        });
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.setBackgroundColor(getResources().getColor(R.color.transparent));
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.Change_Password));
    }


}
