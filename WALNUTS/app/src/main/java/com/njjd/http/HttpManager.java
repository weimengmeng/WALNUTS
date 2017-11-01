package com.njjd.http;

import com.example.retrofit.entity.BaseEntity;
import com.example.retrofit.listener.ProgressListener;
import com.example.retrofit.util.MyGsonConverter;
import com.example.retrofit.util.UploadFileRequestBody;
import com.njjd.application.AppAplication;
import com.njjd.utils.SPUtils;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
/**
 * http交互处理类
 * Created by WMM on 2017/7/24.
 * BASE_URL必须以“/”符号结束
 */
public class HttpManager {
//    public static final String BASE_URL = "http://116.62.243.41/api/";
//    public static final String BASE_URL2 = "http://116.62.243.41/";
  public static final String BASE_URL = "http://192.168.0.111/hetao_api/public/index.php/api/";
  public static final String BASE_URL2 = "http://192.168.0.111/hetao_api/public/";
    /**
     * 设置超时时间，默认6s
     */
    private static final int DEFAULT_TIMEOUT = 6;
    private HttpService httpService;
    private volatile static HttpManager INSTANCE;
    private BaseEntity baseBar;
    private Observable observable;

    //构造方法私有
    private HttpManager() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(MyGsonConverter.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        httpService = retrofit.create(HttpService.class);
    }
    //获取单例
    public static HttpManager getInstance() {
        if (INSTANCE == null) {
            synchronized (HttpManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpManager();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 用户模块
     * @param basePar
     */
    public void userLogin(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.userLogin(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void thirdLogin(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.thirdLogin(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void phoneCode(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.phoneCode(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void verifyPhone(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.verifyPhone(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void checkInvitation(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.checkInvitation(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void userRegister(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.userRegister(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void checkPhone(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.checkPhone(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void setNewPwd(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.setNewPwd(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void authBind(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.authBind(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void completeInfo(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.completeInfo(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void isExistUser(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.isExistUser(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void getFollowUser(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.getFollowUser(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void followUser(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.followUser(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void getUidArticle(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.getUidArticle(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void getUidComment(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.getUidComment(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void getUserInfo(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.getUserInfo(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void getUserUids(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.getUserUids(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    /**
     * 社区模块
     */
    public void getNav(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.getNav(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void getTag(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.getTag(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void upFeedBack(BaseEntity basePar, ProgressListener listener,String uid,String content,String contact){
        baseBar = basePar;
        Map<String, RequestBody> map = new HashMap<>();
        List<File> files=basePar.getFiles();
        for(int i=0;i<files.size();i++){
            UploadFileRequestBody fileRequestBody = new UploadFileRequestBody(files.get(i),listener );
            map.put("img[]\"; filename=\""+files.get(i).getName()+"", fileRequestBody);
        }
        observable = httpService.upFeedBack(map,uid,content,contact).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void upFeedBack2(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.upFeedBack2(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void pubQuestion(BaseEntity basePar, ProgressListener listener,String uid,String token,String title,String content,String label_id){
        baseBar = basePar;
        Map<String, RequestBody> map = new HashMap<>();
        List<File> files=basePar.getFiles();
        for(int i=0;i<files.size();i++){
            UploadFileRequestBody fileRequestBody = new UploadFileRequestBody(files.get(i),listener );
            map.put("imgs[]\"; filename=\""+files.get(i).getName()+"", fileRequestBody);
        }
        observable = httpService.pubQuestion(map,uid,token,title,content,label_id).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void pubQuestion2(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.pubQuestion2(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void pubComment(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.pubComment(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void getQuestionList(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.getQuestionList(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void getArticleDetail(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.getArticleDetail(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void getAnswerList(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.getAnswerList(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void getCommentList(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.getCommentList(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void getHotComment(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.getHotComment(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void agreeOrPraise(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.agreeOrPraise(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void focusQuestion(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.focusQuestion(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void focusLabel(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.followLabel(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void getFollowLabel(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.getFollowLabel(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void getFollowArticle(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.getFollowArticle(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void getUidSave(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.getUidSave(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void getNotice(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.getNotice(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    /**
     *
     *专栏模块
     */
    public void getIndexColumn(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.getIndexColumn(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void getColumnDetail(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.getColumnDetail(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void getColumnArticleDetail(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.getColumnArticleDetail(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void getColumnArticle(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.getColumnArticle(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void getRecommendArticle(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.getRecommendArticle(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void getColumnArticles(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.getColumnArticles(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void getUidCollectColumnArticle(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.getUidCollectColumnArticle(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void saveArticle(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.saveArticle(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void pointArticle(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.pointArticle(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void followColumn(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.followColumn(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void getFollowColumn(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.getFollowColumn(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    /**
     *  公共模块
     * @param basePar
     */
    public void getBanner(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.getBanner(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void provinceList(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.provinceList(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void getSaleModel(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.getSaleModel(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void industryList(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.industryList(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    /**
     * 上传文件必须加上filename ，file是参数名
     * Meditype是multipart/form-data
     * @param basePar
     * @param listener
     */
    public void uploadFile(BaseEntity basePar, ProgressListener listener,String uid,String token) {
        baseBar = basePar;
        Map<String, RequestBody> map = new HashMap<>();
        if (basePar.getFile() != null) {
            UploadFileRequestBody fileRequestBody = new UploadFileRequestBody(basePar.getFile(),listener );
            map.put("img\"; filename=\""+basePar.getFile().getName()+"", fileRequestBody);
        }
        observable = httpService.uploadFile(map,uid,token).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    /**
     * 请求响应订阅
     * @param observer
     * @param subscriber
     */
    private void toSubscribeOn(Observable observer, Subscriber subscriber) {
        observer.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    /**
     * 添加一些公共的参数，比如token等等
     * 也可以通过retrofit请求拦截器添加
     * @param map
     * @return
     */
    private Map<String,String> addPublicParams(Map<String,String> map){
        map.put("token", SPUtils.get(AppAplication.getContext(),"token","").toString());
        return map;
    }
    /**
     * 取消请求
     */
    public void cancle() {
        if (baseBar != null && observable != null) {
            if (!baseBar.getSubscirber().isUnsubscribed()) {
                baseBar.getSubscirber().unsubscribe();
            }
            baseBar = null;
            observable = null;
        }
    }
}
