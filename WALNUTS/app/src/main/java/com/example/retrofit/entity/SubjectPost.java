package com.example.retrofit.entity;


import java.io.File;
import java.util.List;
import java.util.Map;
import rx.Subscriber;

/**
 * 测试数据
 * Created by WMM on 2016/8/26.
 */
public class SubjectPost extends BaseEntity {
    //回调sub
    private Subscriber mSubscriber;
    private Map<String,Object> map;
    private File file;
    private List<File> files;

    /**
     * 用于普通请求参数
     * @param getTopMovieOnNext
     * @param map
     */
    public SubjectPost(Subscriber getTopMovieOnNext, Map<String,Object> map) {
        this.mSubscriber = getTopMovieOnNext;
        this.map = map;
    }
    /**
     * 用于文件上传
     * @param getTopMovieOnNext
     * @param file
     */
    public SubjectPost(Subscriber getTopMovieOnNext, File file) {
        this.mSubscriber = getTopMovieOnNext;
        this.file = file;
    }
    /**
     * 用于文件上传
     * @param getTopMovieOnNext
     * @param files
     */
    public SubjectPost(Subscriber getTopMovieOnNext,List<File> files) {
        this.mSubscriber = getTopMovieOnNext;
        this.files = files;
    }
    @Override
    public Map<String,Object> getParams() {
        return map;
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public List<File> getFiles() {
        return files;
    }

    @Override
    public Subscriber getSubscirber() {
        return mSubscriber;
    }

}
