package kr.edcan.hangeulro.utils;

import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;


import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

import java.util.Date;

import kr.edcan.hangeulro.R;
import kr.edcan.hangeulro.activity.MainActivity;


/**
 * Created by Junseok on 2016. 1. 10..
 */
public class ClipBoardService extends Service {

    final static int INTENT_KEY = 1209;
    Intent startMain;
    MaterialDialog materialDialog;
    public static Service service;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    ClipboardManager manager;

    @Override
    public void onCreate() {
        super.onCreate();
        service = this;
        sharedPreferences = getSharedPreferences("asdf", 0);
        editor = sharedPreferences.edit();
    }


    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("asdf", "sex");
        manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        TaskStackBuilder killbuild = TaskStackBuilder.create(this);
        killbuild.addNextIntent(new Intent(getApplicationContext(), KillProcess.class));
        PendingIntent killProcess = killbuild.getPendingIntent(1123, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("한글을 한글로!")
                .setContentText("퀵서치 기능이 작동중입니다.")
                .addAction(R.drawable.btn_status_deleteapp, "종료", killProcess);

        startMain = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(startMain);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(resultPendingIntent);
        startForeground(1222, builder.build());
        manager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                Log.e("asdf", "hocul");
                if (System.currentTimeMillis() - sharedPreferences.getLong("lastFastSearchTime", System.currentTimeMillis() - 201) > 200) {
                    if (manager.getPrimaryClipDescription().toString().contains("text")) {
                        String capturedString = manager.getPrimaryClip().getItemAt(0).getText().toString();
                        if (capturedString.contains("빼박캔트")) {
                            Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            vb.vibrate(500);
                            materialDialog = new MaterialDialog.Builder(getBaseContext())
                                    .customView(getView(), false)
                                    .theme(Theme.LIGHT)
                                    .build();
                            materialDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                            WindowManager.LayoutParams wmlp = materialDialog.getWindow().getAttributes();
                            wmlp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                            materialDialog.show();
                            editor.putLong("lastFastSearchTime", System.currentTimeMillis());
                            editor.commit();
                        }
                    }
                }
            }
        });
        return START_NOT_STICKY;
    }

    private View getView() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_clipboard_popup_view, null);
        return view;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
