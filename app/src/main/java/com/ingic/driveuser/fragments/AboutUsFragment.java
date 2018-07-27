package com.ingic.driveuser.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ingic.driveuser.R;
import com.ingic.driveuser.entities.CMSEnt;
import com.ingic.driveuser.entities.ResponseWrapper;
import com.ingic.driveuser.fragments.abstracts.BaseFragment;
import com.ingic.driveuser.global.AppConstants;
import com.ingic.driveuser.global.WebServiceConstants;
import com.ingic.driveuser.helpers.InternetHelper;
import com.ingic.driveuser.helpers.UIHelper;
import com.ingic.driveuser.helpers.UpdateToken;
import com.ingic.driveuser.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by saeedhyder on 6/23/2017.
 */

public class AboutUsFragment extends BaseFragment {

    @BindView(R.id.txt_term_condition)
    TextView txtTermCondition;

    public static AboutUsFragment newInstance() {
        return new AboutUsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showMenuButton();
        titleBar.setSubHeading(getString(R.string.about_us));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity()))
            getAboutCMS();
        else
            bindTextview("No Internet Found");


    }

    private void getAboutCMS() {
        loadingStarted();
        Call<ResponseWrapper<CMSEnt>> call = headerWebService.getCMS(AppConstants.TYPE_ABOUT);
        call.enqueue(new Callback<ResponseWrapper<CMSEnt>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<CMSEnt>> call, Response<ResponseWrapper<CMSEnt>> response) {
                loadingFinished();
                if(response.body().getResponse().equals(WebServiceConstants.TOKEN_EXPIRE_CODE) || prefHelper.get_TOKEN().equals("")){
                    UpdateToken token=new UpdateToken(prefHelper.getUserId(),prefHelper.getUser().getEmail(),prefHelper.getUser().getRoleId(),getDockActivity(),prefHelper);
                    token.updateTokenService();
                }
               else if (response.body().getResponse().equals(WebServiceConstants.SUCCESS_RESPONSE_CODE)) {
                    bindTextview(response.body().getResult().getBody());
                } else {
                    UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<CMSEnt>> call, Throwable t) {
                loadingFinished();
                Log.e(ContactUsFragment.class.getSimpleName(), t.toString());
            }
        });
    }



    private void bindTextview(String body) {

        txtTermCondition.setText(checkForNullOREmpty(body.replaceAll("\\<.*?>","")));
        txtTermCondition.setMovementMethod(new ScrollingMovementMethod());
    }




}
