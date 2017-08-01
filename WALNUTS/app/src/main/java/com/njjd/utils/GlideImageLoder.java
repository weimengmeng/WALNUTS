package com.njjd.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.njjd.http.HttpManager;
import com.njjd.walnuts.R;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by mrwim on 17/7/26.
 */

public class GlideImageLoder extends ImageLoader {
    private static GlideImageLoder instance;

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide.with(context).load(path).centerCrop()
                .thumbnail(0.5f)
                .placeholder(R.drawable.head)
                .error(R.drawable.head)
                .dontAnimate().into(imageView);
    }

    //获取单例
    public static GlideImageLoder getInstance() {
        if (instance == null) {
            synchronized (HttpManager.class) {
                if (instance == null) {
                    instance = new GlideImageLoder();
                }
            }
        }
        return instance;
    }
}
