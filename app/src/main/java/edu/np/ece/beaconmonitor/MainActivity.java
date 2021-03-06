package edu.np.ece.beaconmonitor;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by zqi2 on 17/12/16.
 */

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Context context;

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
//    private BeaconManager beaconManager;

    Intent mServiceIntent;
    private BeaconConsumingService mSensorService;

    private static final String VENUE_NAME = "MonitoringVenueBeacon";
    private static final String VENUE_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";

    private static final String LESSON_NAME = "RangingLessonBeacon";
    private static final String LESSON_UUID = "2F234454-CF6D-4A0F-ADF2-F4911BA9FFA6";

    private static final String STUDENT_MAJOR = "1";
    private static final String STUDENT_MINOR = "2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        Preferences.setActivity(this);

        //TODO Request venue and lesson information from server
        //TODO Save data to local
        Preferences.saveDataToLocal(VENUE_NAME, VENUE_UUID,
                LESSON_NAME, LESSON_UUID,
                STUDENT_MAJOR, STUDENT_MINOR);

        mSensorService = new BeaconConsumingService();
        mServiceIntent = new Intent(context, mSensorService.getClass());
        if (!isMyServiceRunning(mSensorService.getClass())) {
            startService(mServiceIntent);
            if (!isMyServiceRunning(mSensorService.getClass())) {
                Preferences.notify(context, "Service", "Fail to start Sensor Service!");
            }
            else
            {
                Preferences.notify(context, "Service", "Sensor Service started successfully!");
            }
        }
        else
        {
            Preferences.notify(context, "Service", "Sensor Service started successfully!");
        }

        Button ATK_BLE_Button = (Button) findViewById(R.id.clearBtn);
        ATK_BLE_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try
                {
                    Intent intent = new Intent(MainActivity.this, BeaconRangingService.class);
                    stopService(intent);

                    startService(intent);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        checkPermissions();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.getIntent() != null) {
            String message = this.getIntent().getStringExtra("message");
            if (message != null) {
//                TextView textView = (TextView) this.findViewById(R.id.textView);
//                textView.setText(message);
            }
        }
    }

    @Override
    protected void onDestroy() {
//        beaconManager.unbind(this);
        //-- Stop service so that it will restart
        stopService(mServiceIntent);
        super.onDestroy();
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check?
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect beacons.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                    }
                });
                builder.show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }
                    });
                    builder.show();
                }
                return;
            }
        }
    }
}
