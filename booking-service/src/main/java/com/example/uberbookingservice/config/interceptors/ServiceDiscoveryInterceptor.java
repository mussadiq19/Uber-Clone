package com.example.uberbookingservice.config.interceptors;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jspecify.annotations.NonNull;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class ServiceDiscoveryInterceptor implements Interceptor {
    private final LoadBalancerClient loadBalancerClient;

    public ServiceDiscoveryInterceptor(LoadBalancerClient loadBalancerClient) {
        this.loadBalancerClient = loadBalancerClient;
    }

    @Override
    public @NonNull Response intercept(@NonNull Chain chain) throws IOException {
        Request request =chain.request();
        //request.url().host gives the hostname her eg UBERSokectService
        ServiceInstance serviceInstance=loadBalancerClient.choose(request.url().host());//chooses one of the instances
        if (serviceInstance ==null){
            throw new IllegalStateException(
                    "No Instance  for service available"+ request.url().host()
            );
        }
        return chain.proceed(
                request.newBuilder()
                        .url(request.url().newBuilder()
                                .port(serviceInstance.getPort())
                                .host(serviceInstance.getHost()).build()

                        ).build()
        );
    }
}
