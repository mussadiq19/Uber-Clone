package com.example.uberbookingservice.config.interceptors;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class LoggingInterceptor implements Interceptor {
    @Override
    public @NonNull Response intercept(@NonNull Chain chain) throws IOException {
        Request request=chain.request();
        System.out.println("REQUEST:" +request.method()+" "+request.url());
        Response response=chain.proceed(request);
        System.out.println("RESPONSE"+ response.code());
        return response;
    }
}
