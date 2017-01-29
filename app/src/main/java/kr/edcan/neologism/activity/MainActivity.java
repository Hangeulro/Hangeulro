package kr.edcan.neologism.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Network;
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

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import kr.edcan.neologism.R;
import kr.edcan.neologism.adapter.CommonListViewAdapter;
import kr.edcan.neologism.adapter.MyPageListViewAdapter;
import kr.edcan.neologism.adapter.NeologismRecyclerAdapter;
import kr.edcan.neologism.databinding.ActivityMainBinding;
import kr.edcan.neologism.databinding.MainListviewFooterBinding;
import kr.edcan.neologism.model.CommonData;
import kr.edcan.neologism.model.DicDBData;
import kr.edcan.neologism.model.DicData;
import kr.edcan.neologism.model.MyDic;
import kr.edcan.neologism.utils.ClipBoardService;
import kr.edcan.neologism.utils.DBSync;
import kr.edcan.neologism.utils.DataManager;
import kr.edcan.neologism.utils.NetworkHelper;
import kr.edcan.neologism.utils.NetworkInterface;
import kr.edcan.neologism.utils.StringUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static int OVERLAY_PERMISSION_REQ_CODE = 5858;
    public static Activity activity = null;

    public static void finishThis() {
        if (activity != null) activity.finish();
    }

    DicData currentTodayWord = null;
    String searchQuery;
    Realm realm;
    ActivityMainBinding binding;
    DataManager manager;
    NetworkInterface service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        manager = new DataManager(this);
        realm = Realm.getDefaultInstance();
        service = NetworkHelper.getNetworkInstance();

        binding.expandNeologism.setOnClickListener(this);
        binding.myDictionary.setOnClickListener(this);
        binding.neologismTest.setOnClickListener(this);
        binding.mainMyPageLaunch.setOnClickListener(this);
        binding.loveDic.setOnClickListener(this);
        binding.funnyDic.setOnClickListener(this);
        binding.sadDic.setOnClickListener(this);
        binding.madDic.setOnClickListener(this);
        binding.okDic.setOnClickListener(this);
        binding.lifeDic.setOnClickListener(this);
        binding.searchButton.setOnClickListener(this);
        binding.todayNeologism.setOnClickListener(this);
        checkTodayWord();
    }

    private void checkTodayWord() {
        if (manager.mustUpdateTodayWord()) {
            if (NetworkHelper.returnNetworkState(getApplicationContext())) {
                Call<ResponseBody> getTodayWord = service.getTodayWord();
                getTodayWord.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.code() == 200) {
                            JSONObject content = null;
                            try {
                                content = new JSONObject(response.body().string());
                                JSONArray originTagList = content.getJSONArray("cata");
                                String originTagStr = originTagList.toString();
                                DicData data = new DicData();
                                data.setContents(content.getString("id"),
                                        content.getString("word"),
                                        content.getString("mean"),
                                        content.getString("ex"),
                                        originTagStr);
                                manager.saveTodayWord(data);
                                setTodayWordResult(true, data);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                setTodayWordResult(false, null);
                            } catch (IOException e) {
                                e.printStackTrace();
                                setTodayWordResult(false, null);
                            }
                        } else setTodayWordResult(false, null);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        setTodayWordResult(false, null);
                        Log.e("asdf", t.getMessage() + "");

                    }
                });
            } else
                Toast.makeText(activity, "인터넷에 연결되지 않아 오늘의 신조어를 확인할 수 없습니다.", Toast.LENGTH_SHORT).show();
        } else setTodayWordResult(true, manager.getTodayWord());
    }

    public void setTodayWordResult(boolean isSuccess, DicData data) {
        if (isSuccess) {
            this.currentTodayWord = data;
            binding.todayWordName.setText(data.getWord());
            binding.todayWordMeaning.setText(data.getMean());
        } else {
            binding.todayWordName.setVisibility(View.GONE);
            binding.todayWordMeaning.setText("인터넷에 연결되지 않아 확인할 수 없습니다.");
        }
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
            case R.id.todayNeologism:
                if(currentTodayWord != null) {
                    startActivity(new Intent(getApplicationContext(), DicDetailViewActivity.class)
                            .putExtra("wordInfo", new Gson().toJson(currentTodayWord, DicData.class))
                            .putExtra("codeType", StringUtils.getCodeType(currentTodayWord.getCata())));
                }
                break;
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
            case R.id.loveDic:
                startActivity(new Intent(getApplicationContext(), DicViewActivity.class).putExtra("codeType", 0));
                break;
            case R.id.funnyDic:
                startActivity(new Intent(getApplicationContext(), DicViewActivity.class).putExtra("codeType", 1));
                break;
            case R.id.sadDic:
                startActivity(new Intent(getApplicationContext(), DicViewActivity.class).putExtra("codeType", 2));
                break;
            case R.id.madDic:
                startActivity(new Intent(getApplicationContext(), DicViewActivity.class).putExtra("codeType", 3));
                break;
            case R.id.okDic:
                startActivity(new Intent(getApplicationContext(), DicViewActivity.class).putExtra("codeType", 4));
                break;
            case R.id.lifeDic:
                startActivity(new Intent(getApplicationContext(), DicViewActivity.class).putExtra("codeType", 5));
                break;
            case R.id.searchButton:
                search();
                break;

        }
    }

    private void search() {
        searchQuery = binding.searchQueryInput.getText().toString().trim();
        if (!searchQuery.isEmpty()) {
            realm.beginTransaction();
            RealmResults<DicDBData> result = realm.where(DicDBData.class)
                    .equalTo("word", binding.searchQueryInput.getText().toString())
                    .findAll();
            if (result.size() > 0) {
                DicData data = new DicData(result.get(0));
                startActivity(new Intent(getApplicationContext(), DicDetailViewActivity.class)
                        .putExtra("wordInfo", new Gson().toJson(data, DicData.class))
                        .putExtra("codeType", StringUtils.getCodeType(data.getCata())));
                binding.searchQueryInput.setText("");
            } else Toast.makeText(activity, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
            realm.commitTransaction();
        } else {
            binding.searchQueryInput.setText("");
            Toast.makeText(activity, "검색어를 입력해주세요!", Toast.LENGTH_SHORT).show();
        }
    }
}
