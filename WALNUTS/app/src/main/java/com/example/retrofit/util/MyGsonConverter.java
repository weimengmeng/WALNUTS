package com.example.retrofit.util;

import com.google.gson.Gson;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;


/**
 * Created by Administrator on 2016/8/26.
 */
public class MyGsonConverter extends Converter.Factory {
    public static MyGsonConverter create() {
        return create(new Gson());
    }

    public static MyGsonConverter create(Gson gson) {
        return new MyGsonConverter(gson);
    }

    private final Gson gson;

    private MyGsonConverter(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.gson = gson;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new GsonResponseBodyConverter<>(gson,type);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] annotations, Annotation[] annotations2, Retrofit retrofit) {
        return new GsonRequestBodyConverter<>(gson, type);
    }
}
