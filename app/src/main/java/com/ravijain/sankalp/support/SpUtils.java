package com.ravijain.sankalp.support;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.preference.PreferenceManager;
import android.util.DisplayMetrics;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.data.SpCategory;
import com.ravijain.sankalp.data.SpDataConstants;
import com.ravijain.sankalp.fragments.SpSettingsFragment;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by ravijain on 9/3/2016.
 */
public class SpUtils {

    public static void startAlarm(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if (!(prefs.getBoolean(SpSettingsFragment.KEY_PREF_ALARM_REGISTERED, false)) && prefs.getBoolean(SpSettingsFragment.KEY_PREF_REMINDERS, true)) {
            AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, SpAlarmReceiver.class);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

            // Set the alarm to start at 8:30 a.m.
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 8);
            calendar.set(Calendar.MINUTE, 30);

            // With setInexactRepeating(), you have to use one of the AlarmManager interval
            // constants--in this case, AlarmManager.INTERVAL_DAY.
            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, alarmIntent);

            ComponentName receiver = new ComponentName(context, SpAlarmReceiver.class);
            PackageManager pm = context.getPackageManager();

            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(SpSettingsFragment.KEY_PREF_ALARM_REGISTERED, true);
            editor.commit();
        }

    }

    public static void stopAlarm(Context context) {
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, SpAlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        if (alarmMgr != null) {
            alarmMgr.cancel(alarmIntent);

            ComponentName receiver = new ComponentName(context, SpAlarmReceiver.class);
            PackageManager pm = context.getPackageManager();

            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
        }
    }

    public static Intent getEmailIntent()
    {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, "findravi@gmail.com");
        //intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        return intent;
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public static void updateLanguage(Context context, String lang) {
        if (!"".equals(lang)) {
            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            context.getResources().updateConfiguration(config, null);
        }
    }

    public static Drawable getIconDrawable(SpCategory category, Context context)
    {
        /*if (category.getCategoryName().equals(SpDataConstants.CATEGORY_NAME_FOOD)) {
            return context.getResources().getDrawable(R.drawable.ic_restaurant_menu_black_24dp);
        }
        else*/ if (category.getCategoryName().equals(SpDataConstants.CATEGORY_NAME_ENTERTAINMENT)) {
            return context.getResources().getDrawable(R.drawable.ic_local_movies_black_24dp);
        }
        else if (category.getCategoryName().equals(SpDataConstants.CATEGORY_NAME_TRAVEL)) {
            return context.getResources().getDrawable(R.drawable.ic_local_airport_black_24dp);
        }
        else if (category.getCategoryName().equals(SpDataConstants.CATEGORY_NAME_DHARMA)) {
            return context.getResources().getDrawable(R.drawable.ic_dashboard_black_24dp);
        }
        else {
            String letter = String.valueOf(category.getCategoryName().toCharArray()[0]).toUpperCase();
            SpColorGenerator generator = SpColorGenerator.MATERIAL;
            SpTextDrawable.Builder builder = SpTextDrawable.builder();
//            builder.width(24).height(24);
            SpTextDrawable drawable = builder
                    .buildRoundRect(letter, generator.getRandomColor(), 2);
            return drawable;
        }
    }

    public static String getLocalizedString(Context context, String key)
    {
        if (context == null) return key;
        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(key, "string", packageName);
        if (resId == 0) return key;
        return context.getString(resId);
    }

}
