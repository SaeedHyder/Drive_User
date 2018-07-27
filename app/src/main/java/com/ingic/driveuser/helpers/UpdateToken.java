package com.ingic.driveuser.helpers;

import android.util.Log;

import com.ingic.driveuser.activities.DockActivity;
import com.ingic.driveuser.entities.ResponseWrapper;
import com.ingic.driveuser.entities.UserEnt;
import com.ingic.driveuser.entities.updateTokenEnt;
import com.ingic.driveuser.fragments.SettingFragment;
import com.ingic.driveuser.fragments.abstracts.BaseFragment;
import com.ingic.driveuser.global.WebServiceConstants;
import com.ingic.driveuser.retrofit.WebService;
import com.ingic.driveuser.retrofit.WebServiceFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by saeedhyder on 12/19/2017.
 */

public class UpdateToken  {

    String userId;
    String email;
    String roleId;
    DockActivity dockActivity;
    BasePreferenceHelper prefHelper;


    public UpdateToken(String userid, String email, String roleId, DockActivity dockActivity,BasePreferenceHelper prefHelper) {
        this.userId = userid;
        this.email = email;
        this.roleId = roleId;
        this.dockActivity = dockActivity;
        this.prefHelper=prefHelper;
    }


    public void updateTokenService(){
        WebService webService = WebServiceFactory.getWebServiceInstanceWithCustomInterceptor(dockActivity, WebServiceConstants.SERVICE_URL);

        Call<ResponseWrapper<updateTokenEnt>> call = webService.UpdateToken(userId,email,roleId);
        dockActivity.onLoadingStarted();
        call.enqueue(new Callback<ResponseWrapper<updateTokenEnt>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<updateTokenEnt>> call, Response<ResponseWrapper<updateTokenEnt>> response) {
                dockActivity.onLoadingFinished();
                prefHelper.set_TOKEN(String.valueOf(response.body().getResult().getToken()));
            }

            @Override
            public void onFailure(Call<ResponseWrapper<updateTokenEnt>> call, Throwable t) {
                dockActivity.onLoadingFinished();
                Log.e(SettingFragment.class.getSimpleName(), t.toString());
            }
        });
    }

}
