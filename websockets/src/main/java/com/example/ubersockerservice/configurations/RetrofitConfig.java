package com.example.ubersockerservice.configurations;

import com.example.ubersockerservice.apis.BookingServiceApi;
import com.example.ubersockerservice.configurations.interceptors.AuthInterceptor;
import com.example.ubersockerservice.configurations.interceptors.LoggingInterceptor;
import com.example.ubersockerservice.configurations.interceptors.ServiceDiscoveryInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.discovery.EurekaClient;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
public class RetrofitConfig {
//    private final EurekaClient eurekaClient;
//
//    public RetrofitConfig(EurekaClient eurekaClient) {
//        this.eurekaClient = eurekaClient;
//    }
//    private String getServiceUrl(String serviceName){
//        return eurekaClient.getNextServerFromEureka(serviceName,false).getHomePageUrl();
    @Bean
    public OkHttpClient okHttpClient(ServiceDiscoveryInterceptor serviceDiscoveryInterceptor, AuthInterceptor authInterceptor, LoggingInterceptor loggingInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(serviceDiscoveryInterceptor)
                .addInterceptor(loggingInterceptor)
                .build();
    }
    @Bean
    public BookingServiceApi bookingServiceApi(OkHttpClient okHttpClient, ObjectMapper objectMapper){
        return new Retrofit.Builder()
                .baseUrl("http://UBERBOOKINGSERVICE/")
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .client(okHttpClient)
                .build()
                .create(BookingServiceApi.class);
    }
}
