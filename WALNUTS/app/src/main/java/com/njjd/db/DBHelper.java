package com.njjd.db;

import com.njjd.application.AppAplication;
import com.njjd.dao.DaoMaster;
import com.njjd.dao.DaoSession;

/**
 * Created by mrwim on 17/7/31.
 */

public class DBHelper {
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    private DBHelper() {
        init();
    }
    /**
     * 静态内部类，实例化对象使用
     */
    private static class SingleInstanceHolder {
        private static final DBHelper INSTANCE = new DBHelper();
    }
    /**
     * 对外唯一实例的接口
     *
     * @return
     */
    public static DBHelper getInstance() {
        return SingleInstanceHolder.INSTANCE;
    }
    /**
     * 初始化数据
     */
    private void init() {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(AppAplication.getContext(),
                "db_walnuts");
        mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoMaster getmDaoMaster() {
        return mDaoMaster;
    }

    public DaoSession getmDaoSession() {
        return mDaoSession;
    }

    public DaoSession getNewSession() {
        mDaoSession = mDaoMaster.newSession();
        return mDaoSession;
    }
}
