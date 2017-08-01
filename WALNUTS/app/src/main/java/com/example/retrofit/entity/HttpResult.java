package com.example.retrofit.entity;

/**
 * 处理http返回数据类
 * Created by WMM on 2016/8/25.
 * @version 1.0
 */
public class HttpResult<T> {
    private int succeed;
    private String msg;
    private T data;
    public int getResultCode() {
        return succeed;
    }

    public void setResultCode(int resultCode) {
        this.succeed = resultCode;
    }

    public T getResultData() {
        return data;
    }

    public void setResultData(T resultData) {
        this.data = resultData;
    }

    public String getResultMsg() {
        return msg;
    }

    public void setResultMsg(String resultMsg) {
        this.msg = resultMsg;
    }


    @Override
    public String toString() {
        return "HttpResult{" +
                "resultCode=" + succeed +
                ", resultMsg='" + msg + '\'' +
                ", resultData=" + data +
                '}';
    }
}
