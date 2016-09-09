package com.ravijain.sankalp.support;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.activities.SpConstants;
import com.ravijain.sankalp.activities.SpSankalpList;
import com.ravijain.sankalp.data.SpContentProvider;
import com.ravijain.sankalp.data.SpSankalp;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ravijain on 9/3/2016.
 */
public class SpAlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent rIntent) {
        if (rIntent.getAction() != null && rIntent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            SpUtils.startAlarm(context);
        } else {
            SpContentProvider p = SpContentProvider.getInstance(context);
            ArrayList<SpSankalp> sankalps = p.getSankalps(SpConstants.SANKALP_TYPE_BOTH,
                    SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_DAY, SpDateUtils.nextDate(Calendar.getInstance()));

            if (sankalps.size() == 0) return;
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("My notification")
                            .setContentText("You have " + sankalps.size() + " sankalps ending tomorrow");
            // Creates an explicit intent for an Activity in your app
            Intent intent = new Intent(context, SpSankalpList.class);
            intent.putExtra(SpConstants.INTENT_KEY_SANKALP_TYPE, SpConstants.SANKALP_TYPE_BOTH);
            intent.putExtra(SpConstants.INTENT_KEY_SANKALP_LIST_FILTER, SpConstants.INTENT_VALUE_SANKALP_LIST_FILTER_DAY);
            intent.putExtra(SpConstants.INTENT_KEY_SANKALP_LIST_FILTER_DATE_VALUE, SpDateUtils.nextDate(Calendar.getInstance()).getTimeInMillis());

            // The stack builder object will contain an artificial back stack for the
            // started Activity.
            // This ensures that navigating backward from the Activity leads out of
            // your application to the Home screen.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            // Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(SpSankalpList.class);
            // Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(intent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            // mId allows you to update the notification later on.
            mNotificationManager.notify(1, mBuilder.build());
        }
    }
}
