package com.xinooo.lightwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class LightWidget extends AppWidgetProvider {

    public static final String ACTION_BUTTON = "action_button";
    public static final String IS_MAX_BRIGHTNESS = "is_max_brightness";
    public static final String SYSTEM_BRIGHTNESS = "system_brightness";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent == null)return;
        String action = intent.getAction();
        if (action!= null && action.equals(ACTION_BUTTON) && requestWriteSettingPermission(context)){
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.light_widget);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName appWidgetId = new ComponentName(context, LightWidget.class);

            if (GTConfig.instance().getBooleanValue(IS_MAX_BRIGHTNESS, false)){
                GTConfig.instance().setBooleanValue(IS_MAX_BRIGHTNESS, false);
                int brightness = Integer.parseInt(GTConfig.instance().getStringValue(SYSTEM_BRIGHTNESS, "255"));
                setSystemBrightness(context, brightness);
                views.setImageViewResource(R.id.btn_light, R.mipmap.bulb_off);
            }else {
                GTConfig.instance().setStringValue(SYSTEM_BRIGHTNESS, String.valueOf(getSystemBrightness(context)));
                setSystemBrightness(context, 255);
                GTConfig.instance().setBooleanValue(IS_MAX_BRIGHTNESS, true);
                views.setImageViewResource(R.id.btn_light, R.mipmap.bulb_on);
            }
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.light_widget);

        if (GTConfig.instance().getBooleanValue(IS_MAX_BRIGHTNESS, false)){
            views.setImageViewResource(R.id.btn_light, R.mipmap.bulb_on);
        }else {
            views.setImageViewResource(R.id.btn_light, R.mipmap.bulb_off);
        }

        Intent intentUpdate = new Intent(context, LightWidget.class);
        intentUpdate.setAction(ACTION_BUTTON);
        PendingIntent pendingUpdate = PendingIntent.getBroadcast(context, appWidgetId, intentUpdate, PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.btn_light, pendingUpdate);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static int getSystemBrightness(Context context){
        try {
            return Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return 255;
    }

    public static void setSystemBrightness(Context context, int value){
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, value);
    }

    public static boolean requestWriteSettingPermission(Context context) {
        if (Settings.System.canWrite(context)) {
            return true;
        }
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (context.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
            context.startActivity(intent);
        }
        return false;
    }

}