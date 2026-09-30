package com.example.uberbookingservice.config;

import com.example.uberbookingservice.apis.LocationServiceApi;
import com.example.uberbookingservice.apis.UberSocketApi;
import com.example.uberbookingservice.config.interceptors.AuthInterceptor;
import com.example.uberbookingservice.config.interceptors.LoggingInterceptor;
import com.example.uberbookingservice.config.interceptors.ServiceDiscoveryInterceptor;
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

//    private EurekaClient eurekaClient;
//
//    public RetrofitConfig(EurekaClient eurekaClient) {
//        this.eurekaClient = eurekaClient;
//    }
//    private String getServiceUrl(String serviceName){
//        return eurekaClient.getNextServerFromEureka(serviceName,false).getHomePageUrl();
//    }
@Bean
public OkHttpClient okHttpClient(ServiceDiscoveryInterceptor serviceDiscoveryInterceptor, AuthInterceptor authInterceptor, LoggingInterceptor loggingInterceptor) {
    return new OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(serviceDiscoveryInterceptor)
            .addInterceptor(loggingInterceptor)
            .build();
}

    @Bean
    public  LocationServiceApi locationServiceApi(OkHttpClient okHttpClient, ObjectMapper objectMapper){
        return new Retrofit.Builder()
                .baseUrl("http://UBERPROJECT-LOCATIONSERVICE/")
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .client(okHttpClient)
                .build()
                .create(LocationServiceApi.class);
    }
//
//    @Bean
//    public Retrofit retrofit(){
//        return new Retrofit.Builder()
//                .addConverterFactory(GsonConverterFactory.create()).build();
//    }
    @Bean
    public UberSocketApi uberSocketApi(OkHttpClient okHttpClient, ObjectMapper objectMapper){
        return new Retrofit.Builder()
                .baseUrl("http://UBERSOCKERSERVICE/")
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .client(okHttpClient)
                .build()
                .create(UberSocketApi.class);
    }

}
