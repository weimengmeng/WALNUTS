package com.example.retrofit.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by WMM on 2016/8/26.
 * @version 1.0
 */
public class JSONUtils<T> {
    private static JsonParser parser = new JsonParser();
    private static JsonElement el;
    private static Gson gson=new Gson();
    private List<T> list;
    /**
     *解析成一个jsonObject对象
     * @param Obj
     * @return
     */
    public static JsonObject getAsJsonObject(Object Obj){
        JsonObject object=null;
        el= parser.parse(gson.toJson(Obj));
        if(el.isJsonObject()){
            object = el.getAsJsonObject();
        }
        return object;
    }

    /**
     * 解析成一个jsonArray数组
     * @param Obj
     * @return
     */
    public static JsonArray getAsJsonArray(Object Obj){
        JsonArray object=null;
        el= parser.parse(gson.toJson(Obj));
        if(el.isJsonArray()){
            object = el.getAsJsonArray();
        }
        return object;
    }

    /**
     *通过反射机制获得一个list数组
     * @param data
     * @param jsonArray
     * @return
     */
    public List<T> getList(T data,JsonArray jsonArray){
        list=new ArrayList<>();
        T info=null;
        Iterator it = jsonArray.iterator();
        while(it.hasNext()){
            JsonElement e = (JsonElement)it.next();
            info = (T) gson.fromJson(e,data.getClass());
            list.add(info);
        }
        return list;
    }
    /**
     *通过反射机制获得一个对象
     * @param data
     * @param jsonObject
     * @return
     */
    public T getObject(T data,JsonObject jsonObject){
        T info= (T) gson.fromJson(jsonObject,data.getClass());
        return info;
    }
}
