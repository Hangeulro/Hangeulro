package kr.edcan.neologism.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v4.view.AsyncLayoutInflater;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import kr.edcan.neologism.R;
import kr.edcan.neologism.adapter.CommonListViewAdapter;
import kr.edcan.neologism.adapter.MyPageListViewAdapter;
import kr.edcan.neologism.adapter.NeologismRecyclerAdapter;
import kr.edcan.neologism.databinding.ActivityMainBinding;
import kr.edcan.neologism.databinding.MainListviewFooterBinding;
import kr.edcan.neologism.model.CommonData;
import kr.edcan.neologism.model.MyDic;
import kr.edcan.neologism.utils.ClipBoardService;
import kr.edcan.neologism.utils.DBSync;
import kr.edcan.neologism.utils.DataManager;
import kr.edcan.neologism.utils.NetworkHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static int OVERLAY_PERMISSION_REQ_CODE = 5858;
    public static Activity activity = null;

    public static void finishThis() {
        if (activity != null) activity.finish();
    }

    ActivityMainBinding binding;
    DataManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = new DataManager(this);

        activity = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            setPackage();
        setDefault();
        startService(new Intent(MainActivity.this, ClipBoardService.class));
        DBSync.syncDB(getApplicationContext());
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void setPackage() {
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
        }
    }

    private void setDefault() {
        binding.expandNeologism.setOnClickListener(this);
        binding.myDictionary.setOnClickListener(this);
        binding.neologismTest.setOnClickListener(this);
        binding.mainMyPageLaunch.setOnClickListener(this);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(MainActivity.this, "QuickSearch을 이용하기 위해서는 먼저 권한을 허용해주셔야 합니다.", Toast.LENGTH_SHORT).show();
                setPackage();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.expandNeologism:
                startActivity(new Intent(getApplicationContext(), DicMenuActivity.class));
                break;
            case R.id.myDictionary:
                if (NetworkHelper.returnNetworkState(getApplicationContext()))
                    if (!manager.getActiveUser().first) {
                        Toast.makeText(getApplicationContext(), "먼저 로그인해주세요.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), AuthActivity.class));
                    } else {
                        startActivity(new Intent(getApplicationContext(), MyDicActivity.class));
                    }
                else
                    Toast.makeText(getApplicationContext(), "인터넷 연결 상태를 확인해주세요!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.neologismTest:
                if (NetworkHelper.returnNetworkState(getApplicationContext()))
                    if (!manager.getActiveUser().first) {
                        Toast.makeText(getApplicationContext(), "먼저 로그인해주세요.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), AuthActivity.class));
                    } else {
                        startActivity(new Intent(getApplicationContext(), QuizActivity.class));
                    }
                else
                    Toast.makeText(getApplicationContext(), "인터넷 연결 상태를 확인해주세요!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mainMyPageLaunch:
                if (NetworkHelper.returnNetworkState(getApplicationContext()))
                    if (!manager.getActiveUser().first) {
                        Toast.makeText(getApplicationContext(), "먼저 로그인해주세요.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), AuthActivity.class));
                    } else startActivity(new Intent(getApplicationContext(), MyPageActivity.class));

                else
                    Toast.makeText(getApplicationContext(), "인터넷 연결 상태를 확인해주세요!", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
