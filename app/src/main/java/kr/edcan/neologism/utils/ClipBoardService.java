package kr.edcan.neologism.utils;

import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
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

import io.realm.Realm;
import io.realm.RealmResults;
import kr.edcan.neologism.R;
import kr.edcan.neologism.activity.MainActivity;
import kr.edcan.neologism.databinding.ActivityClipboardPopupViewBinding;
import kr.edcan.neologism.model.DicDBData;


/**
 * Created by Junseok on 2016. 1. 10..
 */
public class ClipBoardService extends Service {

    public static Service service;
    final static int INTENT_KEY = 1209;
    Intent startMain;
    MaterialDialog materialDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Realm realm;
    ClipboardManager manager;

    @Override
    public void onCreate() {
        super.onCreate();
        service = this;
        sharedPreferences = getSharedPreferences("Hangeulro", 0);
        editor = sharedPreferences.edit();
        realm = Realm.getDefaultInstance();
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
        manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        TaskStackBuilder killbuild = TaskStackBuilder.create(this);
        killbuild.addNextIntent(new Intent(getApplicationContext(), KillProcess.class));
        PendingIntent killProcess = killbuild.getPendingIntent(1123, PendingIntent.FLAG_ONE_SHOT);

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
                if (System.currentTimeMillis() - sharedPreferences.getLong("lastFastSearchTime", System.currentTimeMillis() - 201) > 200) {
                    if (manager.getPrimaryClipDescription().toString().contains("text/plain")) {
                        String capturedString;
                        try {
                            capturedString = manager.getPrimaryClip().getItemAt(0).getText().toString();
                        } catch (NullPointerException e){
                            e.printStackTrace();
                            Log.e("asdf", e.getMessage());
                            return;
                        }
                        RealmResults<DicDBData> realmResults = realm.where(DicDBData.class).contains("word", capturedString).findAll();
                        if (realmResults.size() >= 1) {
                            vibrate();
                            showDialog(realmResults.get(0));
                        }

                    }
                    editor.putLong("lastFastSearchTime", System.currentTimeMillis());
                    editor.commit();
                }
            }
        });
        return START_NOT_STICKY;
    }

    private void showDialog(DicDBData data) {
        materialDialog = new MaterialDialog.Builder(getBaseContext())
                .customView(getView(data.getWord(), data.getMean(), data.getExample()), false)
                .theme(Theme.LIGHT)
                .build();
        materialDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        WindowManager.LayoutParams wmlp = materialDialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        materialDialog.show();

    }

    private void vibrate() {
        Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vb.vibrate(500);

    }

    private View getView(String word, String mean, String example) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        ActivityClipboardPopupViewBinding binding = DataBindingUtil.inflate(inflater, R.layout.activity_clipboard_popup_view, null, false);
        binding.quickSearchWord.setText(word);
        binding.quickSearchContent.setText(mean);
//        binding.quickSearchSubContent.setText((example == null) ? "예문이 존재하지 않습니다" : example);
        return binding.getRoot();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
