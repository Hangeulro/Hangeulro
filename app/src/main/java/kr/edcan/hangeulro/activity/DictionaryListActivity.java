package kr.edcan.hangeulro.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import kr.edcan.hangeulro.R;
import kr.edcan.hangeulro.adapter.DictionaryListViewAdapter;
import kr.edcan.hangeulro.model.CommonData;

public class DictionaryListActivity extends AppCompatActivity {

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_list);
        setDefault();
    }

    private void setDefault() {
        listView = (ListView) findViewById(R.id.dictionarylist_listview);
        ArrayList<CommonData> arrayList = new ArrayList<>();
        arrayList.add(new CommonData("사랑", R.drawable.btn_love));
        arrayList.add(new CommonData("웃음", R.drawable.btn_smile));
        arrayList.add(new CommonData("슬픔", R.drawable.btn_sad));
        arrayList.add(new CommonData("화남", R.drawable.btn_angry));
        arrayList.add(new CommonData("긍정", R.drawable.btn_postive));
        arrayList.add(new CommonData("부러움", R.drawable.btn_burub));
        DictionaryListViewAdapter adapter = new DictionaryListViewAdapter(getApplicationContext(), arrayList);
        listView.setAdapter(adapter);
    }
}
