package com.njjd.fragment;

/**
 * Created by mrwim on 17/7/31.
 */

import android.support.v4.app.Fragment;

import com.umeng.analytics.MobclickAgent;

public abstract class BaseFragment extends Fragment {
    protected boolean isVisible = false;
    protected final String TAG = this.getClass().getSimpleName();
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }
    public abstract void lazyInitData();
}
