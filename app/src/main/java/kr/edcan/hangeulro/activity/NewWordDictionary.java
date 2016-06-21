package kr.edcan.hangeulro.activity;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import kr.edcan.hangeulro.R;
import kr.edcan.hangeulro.adapter.CommonListViewAdapter;
import kr.edcan.hangeulro.adapter.CommonRecyclerAdapter;
import kr.edcan.hangeulro.model.CommonRecycleData;
import kr.edcan.hangeulro.model.CommonRecycleData;

public class NewWordDictionary extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_word_dictionary);
        setAppbarLayout();
        setDefault();
    }


    private void setDefault() {
        recyclerview = (RecyclerView) findViewById(R.id.newworddic_recyclerview);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        recyclerview.setHasFixedSize(false);
        recyclerview.setLayoutManager(manager);
        ArrayList<CommonRecycleData> arrayList = new ArrayList<>();
        arrayList.add(new CommonRecycleData("", "", "", true));
        arrayList.add(new CommonRecycleData("빼박캔트", "빼도박도 못한다라는 말로 줄임 합성어입니다.", "빼도박도 못한다", false));
        arrayList.add(new CommonRecycleData("ㅇㅈ", "인정의 초성을딴 축약어 입니다.", "인정", false));
        arrayList.add(new CommonRecycleData("쩐다", "멋있다,굉장하다 라는 뜻을 가지고 잇는 외래어", "멋있다", false));
        arrayList.add(new CommonRecycleData("크리", "상황이 최상이거나 최악의 극단의상황으로 가는 경우에 사용", "치명타", false));
        arrayList.add(new CommonRecycleData("지못미", "지켜주지 못해 미안해의 줄임말", "지켜주지 못해 미안해", false));
        arrayList.add(new CommonRecycleData("제곧내", "제목이 곧 내용이라는 뜻으로 특별한 내용이 없을때 사용", "제목이 곧 내용", false));
        arrayList.add(new CommonRecycleData("선린엔터", "선린과 드림엔터의 합성어", "선린혁신센터", false));
        CommonRecyclerAdapter adapter = new CommonRecyclerAdapter(getApplicationContext(), arrayList);
        recyclerview.setAdapter(adapter);
    }

    private void setAppbarLayout() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        getSupportActionBar().setTitle("신조어 사전");
        getSupportActionBar().setElevation(5);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable drawable = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        drawable.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(drawable);
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

