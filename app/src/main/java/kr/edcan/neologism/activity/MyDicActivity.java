package kr.edcan.neologism.activity;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;

import kr.edcan.neologism.R;
import kr.edcan.neologism.adapter.MyDicListViewAdapter;
import kr.edcan.neologism.databinding.ActivityMyDicBinding;
import kr.edcan.neologism.model.CommonData;
import kr.edcan.neologism.model.MyDic;
import kr.edcan.neologism.utils.DataManager;
import kr.edcan.neologism.utils.NetworkHelper;
import kr.edcan.neologism.utils.NetworkInterface;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyDicActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityMyDicBinding binding;
    ListView listView;
    Call<ArrayList<MyDic>> getMyDicList;
    Call<ResponseBody> createDic;
    NetworkInterface service;
    DataManager manager;
    ArrayList<MyDic> arrayList;
    MyDicListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_dic);
        manager = new DataManager(getApplicationContext());
        service = NetworkHelper.getNetworkInstance();
        listView = binding.mydicListView;
        arrayList= new ArrayList<>();
        setData(false);
        setAppbarLayout();
    }

    private void setData(final boolean reconfig) {
        getMyDicList = service.getMyDictionary(manager.getActiveUser().second.getToken());
        getMyDicList.enqueue(new Callback<ArrayList<MyDic>>() {
            @Override
            public void onResponse(Call<ArrayList<MyDic>> call, Response<ArrayList<MyDic>> response) {
                switch (response.code()) {
                    case 200:
                        if(reconfig) arrayList.clear();
                        for(MyDic m : response.body()){
                            arrayList.add(m);
                        }
                        if (!reconfig) {
                            adapter = new MyDicListViewAdapter(getApplicationContext(), arrayList);
                            listView.setAdapter(adapter);
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();

                                }
                            });
                        }
                        binding.mydicFab.setOnClickListener(MyDicActivity.this);
                        break;
                    default:
                        Log.e("asdfd", response.code() + "");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<MyDic>> call, Throwable t) {
                Log.e("asdf", t.getMessage());
            }
        });
    }

    private void setAppbarLayout() {
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setBackgroundColor(Color.parseColor("#DD4513"));
        binding.toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void showEditDialog(String contentString) {
        View view = LayoutInflater.from(this).inflate(R.layout.common_dialog_input_view, null);
        TextView title = (TextView) view.findViewById(R.id.dialog_title);
        final EditText editText = (EditText) view.findViewById(R.id.dialog_edittext);
        final EditText editTextSum = (EditText) view.findViewById(R.id.dialog_edittext_summary);
        title.setText(contentString);
        new MaterialDialog.Builder(MyDicActivity.this)
                .customView(view, false)
                .positiveColor(getResources().getColor(R.color.colorPrimary))
                .positiveText("생성")
                .negativeText("취소")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        final String result = editText.getText().toString().trim();
                        final String resultSum = editTextSum.getText().toString().trim();
                        if (result.equals("") || resultSum.equals("")) {
                            Toast.makeText(MyDicActivity.this, "사전 이름을 입력해 주세요!", Toast.LENGTH_SHORT).show();
                            editText.setText("");
                        } else {
                            createDic = service.createMyDictionary(manager.getActiveUser().second.getToken(), result, resultSum);
                            createDic.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    switch (response.code()) {
                                        case 200:
                                            Toast.makeText(MyDicActivity.this, "사전을 생성했습니다!", Toast.LENGTH_SHORT).show();
                                            setData(true);
                                            break;
                                        case 409:
                                            Toast.makeText(MyDicActivity.this, "이미 동일한 이름을 가진 사전이 존재합니다!", Toast.LENGTH_SHORT).show();
                                            break;
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Log.e("asdf", t.getMessage());
                                    Toast.makeText(MyDicActivity.this, "서버와의 연동에 문제가 발생했습니다", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                })
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mydicFab:
                showEditDialog("내 사전 만들기");
        }
    }
}
