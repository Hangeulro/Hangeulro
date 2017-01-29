package kr.edcan.neologism.activity;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;

import kr.edcan.neologism.R;
import kr.edcan.neologism.activity.DicDetailViewActivity;
import kr.edcan.neologism.databinding.DicViewRecyclerContentBinding;
import kr.edcan.neologism.model.Board;
import kr.edcan.neologism.model.DicData;
import kr.edcan.neologism.model.MyDic;
import kr.edcan.neologism.utils.DataManager;
import kr.edcan.neologism.utils.NetworkHelper;
import kr.edcan.neologism.utils.NetworkInterface;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import kr.edcan.neologism.R;
import kr.edcan.neologism.databinding.ActivityDicDetailViewBinding;
import kr.edcan.neologism.model.DicData;
import kr.edcan.neologism.model.MyDic;
import kr.edcan.neologism.utils.DataManager;
import kr.edcan.neologism.utils.NetworkHelper;
import kr.edcan.neologism.utils.NetworkInterface;

public class DicDetailViewActivity extends AppCompatActivity implements View.OnClickListener {

    NetworkInterface service;
    DataManager manager;
    private String wordInfoJson;
    private int codeType;
    Intent intent;
    ActivityDicDetailViewBinding binding;
    DicData data;
    String[] titleTextArr = {
            "#사랑",
            "#우스운",
            "#서글픈",
            "#화가 난",
            "#공감되는",
            "#일상생활"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dic_detail_view);
        setDefault();
    }

    private void setDefault() {
        // Toolbar Settings
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Intent to get data from previous activity
        intent = getIntent();
        service = NetworkHelper.getNetworkInstance();
        manager = new DataManager(getApplicationContext());
        wordInfoJson = intent.getStringExtra("wordInfo");
        codeType = intent.getIntExtra("codeType", 0);
        data = new Gson().fromJson(wordInfoJson, DicData.class);
        getSupportActionBar().setTitle("");
        binding.toolbar.setTitleTextColor(Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#414142"));
        }

        // Set data to ui
        binding.wordName.setText(data.getWord());
        binding.wordMeaning.setText(data.getMean());
        binding.wordExample.setText(data.getExample());
        binding.addToDic.setOnClickListener(this);
        binding.share.setOnClickListener(this);
        binding.bgImage.setImageResource(new int[]{R.drawable.bg_dic_lovetag, R.drawable.bg_dic_funtag, R.drawable.bg_dic_sadtag, R.drawable.bg_dic_angrytag, R.drawable.bg_dic_satisfiedtag, R.drawable.bg_dic_lifetag}[codeType]);
        binding.wordType.setText(titleTextArr[codeType]);

    }

    private void shareText(String s) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, s);
        startActivity(Intent.createChooser(sharingIntent, "신조어 공유!"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share:
                shareText(
                        data.getWord() + " 의 의미는 " + data.getMean() + " 입니다. 더 많은 신조어를 한글을 한글로! 에서 알아보세요! http://iwin247.kr"
                );
                break;
            case R.id.addToDic:
                if (manager.getActiveUser().first)
                    addToDic(data);
                else {
                    Toast.makeText(this, "먼저 로그인해주세요.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), AuthActivity.class));
                }
                break;
        }
    }

    private void addToDic(final DicData data) {

        Call<ArrayList<MyDic>> getMyDictionary = service.getMyDictionary(manager.getActiveUser().second.getToken());
        getMyDictionary.enqueue(new Callback<ArrayList<MyDic>>() {
            @Override
            public void onResponse(Call<ArrayList<MyDic>> call, Response<ArrayList<MyDic>> response) {
                switch (response.code()) {
                    case 200:
                        final ArrayList<String> result = new ArrayList<>();
                        for (MyDic d : response.body()) {
                            result.add(d.getDicname());
                        }
                        if (result.size() == 0) {
                            Toast.makeText(DicDetailViewActivity.this, "내 사전 메뉴에서 먼저 사전을 생성해주세요!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MyDicActivity.class));
                        } else
                            new MaterialDialog.Builder(DicDetailViewActivity.this)
                                    .items(result)
                                    .itemsCallback(new MaterialDialog.ListCallback() {
                                        @Override
                                        public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                            Call<ResponseBody> addToDictionary = service.addToDictionary(manager.getActiveUser().second.getToken(),
                                                    result.get(which), data.getId());
                                            addToDictionary.enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                    switch (response.code()) {
                                                        case 200:
                                                            Toast.makeText(getApplicationContext(), "저장이 완료되었습니다!", Toast.LENGTH_SHORT).show();
                                                            break;
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                    Log.e("asdf", t.getMessage());
                                                }
                                            });

                                        }
                                    })
                                    .show();
                        break;
                    default:
                        Log.e("asdf", response.code() + "");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<MyDic>> call, Throwable t) {

            }
        });
    }

}
