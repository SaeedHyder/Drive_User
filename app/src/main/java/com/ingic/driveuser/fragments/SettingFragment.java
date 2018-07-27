package com.ingic.driveuser.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

import com.ingic.driveuser.R;
import com.ingic.driveuser.entities.ResponseWrapper;
import com.ingic.driveuser.entities.UserEnt;
import com.ingic.driveuser.fragments.abstracts.BaseFragment;
import com.ingic.driveuser.global.WebServiceConstants;
import com.ingic.driveuser.helpers.UIHelper;
import com.ingic.driveuser.helpers.UpdateToken;
import com.ingic.driveuser.ui.views.AnyTextView;
import com.ingic.driveuser.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ingic.driveuser.global.AppConstants.PUSH_ON;

/**
 * Created by saeedhyder on 6/21/2017.
 */

public class SettingFragment extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.txt_notification)
    AnyTextView txtNotification;
    @BindView(R.id.ll_notificationToggel)
    LinearLayout llNotificationToggel;
    @BindView(R.id.txt_changePassword)
    AnyTextView txtChangePassword;
    @BindView(R.id.ll_ChangePassword)
    LinearLayout llChangePassword;
    @BindView(R.id.toggle_notifications)
    ToggleButton toggleNotifications;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.radioGroupLanguage)
    RadioGroup radioGroupLanguage;
    @BindView(R.id.toggle_language)
    ToggleButton toggleLanguage;


    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toggleNotifications.setChecked(prefHelper.getUser().getPushStatus() == PUSH_ON);
        setListners();
        setCheckBox();
    }

    private void setCheckBox() {

        if (prefHelper.getUser().getUserPreference().equals("female")) {
            radioGroup.check(R.id.radio_Female);
        } else if (prefHelper.getUser().getUserPreference().equals("any")) {
            radioGroup.check(R.id.radio_Any);
        } else {
            radioGroup.check(R.id.radio_Male);
        }
    }

    private void setListners() {
        toggleNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TurnNotificationStatus(isChecked);
            }
        });
        toggleLanguage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                UIHelper.showShortToastInCenter(getDockActivity(), "Will be implemented in future version");
            }
        });
        txtChangePassword.setOnClickListener(this);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = group.findViewById(checkedId);
                int position = group.indexOfChild(radioButton);

                if (position == 0) {
                    setDriverPreference("male");
                } else if (position == 1) {
                    setDriverPreference("female");
                } else {
                    setDriverPreference("any");
                }

            }
        });

        radioGroupLanguage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                View radioButton = group.findViewById(checkedId);
                int position = group.indexOfChild(radioButton);

                if (position == 0) {
                    UIHelper.showShortToastInCenter(getDockActivity(), "Will be implemented in beta");
                } else if (position == 1) {
                    UIHelper.showShortToastInCenter(getDockActivity(), "Will be implemented in beta");
                }

            }
        });
    }

    private void setDriverPreference(final String preference) {
        loadingStarted();
        Call<ResponseWrapper> call = headerWebService.setUserPreference(preference);

        call.enqueue(new Callback<ResponseWrapper>() {
            @Override
            public void onResponse(Call<ResponseWrapper> call, Response<ResponseWrapper> response) {
                loadingFinished();
                if (response.body().getResponse().equals(WebServiceConstants.TOKEN_EXPIRE_CODE) || prefHelper.get_TOKEN().equals("")) {
                    UpdateToken token = new UpdateToken(prefHelper.getUserId(), prefHelper.getUser().getEmail(), prefHelper.getUser().getRoleId(), getDockActivity(), prefHelper);
                    token.updateTokenService();
                } else if (response.body().getResponse().equals(WebServiceConstants.SUCCESS_RESPONSE_CODE)) {

                    UserEnt entity = prefHelper.getUser();
                    entity.setUserPreference(preference);

                    prefHelper.putUser(entity);


                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper> call, Throwable t) {
                loadingFinished();
                Log.e(SettingFragment.class.getSimpleName(), t.toString());
            }
        });

    }

    public void TurnNotificationStatus(boolean isChecked) {
        loadingStarted();
        Call<ResponseWrapper<UserEnt>> call = headerWebService.ChangeNotifiationStatus(isChecked ? 1 : 0);
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

                } else {
                    UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<UserEnt>> call, Throwable t) {
                loadingFinished();
                Log.e(SettingFragment.class.getSimpleName(), t.toString());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_changePassword:
                getDockActivity().replaceDockableFragment(ChangePasswordFragment.newInstance(), ChangePasswordFragment.class.getSimpleName());

                break;
        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.setBackgroundColor(getResources().getColor(R.color.appBlackColor));
        titleBar.setSubHeading(getString(R.string.setting));
        titleBar.showMenuButton();
    }



}
