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
     *
     * @param params
     * @return
     */
    @POST("user/register")
    Observable<HttpResult<Object>> userRegister(@QueryMap Map<String, String> params);

    @POST("user/checkSmsCode")
    Observable<HttpResult<Object>> verifyPhone(@QueryMap Map<String, String> params);

    @POST("user/login")
    Observable<HttpResult<Object>> userLogin(@QueryMap Map<String, String> params);
    @POST("user/loginOut")
    Observable<HttpResult<Object>> loginOut(@QueryMap Map<String, String> params);
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

    @POST("user/getFollowUser")
    Observable<HttpResult<Object>> getFollowUser(@QueryMap Map<String, String> params);

    @POST("user/followUser")
    Observable<HttpResult<Object>> followUser(@QueryMap Map<String, String> params);

    @POST("user/getUidArticle")
    Observable<HttpResult<Object>> getUidArticle(@QueryMap Map<String, String> params);

    @POST("user/getUidcollect")
    Observable<HttpResult<Object>> getUidSave(@QueryMap Map<String, String> params);

    @POST("user/getUidComment")
    Observable<HttpResult<Object>> getUidComment(@QueryMap Map<String, String> params);

    @POST("user/getUser")
    Observable<HttpResult<Object>> getUserInfo(@QueryMap Map<String, String> params);

    @POST("user/getUserInfo")
    Observable<HttpResult<Object>> isExistUser(@QueryMap Map<String, String> params);

    @POST("user/getUserUids")
    Observable<HttpResult<Object>> getUserUids(@QueryMap Map<String, String> params);
    @POST("user/suggest")
    Observable<HttpResult<Object>> upFeedBack2(@QueryMap Map<String, String> params);
    @POST("user/suggest")
    @Multipart
    Observable<HttpResult<Object>> upFeedBack(@PartMap Map<String, RequestBody> file, @Query("uid") String uid,
                                              @Query("content") String content, @Query("contact") String contact);

    /**
     * @POST("user/getFollowUser") Observable<HttpResult<Object>> getFollowUser(@QueryMap Map<String, String> params);
     * /**
     * 社区模块
     * 首页 获取分类、获取问题列表、获取banner
     * 详情 获取回答、获取评论、获取回复、回答、回复、评论、收藏回答、关注问题
     * 提问 获取话题、提问
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

    @POST("index/getHotComment")
    Observable<HttpResult<Object>> getHotComment(@QueryMap Map<String, String> params);

    @POST("index/getCommentFt")
    Observable<HttpResult<Object>> getCommentList(@QueryMap Map<String, String> params);

    @POST("index/addArticle")
    @Multipart
    Observable<HttpResult<Object>> pubQuestion(@PartMap Map<String, RequestBody> file, @Query("uid") String uid, @Query("token") String token,
                                               @Query("title") String title, @Query("content") String content, @Query("label_id") String label_id);

    @POST("index/addArticle")
    Observable<HttpResult<Object>> pubQuestion2(@QueryMap Map<String, String> params);

    @POST("index/addComment")
    Observable<HttpResult<Object>> pubComment(@QueryMap Map<String, String> params);
    @POST("index/editComment")
    Observable<HttpResult<Object>> editComment(@QueryMap Map<String, String> params);
    @POST("index/editArticle")
    Observable<HttpResult<Object>> editQuestion(@QueryMap Map<String, String> params);
    @POST("index/deleteComment")
    Observable<HttpResult<Object>> deleteComment(@QueryMap Map<String, String> params);
    @POST("index/setNum")
    Observable<HttpResult<Object>> agreeOrPraise(@QueryMap Map<String, String> params);

    @POST("index/followArticle")
    Observable<HttpResult<Object>> focusQuestion(@QueryMap Map<String, String> params);

    @POST("index/followLabel")
    Observable<HttpResult<Object>> followLabel(@QueryMap Map<String, String> params);

    @POST("index/getFollowLabel")
    Observable<HttpResult<Object>> getFollowLabel(@QueryMap Map<String, String> params);

    @POST("index/getFollowArticle")
    Observable<HttpResult<Object>> getFollowArticle(@QueryMap Map<String, String> params);
    @POST("index/getCommentDetail")
    Observable<HttpResult<Object>> getAnswerById(@QueryMap Map<String, String> params);
    /**
     * 专栏模块
     */
    @POST("index/getColumn")
    Observable<HttpResult<Object>> getIndexColumn(@QueryMap Map<String, String> params);
    @POST("index/getColumnDetail")
    Observable<HttpResult<Object>> getColumnDetail(@QueryMap Map<String, String> params);
    @POST("index/getColumnArticle")
    Observable<HttpResult<Object>> getColumnArticle(@QueryMap Map<String, String> params);
    @POST("index/getColumnArticleAll")
    Observable<HttpResult<Object>> getAllArticle(@QueryMap Map<String, String> params);
    @POST("index/getColumnArticles")
    Observable<HttpResult<Object>> getColumnArticles(@QueryMap Map<String, String> params);
    @POST("index/getColumnArticleDetail")
    Observable<HttpResult<Object>> getColumnArticleDetail(@QueryMap Map<String, String> params);
    @POST("index/getRecommendArticle")
    Observable<HttpResult<Object>> getRecommendArticle(@QueryMap Map<String, String> params);
    @POST("index/getUidCollectColumnArticle")
    Observable<HttpResult<Object>> getUidCollectColumnArticle(@QueryMap Map<String, String> params);
    @POST("index/setCollectColumn")
    Observable<HttpResult<Object>> saveArticle(@QueryMap Map<String, String> params);
    @POST("index/setPointColumn")
    Observable<HttpResult<Object>> pointArticle(@QueryMap Map<String, String> params);
    @POST("index/followColumn")
    Observable<HttpResult<Object>> followColumn(@QueryMap Map<String, String> params);
    @POST("index/getFollowColumn")
    Observable<HttpResult<Object>> getFollowColumn(@QueryMap Map<String, String> params);
    /**
     * 搜索模块
     */
    @POST("search/searchUser")
    Observable<HttpResult<Object>> searchUser(@QueryMap Map<String, String> params);
    @POST("search/searchQuest")
    Observable<HttpResult<Object>> searchQuest(@QueryMap Map<String, String> params);
    @POST("search/searchArticle")
    Observable<HttpResult<Object>> searchArticle(@QueryMap Map<String, String> params);
    @POST("search/searchUserDetail")
    Observable<HttpResult<Object>> searchUserAdvanced(@QueryMap Map<String, String> params);
    @POST("user/getProduct")
    Observable<HttpResult<Object>> getProductOrAdd(@QueryMap Map<String, String> params);
    /**
     *  直播模块
     */
    @POST("live/getLiveList")
    Observable<HttpResult<Object>> getLiveList(@QueryMap Map<String, String> params);
    /**
     * 公共模块 获取地址、销售模式、行业信息、上传图片、轮播图
     *
     * @return
     */
    @POST("Ads/getCarousel")
    Observable<HttpResult<Object>> getBanner(@QueryMap Map<String, String> params);

    @POST("Ads/getNotice")
    Observable<HttpResult<Object>> getNotice(@QueryMap Map<String, String> params);

    @POST("user/getCity")
    Observable<HttpResult<Object>> provinceList(@QueryMap Map<String, String> params);

    @POST("user/getSalesModel")
    Observable<HttpResult<Object>> getSaleModel(@QueryMap Map<String, String> params);

    @POST("user/getIndustry")
    Observable<HttpResult<Object>> industryList(@QueryMap Map<String, String> params);

    @POST("user/uploadHeadImg")
    @Multipart
    Observable<HttpResult<Object>> uploadFile(@PartMap Map<String, RequestBody> file,
                                              @Query("uid") String uid, @Query("token") String token);

    //当文件大时必须使用@streaming流
    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl);
}
