package kr.edcan.neologism.activity;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

import kr.edcan.neologism.R;
import kr.edcan.neologism.adapter.MyDicListViewAdapter;
import kr.edcan.neologism.databinding.ActivityMyDicBinding;
import kr.edcan.neologism.model.CommonData;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class MyDicActivity extends AppCompatActivity {

    ActivityMyDicBinding binding;
    ListView listView;
    Call<ResponseBody> getMyDicList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_dic);
        setDefault();
        setAppbarLayout();
    }

    private void setAppbarLayout() {
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setBackgroundColor(Color.parseColor("#DD4513"));
        binding.toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setDefault() {
        listView = binding.mydicListView;
        ArrayList<CommonData> arrayList = new ArrayList<>();
        arrayList.add(new CommonData("이상해 코드", "Perculiar Code", R.drawable.ic_mydic_gold));
        arrayList.add(new CommonData("이상해 코드", "Perculiar Code", R.drawable.ic_mydic_gold));
        arrayList.add(new CommonData("이상해 코드", "Perculiar Code", R.drawable.ic_mydic_gold));
        arrayList.add(new CommonData("이상해 코드", "Perculiar Code", R.drawable.ic_mydic_gold));
        arrayList.add(new CommonData("이상해 코드", "Perculiar Code", R.drawable.ic_mydic_gold));
        arrayList.add(new CommonData("이상해 코드", "Perculiar Code", R.drawable.ic_mydic_gold));
        listView.setAdapter(new MyDicListViewAdapter(getApplicationContext(), arrayList));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
