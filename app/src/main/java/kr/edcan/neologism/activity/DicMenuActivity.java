package kr.edcan.neologism.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import kr.edcan.neologism.R;
import kr.edcan.neologism.adapter.DicListViewAdapter;
import kr.edcan.neologism.databinding.ActivityDicMenuBinding;
import kr.edcan.neologism.model.CommonData;

public class DicMenuActivity extends AppCompatActivity {

    Toolbar toolbar;

    ActivityDicMenuBinding binding;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dic_menu);
        setAppbarLayout();
        setDefault();
    }

    private void setAppbarLayout() {
        toolbar = binding.toolbar;
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setDefault() {
        listView = binding.dicMenuListView;
        ArrayList<CommonData> arrayList = new ArrayList<>();
        arrayList.add(new CommonData("사랑 코드", "Love Code", R.drawable.ic_dic_love));
        arrayList.add(new CommonData("유머 코드", "Funny Code", R.drawable.ic_dic_funny));
        arrayList.add(new CommonData("우울 코드", "Sad Code", R.drawable.ic_dic_sad));
        arrayList.add(new CommonData("분노 코드", "Angry Code", R.drawable.ic_dic_angry));
        arrayList.add(new CommonData("공감 코드", "Sympathy Code", R.drawable.ic_dic_sympathy));
        arrayList.add(new CommonData("일상생활 코드", "Lifestyle Code", R.drawable.ic_dic_lifestyle));
        DicListViewAdapter adapter = new DicListViewAdapter(getApplicationContext(), arrayList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(), DicViewActivity.class)
                        .putExtra("codeType", position));
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
