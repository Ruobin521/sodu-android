package com.ruobin.sodu.Util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MyUtils {
    private static String channel = null;

    public static String getChannel(Context context) {
        if (channel == null) {
            ApplicationInfo ai = null;
            channel = "shape_maintext_box";
            try {
                ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);

                if (ai != null) {
                    channel = String.valueOf(ai.metaData.get("UMENG_CHANNEL"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return channel;
    }

    public static int getStatusBarHeight(Context context) {
        try {
            @SuppressWarnings("rawtypes")
            Class clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            Field field = clazz.getField("status_bar_height");

            int id = Integer.parseInt(field.get(object).toString());
            return context.getResources().getDimensionPixelSize(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Get the screen height.
     *
     * @param context
     * @return the screen height
     */
    public static int getScreenHeight(Context context) {
        if (context != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);

            return displayMetrics.heightPixels;
        } else {
            return 1920;
        }
    }

    /**
     * Get the screen width.
     *
     * @param context
     * @return the screen width
     */
    public static int getScreenWidth(Context context) {
        if (context != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);

            return displayMetrics.widthPixels;
        } else {
            return 1080;
        }
    }

    public static float getDensity(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.density;
    }

    public static String getMetaValue(Context context, String metaKey) {

        if (context == null || metaKey == null) {
            return null;
        }

        try {
            ApplicationInfo aiApplicationInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);

            if (null != aiApplicationInfo) {
                if (null != aiApplicationInfo.metaData) {
                    return aiApplicationInfo.metaData.getString(metaKey);
                }
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 格式化日期
     * yyyy-MM-dd 转为 yyyy年MM月dd日
     *
     * @param dateStr
     * @return
     */
    public static String formatSystemDateCN(String dateStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String standarTime;

            Date date = simpleDateFormat.parse(dateStr);
            simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
            standarTime = simpleDateFormat.format(date);

            return standarTime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    public static float dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (float) (dipValue * scale + 0.5f);
    }

    public static float px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (float) (pxValue / scale + 0.5f);
    }

    /**
     * 获取标题栏的高度
     *
     * @param activity
     * @return
     */
    public int getTitleHeight(Activity activity) {
        Rect rect = new Rect();
        Window window = activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rect);
        int statusBarHeight = rect.top;
        int contentViewTop = window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int titleBarHeight = contentViewTop - statusBarHeight;

        return titleBarHeight;
    }

    /**
     * 获取状态栏高度
     *
     * @param activity
     * @return
     */
    public int getStateHeight(Activity activity) {
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }

    /**
     * 获取屏幕宽高
     *
     * @param activity
     * @return int[0] 宽，int[1]高
     */
    public int[] getScreenWidthAndSizeInPx(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int[] size = new int[2];
        size[0] = displayMetrics.widthPixels;
        size[1] = displayMetrics.heightPixels;
        return size;
    }

    /**
     * 获取虚拟按键栏高度
     *
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        if (hasNavBar(context)) {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }

    /**
     * 检查是否存在虚拟按键栏
     *
     * @param context
     * @return
     */
    private static boolean hasNavBar(Context context) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
        if (resourceId != 0) {
            boolean hasNav = res.getBoolean(resourceId);
            // check override flag
            String sNavBarOverride = getNavBarOverride();
            if ("1".equals(sNavBarOverride)) {
                hasNav = false;
            } else if ("0".equals(sNavBarOverride)) {
                hasNav = true;
            }
            return hasNav;
        } else { // fallback
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
    }

    /**
     * 判断虚拟按键栏是否重写
     *
     * @return
     */
    private static String getNavBarOverride() {
        String sNavBarOverride = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
            }
        }
        return sNavBarOverride;
    }

}

