package com.example.ubersockerservice.configurations.interceptors;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class AuthInterceptor implements Interceptor {
    @Override
    public @NonNull Response intercept(Interceptor.@NonNull Chain chain) throws IOException {
        Request request =chain.request();
        Request newRequest =request.newBuilder()
                .header("Authorization","Bearer socket-service-token").build();
        return chain.proceed(newRequest);
    }
}
