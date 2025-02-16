package com.procmatrix.client.config;

import com.procmatrix.api.MatrixApiApi;
import com.procmatrix.invoker.ApiClient;
import com.procmatrix.rotation.api.MatrixRotationApiApi;
import com.procmatrix.rotation.invoker.auth.HttpBasicAuth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

@Configuration
public class ApiClientConfig {

    @Value("${procmatrix.service.url}")
    private String procmatrixServiceUrl;

    @Value("${matrixrotation.service.url}")
    private String matrixRotationServiceUrl;

    @Value("${procmatrix.service.username}")
    private String procmatrixServiceUsername;

    @Value("${procmatrix.service.password}")
    private String procmatrixServicePassword;

    @Value("${matrixrotation.service.username}")
    private String matrixRotationServiceUsername;

    @Value("${matrixrotation.service.password}")
    private String matrixRotationServicePassword;

    @Bean
    public MatrixApiApi matrixApi() {
        ApiClient procmatrixClient = new ApiClient();
        procmatrixClient.setBasePath(procmatrixServiceUrl);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor(procmatrixServiceUsername, procmatrixServicePassword))
                .build();
        procmatrixClient.setHttpClient(httpClient);
        return new MatrixApiApi(procmatrixClient);
    }

    @Bean
    public MatrixRotationApiApi matrixRotationApi() {
        com.procmatrix.rotation.invoker.ApiClient matrixrotationClient = new com.procmatrix.rotation.invoker.ApiClient();
        matrixrotationClient.setBasePath(matrixRotationServiceUrl);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor(matrixRotationServiceUsername, matrixRotationServicePassword))
                .build();
        matrixrotationClient.setHttpClient(httpClient);
        return new MatrixRotationApiApi(matrixrotationClient);
    }

    static class BasicAuthInterceptor implements Interceptor {
        private final String credentials;

        public BasicAuthInterceptor(String username, String password) {
            this.credentials = Credentials.basic(username, password);
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request authenticatedRequest = request.newBuilder()
                    .header("Authorization", credentials)
                    .build();
            return chain.proceed(authenticatedRequest);
        }
    }

}