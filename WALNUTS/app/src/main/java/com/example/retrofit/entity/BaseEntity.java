package com.example.retrofit.entity;

import com.example.retrofit.exception.HttpTimeException;
import com.njjd.utils.LogUtils;

import java.io.File;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.functions.Func1;

/**
 * 请求数据统一封装类
 * Created by WMM on 2016/8/26.
 * @version 1.0
 */
public abstract class BaseEntity<T> implements Func1<HttpResult<T>,T> {
    /**
     * 获取传递参数
     */
    public abstract Map<String,Object> getParams();
    /**
     * 获取上传文件
     */
    public abstract File getFile();
    public abstract List<File> getFiles();
    /**
     * 设置回调sub
     *
     * @return
     */
    public abstract Subscriber getSubscirber();


    @Override
    public T call(HttpResult<T> httpResult) {
        if (httpResult.getResultCode() != 0) {
            throw new HttpTimeException(httpResult.getResultMsg());
        }
        return httpResult.getResultData();
    }
}
