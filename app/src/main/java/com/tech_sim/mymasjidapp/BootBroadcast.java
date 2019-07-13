package com.tech_sim.mymasjidapp;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.tech_sim.mymasjidapp.service.MasjidTimeService;

public class BootBroadcast extends BroadcastReceiver {
    private static final String TAG_BOOT_BROADCAST_RECEIVER = "BOOT_BROADCAST_RECEIVER";
    private Context context;

    @SuppressLint("NewApi")
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;
        Log.e("onCallStateChanged","intent state Detected");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
          //  context.startForegroundService(new Intent(context, ScheduleService.class));
            context.startForegroundService(new Intent(context, MasjidTimeService.class));
           // ContextCompat.startForegroundService(context,new Intent(context, ScheduledService.class));
            Log.e("Broadcast", "Oreo Boot_completed");
        } else {
          //  context.startService(new Intent(context,ScheduleService.class));
            context.startService(new Intent(context, MasjidTimeService.class));
            Log.e("Broadcast", "Boot_completed");
        }
    }

 
}
