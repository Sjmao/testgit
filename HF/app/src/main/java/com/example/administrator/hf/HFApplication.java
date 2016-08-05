package com.example.administrator.hf;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import test.greenDAO.bean.Entity;
import test.greenDAO.dao.DaoMaster;
import test.greenDAO.dao.DaoSession;
import test.greenDAO.dao.EntityDao;

/**
 * Created by Administrator on 2016/2/24.
 */
public class HFApplication extends Application {

    private static HFApplication hfApplication;
    private HFApplication(){}
    public static HFApplication getInstance(){
        if (hfApplication==null) {
            hfApplication = new HFApplication();
        }
        return hfApplication ;
    }
    public static HFApplication newInstance() {
        return getInstance();
    }
    @Override
    public void onCreate() {
        super.onCreate();
        HttpUtils.initcoockie();
    }




}
