package kr.edcan.hangeulro.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import kr.edcan.hangeulro.R;
import kr.edcan.hangeulro.databinding.ActivityDicDetailViewBinding;

public class DicDetailViewActivity extends AppCompatActivity {

    private String title, meaning, example;
    Intent intent;
    ActivityDicDetailViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dic_detail_view);
        setDefault();
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
                shareText(title + "의 뜻은 " + meaning + " #한글을_한글로 https://goo.gl/3eB5Pd");
            }
        });

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
