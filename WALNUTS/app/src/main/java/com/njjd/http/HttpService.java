package com.njjd.http;

import com.example.retrofit.entity.HttpResult;
import com.njjd.utils.SPUtils;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.QueryName;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * service统一接口数据
 * Created by WMM on 2017/7/24.
 */
public interface HttpService {
    /**
     * 用户模块
     * 注册、登录、图形验证码、手机验证码、完善信息、第三方登录、找回密码、第三方绑定
     * @param params
     * @return
     */
    @POST("user/register")
    Observable<HttpResult<Object>> userRegister(@QueryMap Map<String, String> params);
    @POST("user/checkSmsCode")
    Observable<HttpResult<Object>> verifyPhone(@QueryMap Map<String, String> params);
    @POST("user/login")
    Observable<HttpResult<Object>> userLogin(@QueryMap Map<String, String> params);
    @POST("user/sendSms")
    Observable<HttpResult<Object>> phoneCode(@QueryMap Map<String, String> params);
    @POST("user/setUserInfo")
    Observable<HttpResult<Object>> completeInfo(@QueryMap Map<String, String> params);
    @POST("user/authlogin")
    Observable<HttpResult<Object>> thirdLogin(@QueryMap Map<String, String> params);
    @POST("user/authBind")
    Observable<HttpResult<Object>> authBind(@QueryMap Map<String, String> params);
    @POST("user/forgetPwd")
    Observable<HttpResult<Object>> setNewPwd(@QueryMap Map<String, String> params);
    @POST("user/checkPhone")
    Observable<HttpResult<Object>> checkPhone(@QueryMap Map<String, String> params);
    @POST("user/getInvitation")
    Observable<HttpResult<Object>> checkInvitation(@QueryMap Map<String, String> params);
    /**
     * 社区模块
     * 首页 获取分类、获取问题列表、获取banner
     * 详情 获取回答、获取评论、获取回复、回答、回复、评论、收藏回答、关注问题
     * 提问 获取标签、提问
     */
    @POST("index/getCateArticle")
    Observable<HttpResult<Object>> getNav(@QueryMap Map<String, String> params);
    @POST("index/getLabel")
    Observable<HttpResult<Object>> getTag(@QueryMap Map<String, String> params);
    @POST("index/getArticle")
    Observable<HttpResult<Object>> getQuestionList(@QueryMap Map<String, String> params);
    @POST("index/articleDetail")
    Observable<HttpResult<Object>> getArticleDetail(@QueryMap Map<String, String> params);
    @POST("index/getCommentF")
    Observable<HttpResult<Object>> getAnswerList(@QueryMap Map<String, String> params);
    @POST("index/getCommentFt")
    Observable<HttpResult<Object>> getCommentList(@QueryMap Map<String, String> params);
    @POST("index/addArticle")
    @Multipart
    Observable<HttpResult<Object>> pubQuestion(@PartMap Map<String, RequestBody> file, @Query("uid") String uid,@Query("token") String token,
            @Query("title") String title,@Query("content") String content,@Query("label_id") String label_id);
    @POST("index/addArticle")
    Observable<HttpResult<Object>> pubQuestion2(@QueryMap Map<String, String> params);
    @POST("index/addComment")
    Observable<HttpResult<Object>> pubComment(@QueryMap Map<String, String> params);
    @POST("index/setNum")
    Observable<HttpResult<Object>> agreeOrPraise(@QueryMap Map<String, String> params);
    @POST("index/followArticle")
    Observable<HttpResult<Object>> focusQuestion(@QueryMap Map<String, String> params);
    @POST("index/getReply")
    Observable<HttpResult<Object>> getReplyList(@QueryMap Map<String, String> params);
    @POST("index/pubReply")
    Observable<HttpResult<Object>> pubReply(@QueryMap Map<String, String> params);
    /**
     * 通知消息模块
     */
    /**
     *  个人中心模块
     */
    /**
     * 公共模块 获取地址、销售模式、行业信息、上传图片
     * @return
     */
    @POST("user/getCity")
    Observable<HttpResult<Object>> provinceList(@QueryMap Map<String, String> params);
    @POST("user/getSalesModel")
    Observable<HttpResult<Object>> getSaleModel(@QueryMap Map<String, String> params);
    @POST("user/getIndustry")
    Observable<HttpResult<Object>> industryList(@QueryMap Map<String, String> params);
    @POST("user/uploadHeadImg")
    @Multipart
    Observable<HttpResult<Object>> uploadFile(@PartMap Map<String, RequestBody> file,
                                              @Query("uid") String uid,@Query("token") String token);
    //当文件大时必须使用@streaming流
    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl);
}
