package com.it.xevai60.network;

public class ApiUtils {
    private ApiUtils() {}

    public static final String BASE_URL = "https://local.thttextile.com.vn/thtapigate/api/";

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}
