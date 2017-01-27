package kr.edcan.neologism.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import kr.edcan.neologism.R;
import kr.edcan.neologism.adapter.DicRecyclerAdapter;
import kr.edcan.neologism.databinding.ActivityDicViewBinding;
import kr.edcan.neologism.model.DicData;
import kr.edcan.neologism.utils.NetworkHelper;
import kr.edcan.neologism.utils.NetworkInterface;
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
            "#사랑",
            "#우스운",
            "#서글픈",
            "#화가 난",
            "#공감되는",
            "#일상생활"
    };
    String titleTextEngArr[] = {
            "Love",
            "Funny",
            "Sad",
            "Angry",
            "Sympathy",
            "Lifestyle"
    };
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
        intent = getIntent();
        codeType = intent.getIntExtra("codeType", -1);
        service = NetworkHelper.getNetworkInstance();
        toolbar = binding.toolbar;
        toolbar.setContentInsetsAbsolute(0,0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.collapsingToolbar.setTitle(titleTextArr[codeType]);
        binding.collapsingToolbar.setContentScrimColor(getResources().getColor(R.color.colorPrimary));
        binding.collapsingToolbar.setExpandedTitleTextAppearance(R.style.expandedTitleStyle);
        binding.collapsingToolbar.setCollapsedTitleTextAppearance(R.style.collapsedTitleStyle);
        recyclerView = binding.dicViewRecyclerView;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    private void setData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(false);
        final ArrayList<DicData> arrayList = new ArrayList<>();

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
