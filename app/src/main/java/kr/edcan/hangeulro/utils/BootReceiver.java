package kr.edcan.hangeulro.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Junseok on 2016. 1. 17..
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            if (context.getSharedPreferences("Exchat", 0).getBoolean("onBoot", true))
                context.startService(new Intent(context, ClipBoardService.class));
        }
    }
}
