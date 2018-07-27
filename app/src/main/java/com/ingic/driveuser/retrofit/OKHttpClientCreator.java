package com.ingic.driveuser.retrofit;


import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.ingic.driveuser.global.AppConstants;
import com.ingic.driveuser.helpers.BasePreferenceHelper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class OKHttpClientCreator {

    private static NotificationManager mNotifyManager;
    private static NotificationCompat.Builder mBuilder;
    private static BasePreferenceHelper preferenceHelper;

    public static OkHttpClient createCustomInterceptorClient(Context context) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addNetworkInterceptor(new CustomInterceptor(progressListener))
                .addInterceptor(interceptor)
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder().addHeader("Accept-Encoding", "identity").build();
                        return chain.proceed(request);
                    }
                })

                .build();


    }
    public static OkHttpClient createCustomInterceptorClientwithHeader(Context context) {
        preferenceHelper = new BasePreferenceHelper(context);

        if (preferenceHelper.get_TOKEN() != null) {
            AppConstants.HeaderToken = preferenceHelper.get_TOKEN();
        } else {
            AppConstants.HeaderToken = "";
        }
        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new CustomInterceptor(progressListener))
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();

                        Request request = original.newBuilder()
                                .header("token", AppConstants.HeaderToken)
                                .method(original.method(), original.body())
                                .build();

                        return chain.proceed(request);
                    }
                })
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder().addHeader("Accept-Encoding", "identity").build();
                        return chain.proceed(request);
                    }
                })
                .build();


        return client;

    }

    public static OkHttpClient createDefaultInterceptorClient(Context context) {

        OkHttpClient client = new OkHttpClient.Builder()

                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Response originalResponse = chain.proceed(chain.request());
                        return originalResponse.newBuilder()
                                .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                                .build();
                    }
                })
                .build();


        return client;

    }

    final static ProgressResponseBody.ProgressListener progressListener = new ProgressResponseBody.ProgressListener() {
        @Override
        public void update(long bytesRead, long contentLength, boolean done) {
          //  int percent = (int) ((100 * bytesRead) / contentLength);
        }
    };


}
