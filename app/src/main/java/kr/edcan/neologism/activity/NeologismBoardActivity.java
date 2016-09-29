package kr.edcan.neologism.activity;

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
import kr.edcan.neologism.adapter.NeologismRecyclerAdapter;
import kr.edcan.neologism.databinding.ActivityNeologismBoardBinding;
import kr.edcan.neologism.model.Board;
import kr.edcan.neologism.utils.NetworkHelper;
import kr.edcan.neologism.utils.NetworkInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NeologismBoardActivity extends AppCompatActivity {
    int codeType;
    Toolbar toolbar;
    RecyclerView recyclerView;
    ActivityNeologismBoardBinding binding;
    Call<ArrayList<Board>> getBoardList;
    ArrayList<Board> arrayList = new ArrayList<>();
    NetworkInterface service;
    NeologismRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_neologism_board);
        setDefault();
        setData();
    }


    private void setDefault() {
        binding.neologismBoardProgrees.setIndeterminate(true);
        binding.neologismBoardProgrees.setThickness(15);
        binding.neologismBoardProgrees.setColor(Color.parseColor("#393939"));
        binding.neologismBoardProgrees.startAnimation();
        service = NetworkHelper.getNetworkInstance();
        toolbar = binding.toolbar;
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.collapsingToolbar.setTitle("신조어 게시판");
        binding.collapsingToolbar.setExpandedTitleTextAppearance(R.style.expandedTitleStyle);
        binding.collapsingToolbar.setCollapsedTitleTextAppearance(R.style.collapsedTitleStyle);
        recyclerView = binding.neologismBoardRecyclerView;
        binding.collapsingToolbar.setContentScrimColor(Color.parseColor("#393939"));
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(Color.parseColor("#333232"));

    }

    private void setData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(false);
        getBoardList = service.getBoardList();
        getBoardList.enqueue(new Callback<ArrayList<Board>>() {
            @Override
            public void onResponse(Call<ArrayList<Board>> call, Response<ArrayList<Board>> response) {
                switch (response.code()){
                    case 200:
                        arrayList = response.body();
                        adapter = new NeologismRecyclerAdapter(NeologismBoardActivity.this, arrayList);
                        recyclerView.setAdapter(adapter);
                        binding.neologismBoardProgrees.stopAnimation();
                        binding.neologismBoardProgrees.setVisibility(View.GONE);
                        break;
                    default:
                        Toast.makeText(NeologismBoardActivity.this, "서버와의 연동에 문제가 발생했습니다!", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Board>> call, Throwable t) {
                Toast.makeText(NeologismBoardActivity.this, "서버와의 연동에 문제가 발생했습니다!", Toast.LENGTH_SHORT).show();
                Log.e("asdf", t.getMessage());
                finish();
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
