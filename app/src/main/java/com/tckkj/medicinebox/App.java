package com.tckkj.medicinebox;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tckkj.medicinebox.entity.Bean;
import com.tckkj.medicinebox.util.LocalManageUtil;
import com.tckkj.medicinebox.util.SPUtil;
import com.tckkj.medicinebox.util.StringUtil;
import com.umeng.commonsdk.UMConfigure;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * kylin on 2017/12/12.
 */

public class App extends Application {
    public static Map<String, Long> map;

    public static String token = "";

    public static boolean islogin=false;    //是否登录
    public static boolean isFirstIn = true;    //是否第一次打开应用
    public static Bean.LoginMsg loginMsg;       //登录后返回的用户信息
    public static String lastAccount;           //上次登录所用账号
    public static String lastAreaCode;           //上次登录所用地区编号

    public static boolean isLanguageSelected = false;     //是否已经选择语言
    public static boolean isMainEngineSelect = false;     //是否已经选择主机
    public static String languageType = "";                 //当前选择语言
    public static String hostOid = "";                      //当前选择的主机id
    public static String hostCode = "";                      //当前选择的主机编号

    /**
     * 维护Activity 的list
     */
    private static List<Activity> mActivitys = Collections.synchronizedList(new LinkedList<Activity>());

    @Override
    protected void attachBaseContext(Context base) {
        //保存系统选择语言
        LocalManageUtil.saveSystemCurrentLanguage(base);
        super.attachBaseContext(LocalManageUtil.setLocal(base));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //保存系统选择语言
        LocalManageUtil.onConfigurationChanged(getApplicationContext());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityListener();
        LocalManageUtil.setApplicationLanguage(this);

        islogin = (boolean) SPUtil.getData(this, "islogin", false);
        isFirstIn = (boolean) SPUtil.getData(this, "isFirstIn", true);
        token = (String) SPUtil.getData(this, "token", "");
        lastAccount = (String) SPUtil.getData(this, "lastAccount", "");
        lastAreaCode = (String) SPUtil.getData(this, "lastAreaCode", "86");
        loginMsg = SPUtil.getBeanFromSp(this, "loginMsg", "loginMsg");
        isLanguageSelected = (boolean)SPUtil.getData(this, "isLanguageSelected", false);
        isMainEngineSelect = (boolean)SPUtil.getData(this, "isMainEngineSelect", false);
        languageType = (String)SPUtil.getData(this, "languageType", "");
        hostOid = (String)SPUtil.getData(this, "hostOid", "");
        hostCode = (String)SPUtil.getData(this, "hostCode", "");

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        UMConfigure.init(this,"5ba986baf1f5567047000012","Umeng",UMConfigure.DEVICE_TYPE_PHONE, null);
        UMConfigure.setLogEnabled(true);
    }

    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.gray, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });
        /*PlatformConfig.setWeixin("wxef582ac741b222a3","d4db62de24d27748ae6a34030fe3d74a");
        PlatformConfig.setQQZone("1106701003","3YJoucb85kutzgnQ");*/
    }

    public static boolean isInstall(Context context, String pkg) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
//                Log.e("pn","pn="+pn);
                if (pn.equalsIgnoreCase(pkg)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 结束所有Activity
     */
    public static void finishAllActivity() {
        if (mActivitys == null) {
            return;
        }
        for (Activity activity : mActivitys) {
            if (!(activity.getClass().equals(MainActivity.class)))
                activity.finish();
        }
        mActivitys.clear();
    }

    /**
     * 结束所有Activity
     */
    public static void finishRealAllActivity() {
        if (mActivitys == null) {
            return;
        }
        for (Activity activity : mActivitys) {
            activity.finish();
        }
        mActivitys.clear();
    }

    /**
     * 得到MainActivity
     */
    public static MainActivity getMainActivity() {
        if (mActivitys == null) {
            return null;
        }
        for (Activity activity : mActivitys) {
            if ((activity.getClass().equals(MainActivity.class)))
                return (MainActivity)activity;
        }
        return null;
    }

    /**
     * @param activity 作用说明 ：添加一个activity到管理里
     */
    public void pushActivity(Activity activity) {
        mActivitys.add(activity);
    }

    /**
     * @param activity 作用说明 ：删除一个activity在管理里
     */
    public void popActivity(Activity activity) {
        mActivitys.remove(activity);
    }


    private void registerActivityListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                    /*
                     *  监听到 Activity创建事件 将该 Activity 加入list,MainActivity主页面不加入
                     */
//                    if (!(activity.getClass().equals(MainActivity.class)))
                    pushActivity(activity);

                }

                @Override
                public void onActivityStarted(Activity activity) {

                }

                @Override
                public void onActivityResumed(Activity activity) {

                }

                @Override
                public void onActivityPaused(Activity activity) {

                }

                @Override
                public void onActivityStopped(Activity activity) {

                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

                }

                @Override
                public void onActivityDestroyed(Activity activity) {
                    if (null==mActivitys&&mActivitys.isEmpty()){
                        return;
                    }
                    if (mActivitys.contains(activity)){
                        /*
                         *  监听到 Activity销毁事件 将该Activity 从list中移除
                         */
                        popActivity(activity);
                    }
                }
            });
        }
    }
}
