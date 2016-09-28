package kr.edcan.neologism.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import kr.edcan.neologism.R;
import kr.edcan.neologism.databinding.ActivityDicDetailViewBinding;

public class DicDetailViewActivity extends AppCompatActivity {

    private String title, meaning, example;
    Intent intent;
    ActivityDicDetailViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackgroundWindow();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dic_detail_view);
        setDefault();
    }


    private void setBackgroundWindow() {
        WindowManager.LayoutParams windowManager = getWindow().getAttributes();
        windowManager.dimAmount = 0.88f;
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#77B7B7B7")));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }
    private void setDefault() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("신조어 상세보기");
        binding.toolbar.setTitleTextColor(Color.WHITE);
        intent = getIntent();
        title = intent.getStringExtra("title");
        meaning = intent.getStringExtra("meaning");
        example = intent.getStringExtra("example");
        binding.dicDetailTitle.setText(title);
        binding.dicDetailMeaning.setText(meaning);
        binding.dicDetailExample.setText(example);
        binding.dicDetailShare.setText("\"" + title + "\"라는 단어의 뜻을\n다른 친구들과 함께 공유해보세요!");
        binding.dicDetailShareLaunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareText(title + "의 뜻은 " + meaning + " #한글을_한글로 https://goo.gl/StvRXG");
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#414142"));
        }

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
}
