package kr.edcan.neologism.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.google.gson.Gson;

import kr.edcan.neologism.R;
import kr.edcan.neologism.databinding.ActivityDicDetailViewBinding;
import kr.edcan.neologism.model.DicData;

public class DicDetailViewActivity extends AppCompatActivity implements View.OnClickListener {

    private String wordInfoJson;
    private int codeType;
    Intent intent;
    ActivityDicDetailViewBinding binding;
    DicData data;
    String[] titleTextArr = {
            "#사랑",
            "#우스운",
            "#서글픈",
            "#화가 난",
            "#공감되는",
            "#일상생활"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dic_detail_view);
        setDefault();
    }

    private void setDefault() {
        // Toolbar Settings
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Intent to get data from previous activity
        intent = getIntent();
        wordInfoJson = intent.getStringExtra("wordInfo");
        codeType = intent.getIntExtra("codeType", 0);
        data = new Gson().fromJson(wordInfoJson, DicData.class);
        getSupportActionBar().setTitle("");
        binding.toolbar.setTitleTextColor(Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#414142"));
        }

        // Set data to ui
        binding.wordName.setText(data.getWord());
        binding.wordMeaning.setText(data.getMean());
        binding.wordExample.setText(data.getExample());
        binding.addToDic.setOnClickListener(this);
        binding.share.setOnClickListener(this);
        binding.bgImage.setImageResource(new int[]{R.drawable.bg_dic_lovetag, R.drawable.bg_dic_funtag, R.drawable.bg_dic_sadtag, R.drawable.bg_dic_angrytag, R.drawable.bg_dic_satisfiedtag, R.drawable.bg_dic_lifetag}[codeType]);
        binding.wordType.setText(titleTextArr[codeType]);

    }

    private void shareText(String s) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, s);
        startActivity(Intent.createChooser(sharingIntent, "신조어 공유!"));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share:
                break;
            case R.id.addToDic:
                shareText(
                        data.getWord() + " 의 의미는 " + data.getMean() + " 입니다. 더 많은 신조어를 한글을 한글로! 에서 알아보세요! http://iwin247.kr"
                );
                break;
        }
    }
}
