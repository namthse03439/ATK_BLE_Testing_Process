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
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import org.altbeacon.beacon.Region;

public class Preferences {

    // tags for Shared Preferences to store and retrieve some piece of data from local
    public static final String SharedPreferencesTag = "Beacon_Monitor_Preferences";
    public static final int SharedPreferences_ModeTag = Context.MODE_PRIVATE;

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

    public static void saveDataToLocal(String Venue_Beacon_Name, String Venue_Beacon_UUID,
                                       String Lesson_Beacon_Name, String Lesson_Beacon_UUID)
    {
        SharedPreferences pref = activity.getSharedPreferences(SharedPreferencesTag, SharedPreferences_ModeTag);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("Venue_Beacon_Name", Venue_Beacon_Name);
        editor.putString("Venue_Beacon_UUID", Venue_Beacon_UUID);
        editor.putString("Lesson_Beacon_Name", Lesson_Beacon_Name);
        editor.putString("Lesson_Beacon_UUID", Lesson_Beacon_UUID);

        editor.apply();
    }

    public static String getVenueBeaconName(Context context)
    {
        SharedPreferences pref = context.getSharedPreferences(SharedPreferencesTag, SharedPreferences_ModeTag);
        String Venue_Beacon_Name = pref.getString("Venue_Beacon_Name", null);
        return Venue_Beacon_Name;
    }

    public static String getVenueBeaconUUID(Context context)
    {
        SharedPreferences pref = context.getSharedPreferences(SharedPreferencesTag, SharedPreferences_ModeTag);
        String Venue_Beacon_UUID = pref.getString("Venue_Beacon_UUID", null);
        return Venue_Beacon_UUID;
    }

    public static String getLessonBeaconName(Context context)
    {
        SharedPreferences pref = context.getSharedPreferences(SharedPreferencesTag, SharedPreferences_ModeTag);
        String Lesson_Beacon_Name = pref.getString("Lesson_Beacon_Name", null);
        return Lesson_Beacon_Name;
    }

    public static String getLessonBeaconUUID(Context context)
    {
        SharedPreferences pref = context.getSharedPreferences(SharedPreferencesTag, SharedPreferences_ModeTag);
        String Lesson_Beacon_UUID = pref.getString("Lesson_Beacon_UUID", null);
        return Lesson_Beacon_UUID;
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
