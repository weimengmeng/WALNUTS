package com.njjd.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.njjd.application.AppAplication;
import com.njjd.dao.DaoMaster;
import com.njjd.dao.DaoSession;
import com.njjd.dao.IndexNavEntityDao;
import com.njjd.dao.QuestionEntityDao;
import com.njjd.dao.TagEntityDao;

import org.greenrobot.greendao.database.Database;

/**
 * Created by mrwim on 17/7/31.
 */

public class DBHelper extends DaoMaster.OpenHelper{
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    private DBHelper(Context context,String name) {
        super(context, name);
        init();
    }
    /**
     * 静态内部类，实例化对象使用
     */
    private static class SingleInstanceHolder {
        private static final DBHelper INSTANCE = new DBHelper(AppAplication.getContext(),
                "db_walnuts");
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
        mDaoMaster = new DaoMaster(getWritableDatabase());
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

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
            @Override
            public void onCreateAllTables(Database db, boolean ifNotExists) {
                DaoMaster.createAllTables(db, ifNotExists);
            }
            @Override
            public void onDropAllTables(Database db, boolean ifExists) {
                DaoMaster.dropAllTables(db, ifExists);
            }
        },QuestionEntityDao.class, IndexNavEntityDao.class, TagEntityDao.class);
    }
}
