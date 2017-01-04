package edu.np.ece.beaconmonitor;

/**
 * Created by Sonata on 6/13/2016.
 */

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.widget.EditText;

import org.altbeacon.beacon.Region;

public class Preferences {
    public static ProgressDialog loading;
    public static boolean isShownLoading = false;

    public static String student_major = "1";
    public static String student_minor = "2";

    public static int cnt = 0;

    public static Activity activity;

    public static void showLoading(final Activity activity, final String title, final String message){
        try {
            if (!isShownLoading) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading = ProgressDialog.show(activity, title, message, false, false);
                        isShownLoading = true;
                    }
                });

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void dismissLoading(){
        try {
            if (isShownLoading) {

                loading.dismiss();
                isShownLoading = false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void setActivity(Activity act)
    {
        activity = act;
    }

    public static Activity getActivity() {
        return activity;
    }

    public static void notify(Context context, String title, String content) {
//        boolean isBeingDebugged = android.os.Debug.isDebuggerConnected();
//        if(!isBeingDebugged){
//            return;
//        }
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification noti = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.mipmap.ic_launcher2)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .build();
        mNotificationManager.notify(Preferences.cnt++, noti);
    }
}
