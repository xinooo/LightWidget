package com.xinooo.lightwidget;

import android.app.Application;

public class AppMain extends Application {
    private static AppMain app = null;
    public AppMain() {
        super();
        app = this;
    }

    /**
     * 返回应用程序句柄
     *
     * @return AppMain
     */
    public static AppMain getApp() {
        return app;
    }
}
