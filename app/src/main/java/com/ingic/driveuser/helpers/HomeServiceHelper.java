package com.ingic.driveuser.helpers;

import android.util.Log;

import com.ingic.driveuser.activities.DockActivity;
import com.ingic.driveuser.entities.ResponseWrapper;
import com.ingic.driveuser.global.WebServiceConstants;
import com.ingic.driveuser.interfaces.webServiceResponseLisener;
import com.ingic.driveuser.retrofit.WebService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created on 7/17/2017.
 */

public class HomeServiceHelper<T> {
    private T result;
    private webServiceResponseLisener serviceResponseLisener;
    private DockActivity context;
    private BasePreferenceHelper preferenceHelper;
    private WebService webService;
    private Call<T> call;

    public HomeServiceHelper(T response, DockActivity conttext, WebService webService) {
        this.result = response;
        this.serviceResponseLisener = serviceResponseLisener;
        this.context = conttext;
        this.webService = webService;
        this.preferenceHelper = preferenceHelper;
    }

    public HomeServiceHelper(webServiceResponseLisener serviceResponseLisener, DockActivity conttext, WebService webService, BasePreferenceHelper preferenceHelper) {
        this.serviceResponseLisener = serviceResponseLisener;
        this.context = conttext;
        this.webService = webService;
    }

    public void enqueueCall(Call<ResponseWrapper<T>> call, final String tag) {
        if (InternetHelper.CheckInternetConectivityandShowToast(context)) {
            context.onLoadingStarted();
            call.enqueue(new Callback<ResponseWrapper<T>>() {
                @Override
                public void onResponse(Call<ResponseWrapper<T>> call, Response<ResponseWrapper<T>> response) {
                    context.onLoadingFinished();

                    if (response != null && response.body() != null) {
                        if (response.body().getResponse().equals(WebServiceConstants.TOKEN_EXPIRE_CODE)) {
                            UpdateToken token = new UpdateToken(preferenceHelper.getUserId(), preferenceHelper.getUser().getEmail(), preferenceHelper.getUser().getRoleId(), context, preferenceHelper);
                            token.updateTokenService();
                        } else if (response.body().getResponse().equals(WebServiceConstants.SUCCESS_RESPONSE_CODE)) {
                            result = response.body().getResult();

                            serviceResponseLisener.ResponseSuccess(result, tag, response.body().getMessage());
                        } else {
                       /* if(tag.equals(CREATE)){
                            UIHelper.showShortToastInCenter(context,context.getString(R.string.select_destination));
                        }
                        else{*/
                            UIHelper.showShortToastInCenter(context, response.body().getMessage());
                        }
                    }

                }

                @Override
                public void onFailure(Call<ResponseWrapper<T>> call, Throwable t) {
                    context.onLoadingFinished();
                    t.printStackTrace();
                    Log.e(HomeServiceHelper.class.getSimpleName() + " by tag: " + tag, t.toString());
                }
            });
        }
    }

    public void enqueueCall(Call<ResponseWrapper<T>> call, final String tag, final boolean isLoading) {
        if (InternetHelper.CheckInternetConectivityandShowToast(context)) {

            call.enqueue(new Callback<ResponseWrapper<T>>() {
                @Override
                public void onResponse(Call<ResponseWrapper<T>> call, Response<ResponseWrapper<T>> response) {
                    context.onLoadingFinished();

                    if (response != null && response.body() != null && response.body().getResponse()!=null) {
                        if (response.body().getResponse().equals(WebServiceConstants.TOKEN_EXPIRE_CODE)) {
                            UpdateToken token = new UpdateToken(preferenceHelper.getUserId(), preferenceHelper.getUser().getEmail(), preferenceHelper.getUser().getRoleId(), context, preferenceHelper);
                            token.updateTokenService();
                        } else if (response.body().getResponse().equals(WebServiceConstants.SUCCESS_RESPONSE_CODE)) {
                            result = response.body().getResult();

                            serviceResponseLisener.ResponseSuccess(result, tag, response.body().getMessage());
                        } else {
                            //  UIHelper.showShortToastInCenter(context, response.body().getMessage());
                        }
                    }

                }

                @Override
                public void onFailure(Call<ResponseWrapper<T>> call, Throwable t) {
                    context.onLoadingFinished();
                    t.printStackTrace();
                    Log.e(HomeServiceHelper.class.getSimpleName() + " by tag: " + tag, t.toString());
                }
            });
        }
    }

    public T getResult() {
        return result;
    }

}
