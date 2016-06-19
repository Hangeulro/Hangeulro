package kr.edcan.hangeulro.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import kr.edcan.hangeulro.R;
import kr.edcan.hangeulro.adapter.CommonListViewAdapter;
import kr.edcan.hangeulro.model.CommonData;

public class MainActivity extends AppCompatActivity {

    ArrayList<CommonData> arrayList;
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setDefault();
    }

    private void setDefault() {
        listview = (ListView) findViewById(R.id.main_listview);
        arrayList = new ArrayList<>();
        arrayList.add(new CommonData("사전", R.drawable.ic_dic));
        arrayList.add(new CommonData("내사전", R.drawable.ic_my));
        arrayList.add(new CommonData("신조어 게시판", R.drawable.ic_board));
        arrayList.add(new CommonData("설정", R.drawable.ic_setting));
        CommonListViewAdapter adapter = new CommonListViewAdapter(getApplicationContext(), arrayList);
        listview.setAdapter(adapter);
    }
}
