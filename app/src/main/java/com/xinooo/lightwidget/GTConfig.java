package com.xinooo.lightwidget;

import android.content.Context;
import android.content.SharedPreferences;

public class GTConfig {
    private static GTConfig mOwnConfig = new GTConfig();
    private static final String PREFERENCE_NAME = "Xinooo";
    //系统参数保存对象
    private SharedPreferences mSysPreferences;

    public static GTConfig instance() {
        if (null == mOwnConfig) {
            mOwnConfig = new GTConfig();
        }
        if (null == mOwnConfig.mSysPreferences) {
            mOwnConfig.readPreference(AppMain.getApp());
        }
        return mOwnConfig;
    }

    public SharedPreferences readPreference(Context ctx) {
        mSysPreferences = ctx.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);

        return mSysPreferences;
    }

    public String getStringValue(String key, String value) {
        return mSysPreferences.getString(key, value);
    }

    public String getStringValue(String key) {
        return mSysPreferences.getString(key, null);
    }

    public void setStringValue(String key, String value) {
        mSysPreferences.edit().putString(key, value).apply();
    }

    public boolean getBooleanValue(String type, boolean defaultValue) {
        return mSysPreferences.getBoolean(type, defaultValue);
    }

    public void setBooleanValue(String type, boolean isBoolean) {
        SharedPreferences.Editor editor = mSysPreferences.edit();
        editor.putBoolean(type, isBoolean);
        editor.commit();
    }
}
