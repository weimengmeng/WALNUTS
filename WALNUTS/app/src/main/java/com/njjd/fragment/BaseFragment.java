package com.njjd.fragment;

/**
 * Created by mrwim on 17/7/31.
 */

import android.support.v4.app.Fragment;

public abstract class BaseFragment extends Fragment {
    protected boolean isVisible = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isVisible = true;
            lazyInitData();
        } else {
            isVisible = false;
        }
    }
    public abstract void lazyInitData();
}
