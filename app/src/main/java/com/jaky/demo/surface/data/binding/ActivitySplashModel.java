package com.jaky.demo.surface.data.binding;

import android.content.Context;
import android.content.pm.PackageManager;

import com.jaky.demo.surface.R;

import java.util.Calendar;

/**
 * Created by Jack on 2017/12/31.
 */

public class ActivitySplashModel {

    private Context context;

    public ActivitySplashModel(Context context) {
        this.context = context;
    }

    public int getBackgroundImageResID() {
        int resId;
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(calendar.HOUR_OF_DAY);
        if (hour >= 6 && hour <= 12) {
            resId = R.mipmap.morning;
        } else if (hour > 12 && hour <= 18) {
            resId = R.mipmap.afternoon;
        } else {
            resId = R.mipmap.night;
        }
        return resId;
    }


    public String getVersionName() {
        String versionName = null;
        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return String.format(context.getResources().getString(R.string.splash_version), versionName);
    }


    public String getCopyright() {
        return context.getResources().getString(R.string.splash_copyright);
    }
}
