package kr.edcan.neologism.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import kr.edcan.neologism.R;
import kr.edcan.neologism.adapter.MyDicRecyclerAdapter;
import kr.edcan.neologism.databinding.ActivityMyDicViewBinding;
import kr.edcan.neologism.model.MyDic;
import kr.edcan.neologism.model.MyDicViewData;
import kr.edcan.neologism.model.Word;
import kr.edcan.neologism.utils.DataManager;
import kr.edcan.neologism.utils.NetworkHelper;
import kr.edcan.neologism.utils.NetworkInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

public class MyDicViewActivity extends AppCompatActivity {
    int codeType;
    Toolbar toolbar;
    RecyclerView recyclerView;
    ActivityMyDicViewBinding binding;
    Intent intent;
    DataManager manager;
    String dicName;
    MyDicRecyclerAdapter adapter;
    ArrayList<Word> arrayList;
    Call<MyDicViewData> getMyDicInfo;
    NetworkInterface service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_dic_view);
        setDefault();
        setData();
    }


    private void setDefault() {
        manager = new DataManager(getApplicationContext());
        intent = getIntent();
        binding.myDicViewNoData.setVisibility(View.INVISIBLE);
        dicName = intent.getStringExtra("dicName");
        binding.myDicViewProgrees.setThickness(15);
        binding.myDicViewProgrees.setColor(Color.parseColor("#393939"));
        binding.myDicViewProgrees.startAnimation();
        service = NetworkHelper.getNetworkInstance();
        toolbar = binding.toolbar;
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.collapsingToolbar.setTitle(dicName+" 코드");
        binding.collapsingToolbar.setExpandedTitleTextAppearance(R.style.expandedTitleStyle);
        binding.collapsingToolbar.setCollapsedTitleTextAppearance(R.style.collapsedTitleStyle);
        recyclerView = binding.myDicViewRecyclerView;
        binding.myDicViewAcc.setImageResource(R.drawable.acc_mydicsebu_imoji);
        binding.collapsingToolbar.setContentScrimColor(Color.parseColor("#393939"));
        binding.myDicViewHeadeBG.setBackgroundColor(Color.parseColor("#444444"));
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(Color.parseColor("#333232"));
    }

    private void setData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(false);
        arrayList = new ArrayList<>();
        getMyDicInfo = service.getMyDicInfo(manager.getActiveUser().second.getToken(), dicName);
        getMyDicInfo.enqueue(new Callback<MyDicViewData>() {
            @Override
            public void onResponse(Call<MyDicViewData> call, Response<MyDicViewData> response) {
                Log.e("asdf", response.code() + "");
                switch (response.code()) {
                    case 200:
                        arrayList = response.body().getWordlist();
                        adapter = new MyDicRecyclerAdapter(MyDicViewActivity.this, arrayList);
                        recyclerView.setAdapter(adapter);
                        binding.myDicViewProgrees.stopAnimation();
                        binding.myDicViewProgrees.setVisibility(View.GONE);
                        if (arrayList.size() == 0)
                            binding.myDicViewNoData.setVisibility(View.VISIBLE);

                        break;
                    default:
                        Toast.makeText(MyDicViewActivity.this, "내 사전을 불러오는 중 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MyDicViewData> call, Throwable t) {
                Log.e("asdf", t.getMessage());
                Toast.makeText(MyDicViewActivity.this, "내 사전을 불러오는 중 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
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
