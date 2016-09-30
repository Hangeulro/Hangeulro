package kr.edcan.neologism.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import kr.edcan.neologism.R;
import kr.edcan.neologism.adapter.CommonListViewAdapter;
import kr.edcan.neologism.adapter.NeologismRecyclerAdapter;
import kr.edcan.neologism.databinding.ActivityMainBinding;
import kr.edcan.neologism.databinding.MainListviewFooterBinding;
import kr.edcan.neologism.model.CommonData;
import kr.edcan.neologism.utils.ClipBoardService;
import kr.edcan.neologism.utils.DBSync;

public class MainActivity extends AppCompatActivity {

    public static Activity activity = null;
    public static void finishThis(){
        if(activity != null) activity.finish();
    };
    ActivityMainBinding binding;
    ArrayList<CommonData> arrayList;
    ListView listview;
    ViewPager mainPager;
    public static int OVERLAY_PERMISSION_REQ_CODE = 5858;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            setPackage();
        setDefault();
        startService(new Intent(MainActivity.this, ClipBoardService.class));
        DBSync.syncDB(getApplicationContext());
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
        mainPager = binding.mainHeader;
        mainPager.setAdapter(new PagerAdapterClass(getApplicationContext()));
        mainPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        setViewPagerAutoScroll();
        arrayList = new ArrayList<>();
        arrayList.add(new CommonData("신조어 사전", "Neologism Dictionary", R.drawable.ic_main_dic));
        arrayList.add(new CommonData("내 사전", "My Dictionary", R.drawable.ic_main_mydic));
        arrayList.add(new CommonData("신조어 게시판", "Neologism Bulletin Board", R.drawable.ic_main_board));
        arrayList.add(new CommonData("신조어 퀴즈", "Neologism Quiz", R.drawable.ic_main_battle));
        CommonListViewAdapter adapter = new CommonListViewAdapter(getApplicationContext(), arrayList);
        listview.setAdapter(adapter);
        MainListviewFooterBinding footerBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.main_listview_footer, null, false);
        listview.addFooterView(footerBinding.getRoot());
        footerBinding.mainListviewFooterMyPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MyPageActivity.class));
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(getApplicationContext(), DicMenuActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(getApplicationContext(), MyDicActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(getApplicationContext(), NeologismBoardActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(getApplicationContext(), QuizActivity.class));
                        break;
//
//                    default:
//                        Toast.makeText(MainActivity.this, "업데이트 후 적용될 예정입니다!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setViewPagerAutoScroll() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mainPager.setCurrentItem((mainPager.getCurrentItem() < 2) ? mainPager.getCurrentItem() + 1 : 0, true);
                setViewPagerAutoScroll();
            }
        }, 3000);
    }

    /**
     * PagerAdapter
     */
    private class PagerAdapterClass extends PagerAdapter {

        private LayoutInflater mInflater;

        public PagerAdapterClass(Context c) {
            super();
            mInflater = LayoutInflater.from(c);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Object instantiateItem(View pager, int position) {
            View v = null;
            if (position == 0) {
                v = mInflater.inflate(R.layout.main_pager0, null);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(), DicMenuActivity.class));
                    }
                });
            } else if (position == 1) {
                v = mInflater.inflate(R.layout.main_pager1, null);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(), TodayNeologismActivity.class));
                    }
                });
            } else {
                v = mInflater.inflate(R.layout.main_pager2, null);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(), SelectBestActivity.class));
                    }
                });
            }

            ((ViewPager) pager).addView(v, 0);

            return v;
        }

        @Override
        public void destroyItem(View pager, int position, Object view) {
            ((ViewPager) pager).removeView((View) view);
        }

        @Override
        public boolean isViewFromObject(View pager, Object obj) {
            return pager == obj;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }

        @Override
        public void finishUpdate(View arg0) {
        }
    }


}
