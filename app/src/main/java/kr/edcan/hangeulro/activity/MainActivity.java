package kr.edcan.hangeulro.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import kr.edcan.hangeulro.R;
import kr.edcan.hangeulro.adapter.CommonListViewAdapter;
import kr.edcan.hangeulro.databinding.ActivityMainBinding;
import kr.edcan.hangeulro.databinding.MainListviewFooterBinding;
import kr.edcan.hangeulro.model.CommonData;
import kr.edcan.hangeulro.utils.ClipBoardService;

public class MainActivity extends AppCompatActivity {


    ActivityMainBinding binding;
    ArrayList<CommonData> arrayList;
    ListView listview;
    public static int OVERLAY_PERMISSION_REQ_CODE = 5858;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            setPackage();
        setDefault();
        startService(new Intent(MainActivity.this, ClipBoardService.class));
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void setPackage() {
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(MainActivity.this, "한글을 한글로!를 실행하기 위해 권한을 허용해주세요!", Toast.LENGTH_SHORT).show();
                setPackage();
            }
        }
    }

    private void setDefault() {
        listview = binding.mainListView;
        arrayList = new ArrayList<>();
        arrayList.add(new CommonData("신조어 사전", "Neologism Dictionary", R.drawable.ic_main_dic));
        arrayList.add(new CommonData("내 사전", "My Dictionary", R.drawable.ic_main_mydic));
        arrayList.add(new CommonData("누리꾼 게시판", "Netizen Bulletin Board", R.drawable.ic_main_board));
        arrayList.add(new CommonData("신조어 퀴즈", "Neologism Quiz", R.drawable.ic_main_battle));
        CommonListViewAdapter adapter = new CommonListViewAdapter(getApplicationContext(), arrayList);
        listview.setAdapter(adapter);
        MainListviewFooterBinding footerBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.main_listview_footer, null, false);
        listview.addFooterView(footerBinding.getRoot());
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        startActivity(new Intent(getApplicationContext(), DicMenuActivity.class));
                        break;
                }
            }
        });
    }
}
