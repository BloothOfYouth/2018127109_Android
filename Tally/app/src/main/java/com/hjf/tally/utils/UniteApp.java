package com.hjf.tally.utils;

import android.app.Application;

import com.hjf.tally.db.DBManager;

/**
 * 全局应用的类
 * @author hjf
 * @create 2020-12-24 17:19
 */
public class UniteApp extends Application {

    /**
     * 只要一打开应用，不管在哪个界面都会一开始执行这个代码
     */
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化数据库
        DBManager.initDataBase(getApplicationContext());
    }
}
