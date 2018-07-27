package com.ingic.driveuser.helpers;

import android.content.Context;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.ingic.driveuser.entities.ResponseWrapper;
import com.ingic.driveuser.global.WebServiceConstants;
import com.ingic.driveuser.retrofit.WebService;
import com.ingic.driveuser.retrofit.WebServiceFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created on 5/15/2017.
 */

public class TokenUpdater {
    private static final TokenUpdater tokenUpdater = new TokenUpdater();

    public static TokenUpdater getInstance() {
        return tokenUpdater;
    }

    private WebService webservice;

    private TokenUpdater() {
    }

    public void UpdateToken(Context context, String DeviceType, String Token) {
        if (Token.isEmpty()) {
            Log.e("Token Updater", "Token is Empty");
        }
        webservice = WebServiceFactory.getWebServiceInstanceWithCustomInterceptorandheader(context,
                WebServiceConstants.SERVICE_URL);
        Call<ResponseWrapper> call = webservice.updateToken(DeviceType, FirebaseInstanceId.getInstance().getToken());
        call.enqueue(new Callback<ResponseWrapper>() {
            @Override
            public void onResponse(Call<ResponseWrapper> call, Response<ResponseWrapper> response) {
                if (response != null && response.body() != null) {
                    Log.i("UPDATETOKEN", response.body().getResponse() + "");
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper> call, Throwable t) {
                Log.e("UPDATETOKEN", t.toString());
            }
        });
    }

}
