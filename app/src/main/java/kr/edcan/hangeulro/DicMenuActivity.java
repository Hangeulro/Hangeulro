package kr.edcan.hangeulro;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import kr.edcan.hangeulro.adapter.CommonListViewAdapter;
import kr.edcan.hangeulro.adapter.DicListViewAdapter;
import kr.edcan.hangeulro.databinding.ActivityDicMenuBinding;
import kr.edcan.hangeulro.model.CommonData;

public class DicMenuActivity extends AppCompatActivity {

    ActivityDicMenuBinding binding;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dic_menu);
        setDefault();
    }

    private void setDefault() {
        listView = binding.dicMenuListView;
        ArrayList<CommonData> arrayList  = new ArrayList<>();
        arrayList.add(new CommonData("사랑 코드", "Love Code",  R.drawable.ic_dic_love));
        arrayList.add(new CommonData("우스운 코드", "Funny Code",  R.drawable.ic_dic_funny));
        arrayList.add(new CommonData("서글픈 코드", "Sad Code",  R.drawable.ic_dic_sad));
        arrayList.add(new CommonData("화가 난 코드", "Angry Code",  R.drawable.ic_dic_angry));
        arrayList.add(new CommonData("공감 코드", "Sympathy Code",  R.drawable.ic_dic_sympathy));
        arrayList.add(new CommonData("일상생활 코드", "Lifestyle Code",  R.drawable.ic_dic_lifestyle));
        DicListViewAdapter adapter = new DicListViewAdapter(getApplicationContext(), arrayList);
        listView.setAdapter(adapter);
    }
}
