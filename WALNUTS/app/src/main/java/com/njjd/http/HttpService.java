package com.njjd.http;

import com.example.retrofit.entity.HttpResult;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * service统一接口数据
 * Created by WMM on 2017/7/24.
 */
public interface HttpService {
    /**
     * 用户模块 注册、登录、图形验 证码、手机验证码、完善信息、第三方登录
     * @param params
     * @return
     */
    @POST("register")
    Observable<HttpResult<Object>> userRegister(@QueryMap Map<String, String> params);
    @POST("sendSms")
    Observable<HttpResult<Object>> verifyPhone(@QueryMap Map<String, String> params);
    @POST("login")
    Observable<HttpResult<Object>> userLogin(@QueryMap Map<String, String> params);
    @POST("sendSms")
    Observable<HttpResult<Object>> phoneCode(@QueryMap Map<String, String> params);
    @POST("setUserInfo")
    Observable<HttpResult<Object>> completeInfo(@QueryMap Map<String, String> params);
    @POST("authlogin")
    Observable<HttpResult<Object>> thirdLogin(@QueryMap Map<String, String> params);
    /**
     * 公共模块 获取地址、行业、上传图片
     * @return
     */
    @POST("getCity")
    Observable<HttpResult<Object>> provinceList(@QueryMap Map<String, String> params);
    @POST("getCity")
    Observable<HttpResult<Object>> cityList(@QueryMap Map<String, String> params);
    @POST("industryList")
    Observable<HttpResult<Object>> industryList(@QueryMap Map<String, String> params);
    @POST("fileUpload/uploadImage")
    @Multipart
    Observable<HttpResult<Object>> uploadFile(@PartMap Map<String, RequestBody> file);
    //当文件大时必须使用@streaming流
    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl);
}
