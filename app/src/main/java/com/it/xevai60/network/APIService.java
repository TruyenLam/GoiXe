package com.it.xevai60.network;

import com.it.xevai60.model.Token;
import com.it.xevai60.model.User;
import com.it.xevai60.model.YeuCauXe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIService {
//
//    @POST("/posts")
//    @FormUrlEncoded
//    Call<PhieuNhap> savePost(@Field("title") String title,
//                        @Field("body") String body,
//                        @Field("userId") long userId);
//
//    @PUT("/posts/{id}")
//    @FormUrlEncoded
//    Call<PhieuNhap> updatePost(@Path("id") long id,
//                          @Field("title") String title,
//                          @Field("body") String body,
//                          @Field("userId") long userId);
//
//    @DELETE("/posts/{id}")
//    Call<PhieuNhap> deletePost(@Path("id") long id);
//
//    @GET("NhapKho/Get_KiemTraPhieuNhapKho?")
//    Call<PhieuNhap> getPhieuNhap(@Query("maSoPhieuNhap") String maSoPhieuNhap);

    @Headers( "Content-Type: application/json" )
    @POST("login/login")
    Call<Token> postUser(@Body User user);


    @GET("QLSX/XeVai/Get_YeuCauXeVai?yeucauxevai=a")
    Call<List<YeuCauXe>> getYeuCauXe(@HeaderMap HashMap<String, String> headers);
}