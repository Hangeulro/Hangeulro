package kr.edcan.hangeulro.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import kr.edcan.hangeulro.R;
import kr.edcan.hangeulro.adapter.DicRecyclerAdapter;
import kr.edcan.hangeulro.databinding.ActivityDicMenuBinding;
import kr.edcan.hangeulro.databinding.ActivityDicViewBinding;
import kr.edcan.hangeulro.model.DicData;
import kr.edcan.hangeulro.utils.NetworkHelper;
import kr.edcan.hangeulro.utils.NetworkInterface;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DicViewActivity extends AppCompatActivity {
    int codeType;
    Toolbar toolbar;
    RecyclerView recyclerView;
    ActivityDicViewBinding binding;
    Intent intent;
    String[] titleTextArr = {
            "사랑 코드",
            "우스운 코드",
            "서글픈 코드",
            "화가 난 코드",
            "공감 코드",
            "일상생활 코드"
    };
    String titleTextEngArr[] = {
            "Love",
            "Funny",
            "Sad",
            "Angry",
            "Sympathy",
            "Lifestyle"
    };
    int actionbarColor[] = {R.color.loveActionbarColor, R.color.funActionbarColor, R.color.sadActionbarColor, R.color.angryActionbarColor, R.color.symActionbarColor, R.color.lifeActionbarColor};
    int statusbarColor[] = {R.color.loveStatusColor, R.color.funStatusColor, R.color.sadStatusColor, R.color.angryStatusColor, R.color.symStatusColor, R.color.lifeStatusColor};
    int mainColor[] = {R.color.loveThemeColor, R.color.funThemeColor, R.color.sadThemeColor, R.color.angryThemeColor, R.color.symThemeColor, R.color.lifeThemeColor};
    int emoji[] = {R.drawable.acc_diclove_imoji, R.drawable.acc_dicfun_emoji, R.drawable.acc_dicsad_emoji, R.drawable.acc_dicangry_imoji, R.drawable.acc_dicsym_emoji, R.drawable.acc_diclife_emoji};
    Call<ResponseBody> getWordByType;
    NetworkInterface service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dic_view);
        setDefault();
        setData();
    }


    private void setDefault() {
        binding.dicViewProgrees.setIndeterminate(true);
        binding.dicViewProgrees.setThickness(15);
        binding.dicViewProgrees.startAnimation();
        intent = getIntent();
        codeType = intent.getIntExtra("codeType", -1);
        service = NetworkHelper.getNetworkInstance();
        toolbar = binding.toolbar;

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.collapsingToolbar.setTitle(titleTextArr[codeType]);
        binding.collapsingToolbar.setContentScrimColor(getResources().getColor(actionbarColor[codeType]));
        binding.collapsingToolbar.setExpandedTitleTextAppearance(R.style.expandedTitleStyle);
        binding.collapsingToolbar.setCollapsedTitleTextAppearance(R.style.collapsedTitleStyle);
        recyclerView = binding.dicViewRecyclerView;
        binding.dicViewHeadeBG.setBackgroundColor(getResources().getColor(mainColor[codeType]));
        binding.dicViewAcc.setImageResource(emoji[codeType]);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(getResources().getColor(statusbarColor[codeType]));
    }

    private void setData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(false);
        final ArrayList<DicData> arrayList = new ArrayList<>();
        getWordByType = service.getWordWithType(titleTextArr[codeType].substring(0, 1));
        getWordByType.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("asdf", response.code() + "");
                try {
                    JSONArray a = new JSONArray(response.body().string());
                    for (int i = 0; i < a.length(); i++) {
                        JSONObject result = a.getJSONObject(i);
                        String example = result.getString("ex");
                        Log.e("asdf", example);
                        arrayList.add(new DicData(result.getString("word"), result.getString("mean"), (example.equals("null")) ? "예문이 없습니다." : example, result.getInt("see")));
                    }
                    DicRecyclerAdapter adapter = new DicRecyclerAdapter(getApplicationContext(), arrayList, codeType);
                    recyclerView.setAdapter(adapter);
                    binding.dicViewProgrees.setVisibility(View.GONE);
                } catch (IOException e) {
                    Log.e("asdf", e.getMessage());
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("asdf", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("asdf", t.getMessage());
            }
        });

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
}
