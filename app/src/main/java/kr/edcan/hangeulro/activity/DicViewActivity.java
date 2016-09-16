package kr.edcan.hangeulro.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import kr.edcan.hangeulro.R;
import kr.edcan.hangeulro.databinding.ActivityDicMenuBinding;
import kr.edcan.hangeulro.databinding.ActivityDicViewBinding;

public class DicViewActivity extends AppCompatActivity {
    int codeType;
    Toolbar toolbar;
    RecyclerView recyclerView;
    ActivityDicViewBinding binding;
    Intent intent;
    String[] titleTextArr = {
            "사랑 코드",
            "우스운 코드",
            "서글픈 코드",
            "화가 난 코드",
            "공감 코드",
            "일상생활 코드"
    };
    String titleTextEngArr[] = {
            "Love",
            "Funny",
            "Sad",
            "Angry",
            "Sympathy",
            "Lifestyle"
    };
    int actionbarColor[] = {R.color.loveActionbarColor, R.color.funActionbarColor, R.color.sadActionbarColor, R.color.angryActionbarColor, R.color.symActionbarColor, R.color.lifeActionbarColor};
    int statusbarColor[] = {R.color.loveStatusColor, R.color.funStatusColor, R.color.sadStatusColor, R.color.angryStatusColor, R.color.symStatusColor, R.color.lifeStatusColor};
    int mainColor[] = {R.color.loveThemeColor, R.color.funThemeColor, R.color.sadThemeColor, R.color.angryThemeColor, R.color.symThemeColor, R.color.lifeThemeColor};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dic_view);
        setDefault();
    }

    private void setDefault() {
        intent = getIntent();
        codeType = intent.getIntExtra("codeType", -1);
        String titleText = titleTextArr[codeType];
        toolbar = binding.toolbar;
        toolbar.setTitle(titleText);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("신조어사전 | " + titleTextArr[codeType]);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = binding.dicViewRecyclerView;
        binding.dicViewHeadeBG.setBackgroundColor(getResources().getColor(mainColor[codeType]));
        binding.dicViewType.setText(titleTextEngArr[codeType] + " Code");
        toolbar.setBackgroundColor(getResources().getColor(actionbarColor[codeType]));
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(getResources().getColor(statusbarColor[codeType]));
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
