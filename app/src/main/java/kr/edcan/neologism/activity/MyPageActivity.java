package kr.edcan.neologism.activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.login.LoginManager;
import com.twitter.sdk.android.Twitter;

import java.net.MalformedURLException;
import java.util.ArrayList;

import kr.edcan.neologism.R;
import kr.edcan.neologism.adapter.MyPageListViewAdapter;
import kr.edcan.neologism.model.CommonData;
import kr.edcan.neologism.model.User;
import kr.edcan.neologism.utils.DataManager;
import kr.edcan.neologism.utils.ImageSingleTon;
import kr.edcan.neologism.utils.NetworkHelper;
import kr.edcan.neologism.utils.NetworkInterface;
import kr.edcan.neologism.utils.SupportHelper;
import kr.edcan.neologism.views.RoundImageView;
import kr.edcan.neologism.views.SeekArc;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyPageActivity extends AppCompatActivity {

    SupportHelper helper;
    ArrayList<CommonData> arrayList;
    MyPageListViewAdapter adapter;
    SeekArc expProgress;
    ImageView profileBackground;
    RoundImageView profileImageView;
    ListView listView;
    Toolbar toolbar;
    TextView profileName, profileLevel, subText;
    User user;
    NetworkInterface service;
    Call<User> getUserInfo;
    Call<String> destryoUser;
    View headerView;
    DataManager manager;
    int level, point, max=3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_page);
        setActionbar();
        loadUserData();
    }

    private void setActionbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        service = NetworkHelper.getNetworkInstance();
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        getSupportActionBar().setTitle("마이페이지");
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void loadUserData() {
        manager = new DataManager(getApplicationContext());
        user = manager.getActiveUser().second;
        getUserInfo = service.getUserInfo(user.getToken());
        Log.e("asdf", user.getToken());
        getUserInfo.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                switch (response.code()){
                    case 200:
                        level = Integer.parseInt(response.body().getLevel());
                        point = Integer.parseInt(response.body().getPoint());
                        setDefault();
                        setListView();
                        break;
                    default:
                        Toast.makeText(MyPageActivity.this, "서버와의 연결에 오류가 발생했습니다..", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(MyPageActivity.this, "서버와의 연결에 오류가 발생했습니다..", Toast.LENGTH_SHORT).show();
                Log.e("asdf", t.getMessage());
            }
        });
    }

    private void setDefault() {
        helper = new SupportHelper(MyPageActivity.this);
        headerView = getLayoutInflater().inflate(R.layout.mypage_listview_header, null);
        profileName = (TextView) headerView.findViewById(R.id.mypage_profile_name);
        listView = (ListView) findViewById(R.id.myPageListView);
        profileBackground = (ImageView) headerView.findViewById(R.id.mypage_profile_background);
        profileImageView = (RoundImageView) headerView.findViewById(R.id.mypage_profile_image);
        expProgress = (SeekArc) headerView.findViewById(R.id.mypage_show_exp);
        profileLevel = (TextView) headerView.findViewById(R.id.mypage_profile_level);
        subText = (TextView) headerView.findViewById(R.id.mypage_profile_subText);
        profileLevel.setText(level+"");
        expProgress.setMax(3000);
        expProgress.setProgress(point);
        try {
            profileImageView.setImageUrl((user.getUserType() == 0) ? user.getProfileImageUrl() : SupportHelper.convertTwitterImgSize(user.getProfileImageUrl(), 2), ImageSingleTon.getInstance(this).getImageLoader());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        profileImageView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                if (profileImageView.getDrawable() != null) {
                    Bitmap bitmap = ((BitmapDrawable) profileImageView.getDrawable()).getBitmap();
                    if (bitmap != null)
                        profileBackground.setImageBitmap(SupportHelper.blur(bitmap, getApplicationContext()));
                }

            }
        });
        profileName.setText(user.getName());
        subText.setText("Lv."+level+" , 다음 레벨까지 "+(3000-point)+" EXP");
    }

    private void setListView() {
        arrayList = new ArrayList<>();
        arrayList.add(new CommonData("내가 쓴 글 관리",
                "신조어 게시판에 내가 쓴 글을 수정하거나 삭제할 수 있습니다.",
                R.drawable.ic_mypage_boardmanage));
        arrayList.add(new CommonData("내 프로필 수정하기",
                "나의 프로필을 수정할 수 있습니다.",
                R.drawable.ic_mypage_profile));
        arrayList.add(new CommonData("회원 탈퇴",
                "한글을 한글로에 있는 모든 데이터를 삭제하고 회원탈퇴합니다.",
                R.drawable.ic_mypage_leave));
        arrayList.add(new CommonData("로그아웃",
                "이 기기에서 한글을 한글로를 로그아웃합니다..",
                R.drawable.ic_mypage_logout));
        arrayList.add(new CommonData("설정",
                "앱의 세부 설정 및 정보를 확인합니다.",
                R.drawable.ic_mypage_setting));
        adapter = new MyPageListViewAdapter(this, arrayList);
        listView.setAdapter(adapter);
        listView.addHeaderView(headerView);
        listView.setOnItemClickListener(listener);
    }

    ListView.OnItemClickListener listener = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            switch (i) {
                case 1:
                    // 내가 쓴 글 관리
                    break;
                case 2:
                    // 내 프로필 수정하기
                    break;

                case 3:
//                     회원 탈퇴
                    helper.showAlertDialog("회원탈퇴", "한글을 한글로!에서 완전히 탈퇴합니다", new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                            destryoUser = service.destroyUser(manager.getActiveUser().second.getId(), manager.getActiveUser().second.getUserType());
//                            destryoUser.enqueue(new Callback<String>() {
//                                @Override
//                                public void onResponse(Call<String> call, Response<String> response) {
//                                    switch (response.code()) {
//                                        case 200:
//                                            Toast.makeText(MyPageActivity.this, "정상적으로 탈퇴되셨습니다.", Toast.LENGTH_SHORT).show();
//                                            break;
//                                    }
//                                    Log.e("asdf", response.code() + "");
//                                }
//
//                                @Override
//                                public void onFailure(Call<String> call, Throwable t) {
//                                    Log.e("asdf", t.getMessage());
//
//                                }
//                            });
                            LoginManager.getInstance().logOut();
                            manager.removeAllData();
                            startActivity(new Intent(getApplicationContext(), AuthActivity.class));
                            finish();
                            MainActivity.finishThis();
                        }
                    });
                    break;
                case 4:
                    // 로그아웃
                    helper.showAlertDialog("로그아웃", "한글을 한글로에서 로그아웃하시겠습니까?.", new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            LoginManager.getInstance().logOut();
                            Twitter.getSessionManager().clearActiveSession();
                            Twitter.logOut();
                            manager.removeAllData();
                            finish();
                            startActivity(new Intent(getApplicationContext(), AuthActivity.class));
                            MainActivity.finishThis();

                        }
                    });
                    break;
                case 5:
                    // 설정
                    break;
            }
        }
    };

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