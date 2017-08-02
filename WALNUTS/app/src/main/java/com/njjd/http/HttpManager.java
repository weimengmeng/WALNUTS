package com.njjd.http;

import com.example.retrofit.entity.BaseEntity;
import com.example.retrofit.listener.ProgressListener;
import com.example.retrofit.util.MyGsonConverter;
import com.example.retrofit.util.UploadFileRequestBody;
import com.google.gson.Gson;

import java.util.HashMap;
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
    public static final String BASE_URL = "http://192.168.100.105/hetao_api/public/index.php/index/user/";
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
    public void userRegister(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.userRegister(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void completeInfo(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.completeInfo(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }

    /**
     *  公共模块
     * @param basePar
     */
    public void provinceList(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.provinceList(basePar.getParams()).map(basePar);
        toSubscribeOn(observable, basePar.getSubscirber());
    }
    public void cityList(BaseEntity basePar) {
        baseBar = basePar;
        observable = httpService.cityList(basePar.getParams()).map(basePar);
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
    public void uploadFile(BaseEntity basePar, ProgressListener listener) {
        baseBar = basePar;
        Map<String, RequestBody> map = new HashMap<>();
        if (basePar.getFile() != null) {
            UploadFileRequestBody fileRequestBody = new UploadFileRequestBody(basePar.getFile(),listener );
            map.put("file\"; filename=\""+basePar.getFile().getName()+"", fileRequestBody);
        }
        observable = httpService.uploadFile(map).map(basePar);
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
        map.put("token","");
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
