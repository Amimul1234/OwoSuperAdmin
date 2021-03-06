package com.owoSuperAdmin.network;

import android.util.Base64;
import com.owoSuperAdmin.configurationsFile.HostAddress;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static RetrofitClient mInstance;
    private final Retrofit retrofit;

    private static final String AUTH = "Basic "+
            Base64.encodeToString(("owodokan:c711a757bd3a3d528dfade364e61fb5b8397dd074ef1ff7a68a6a285c18cb285").getBytes(), Base64.NO_WRAP);

    private RetrofitClient()
    {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(90, TimeUnit.SECONDS)
                .writeTimeout(90, TimeUnit.SECONDS)
                .connectTimeout(90, TimeUnit.SECONDS)
                .build();

        /*
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(
                        new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Request original = chain.request();

                                Request.Builder requestBuilder = original.newBuilder()
                                        .addHeader("Authorization", AUTH)
                                        .method(original.method(), original.body());

                                Request request = requestBuilder.build();
                                return chain.proceed(request);
                            }
                        }


                ).build();

         */

        retrofit = new Retrofit.Builder()
                .baseUrl(HostAddress.HOST_ADDRESS.getAddress())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public static synchronized RetrofitClient getInstance()
    {
        if(mInstance == null)
        {
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    public Api getApi()
    {
        return retrofit.create(Api.class);
    }
}
