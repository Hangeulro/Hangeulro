package kr.edcan.neologism.activity;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

import kr.edcan.neologism.R;
import kr.edcan.neologism.adapter.MyDicListViewAdapter;
import kr.edcan.neologism.databinding.ActivityMyDicBinding;
import kr.edcan.neologism.model.CommonData;
import kr.edcan.neologism.model.MyDic;
import kr.edcan.neologism.utils.DataManager;
import kr.edcan.neologism.utils.NetworkHelper;
import kr.edcan.neologism.utils.NetworkInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyDicActivity extends AppCompatActivity {

    ActivityMyDicBinding binding;
    ListView listView;
    Call<ArrayList<MyDic>> getMyDicList;
    NetworkInterface service;
    DataManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_dic);
        setData();
        setAppbarLayout();
    }

    private void setData() {
        manager = new DataManager(getApplicationContext());
        service = NetworkHelper.getNetworkInstance();
        getMyDicList = service.getMyDictionary(manager.getActiveUser().second.getToken());
        getMyDicList.enqueue(new Callback<ArrayList<MyDic>>() {
            @Override
            public void onResponse(Call<ArrayList<MyDic>> call, Response<ArrayList<MyDic>> response) {
                switch (response.code()) {
                    case 200:
                        Log.e("asdf", "len " + response.body().size());
                        for (MyDic m : response.body()) {
                            Log.e("asdf", m.getDicname());
                        }
                        break;
                    default:
                        Log.e("asdfd", response.code() + "");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<MyDic>> call, Throwable t) {

            }
        });
        listView = binding.mydicListView;
        ArrayList<CommonData> arrayList = new ArrayList<>();
        listView.setAdapter(new MyDicListViewAdapter(getApplicationContext(), arrayList));
    }

    private void setAppbarLayout() {
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setBackgroundColor(Color.parseColor("#DD4513"));
        binding.toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
