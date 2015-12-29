package com.clover.example.readcurrentorderexample.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.clover.example.readcurrentorderexample.MainActivity;
import com.clover.example.readcurrentorderexample.OnlineOrderService;
import com.clover.example.readcurrentorderexample.PistachioOrder;
import com.clover.example.readcurrentorderexample.convertor.ItemTypeAdapterFactory;
import com.clover.example.readcurrentorderexample.event.OrdersFetchCompleteEvent;
import com.clover.example.readcurrentorderexample.pojo.Root;
import com.clover.example.readcurrentorderexample.pojo.Token;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by lidon on 11/12/2015.
 */
public class FetchOrdersTask extends AsyncTask<Void, Void, Void> {
    List<PistachioOrder> pistachioOrders;
    EventBus bus=EventBus.getDefault();

    OkHttpClient okHttpClient;
    Retrofit retrofit;
    OnlineOrderService service;
    Gson gson;

    @Override
    protected void onPreExecute() {
        okHttpClient = new OkHttpClient();
        okHttpClient.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                com.squareup.okhttp.Response response = chain.proceed(chain.request());
                return response;
            }
        });
        // setup retrofit with proper interface for the REST call
        retrofit = new Retrofit.Builder()
                .baseUrl("http://ordrsrvr.net/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(OnlineOrderService.class);
        // use the custom TypeAdapterFactory to parse the jsons
        gson = new GsonBuilder()
                .registerTypeAdapterFactory(new ItemTypeAdapterFactory())
                .create();
    }

    public List<PistachioOrder> getPistachioOrders() {
        return pistachioOrders;
    }

    @Override
    protected Void doInBackground(Void... params) {
        final boolean flag=false;
        // the outer callback is for user authenticating
        Call<Token> call = service.authenticateUser("getToken", "bluecharlotte", "welcome", "dgr2yc9wbq");
        call.enqueue(new Callback<Token>() {
            public void onResponse(retrofit.Response<Token> response, Retrofit retrofit) {

                if (response.isSuccess()) {
                    Log.d("demo1", "authenticate success");
                    String token = response.body().getToken();
                    Log.d("demo1", "token: " + token);
                    Retrofit retrofit2 = new Retrofit.Builder()
                            .baseUrl("http://ordrsrvr.net/")
                            .client(okHttpClient)
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .build();

                    // this service is for fetching orders (with the token just got)
                    OnlineOrderService service2 = retrofit2.create(OnlineOrderService.class);
                    Call<Root> call2 = service2.fetchOrders("getOrder", token, "blue_charlotte", "dgr2yc9wbq");
                    call2.enqueue(new Callback<Root>() {
                        public void onResponse(retrofit.Response<Root> response2, Retrofit retrofit2) {

                            if (response2.isSuccess()) {
                                Log.d("demo1", "fetch succeed");
                                pistachioOrders=ItemTypeAdapterFactory.getOrders();
                                Log.d("demo1", pistachioOrders.toString());
                                // notify the MainActivity as soon as the fetch is done by firing an event
                                bus.post(new OrdersFetchCompleteEvent("Fetch Complete"));

                            } else {
                                Log.d("demo1", "fail");
                                // just retry if fail
                                new FetchOrdersTask().execute();
                            }
                        }

                        public void onFailure(Throwable t) { // may have NullPointerException in next line for some unknown reason.
                            Log.d("demo1", "failure");
                            new FetchOrdersTask().execute();
                        }

                    });

                } else {
                    Log.d("demo1", "fail");
                }
            }

            public void onFailure(Throwable t) {
                Log.d("demo1", t.getMessage());
            }

        });
        return null;
    }

    @Override
    protected void onPostExecute(Void voi) {
        super.onPostExecute(voi);

    }

}