package com.njjd.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.njjd.application.AppAplication;
import com.njjd.http.HttpManager;
import com.njjd.walnuts.R;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by mrwim on 17/7/26.
 */

public class GlideImageLoder2 extends ImageLoader {
    private static GlideImageLoder2 instance;

    @Override
    public void displayImage(final Context context, Object path, ImageView imageView) {
        Glide.with(AppAplication.getContext()).load(path.toString().replace("\\","/")).centerCrop()
                .thumbnail(0.5f)
                .placeholder(R.drawable.error)
                .error(R.drawable.error)
                .dontAnimate().into(imageView);
    }

    //获取单例
    public static GlideImageLoder2 getInstance() {
        if (instance == null) {
            synchronized (HttpManager.class) {
                if (instance == null) {
                    instance = new GlideImageLoder2();
                }
            }
        }
        return instance;
    }
}
