package kr.edcan.neologism.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import kr.edcan.neologism.R;
import kr.edcan.neologism.adapter.NeoBoardCommentListViewAdapter;
import kr.edcan.neologism.databinding.ActivityNeologismBoardViewBinding;
import kr.edcan.neologism.databinding.NeologismBoardFooterBinding;
import kr.edcan.neologism.databinding.NeologismBoardListcontentBinding;
import kr.edcan.neologism.model.Board;
import kr.edcan.neologism.utils.ImageSingleTon;
import kr.edcan.neologism.utils.NetworkHelper;
import kr.edcan.neologism.utils.NetworkInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NeologismBoardViewActivity extends AppCompatActivity {

    String boardid;
    Intent intent;
    ListView listView;
    NetworkInterface service;
    ActivityNeologismBoardViewBinding binding;
    Call<Board> getBoardInfo;
    Board data;
    ArrayList<Board.Comments> commentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_neologism_board_view);
        setAppbarLayout();
        setDefault();
    }

    private void setAppbarLayout() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("게시글 상세보기");
        binding.toolbar.setTitleTextColor(Color.WHITE);
        binding.toolbar.setBackgroundColor(Color.parseColor("#707070"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(Color.parseColor("#464545"));
    }

    private void setDefault() {
        intent = getIntent();
        boardid = intent.getStringExtra("boardid");
        Log.e("asdf", boardid);
        service = NetworkHelper.getNetworkInstance();
        getBoardInfo = service.getBoardInfo(boardid);
        getBoardInfo.enqueue(new Callback<Board>() {
            @Override
            public void onResponse(Call<Board> call, Response<Board> response) {
                switch (response.code()) {
                    case 200:
                        data = response.body();
                        commentList = response.body().getComments();
                        setUI();
                        break;
                    default:
                        Toast.makeText(NeologismBoardViewActivity.this, "서버와의 연동에 문제가 발생했습니다!", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                }
            }


            @Override
            public void onFailure(Call<Board> call, Throwable t) {
                Toast.makeText(NeologismBoardViewActivity.this, "서버와의 연동에 문제가 발생했습니다!", Toast.LENGTH_SHORT).show();
                finish();
                Log.e("asdf", t.getMessage());
            }
        });


    }

    private void setUI() {
        listView = binding.neologismBoardViewListView;
        NeologismBoardListcontentBinding header = DataBindingUtil.inflate(getLayoutInflater(), R.layout.neologism_board_listcontent, null, false);
        header.neologismContentProfile.setImageUrl(data.getWriter_profile(), ImageSingleTon.getInstance(getApplicationContext()).getImageLoader());
        header.neologismContentDate.setText(data.getWriter() + " - " + data.getDate().toLocaleString());
        header.neologismContentTitle.setText(data.getTitle());
        header.neologismContentText.setText(data.getContents());
        if (!data.getImageurl().equals("null")) {
            header.neologismContentImage.setVisibility(View.VISIBLE);
            header.neologismContentImage.setImageUrl(data.getImageurl(), ImageSingleTon.getInstance(getApplicationContext()).getImageLoader());
        } else header.neologismContentImage.setVisibility(View.GONE);
        header.neologismContentLikeCount.setText(data.getGood() + "");
        header.neologismContentUnLikeCount.setText(data.getBad() + "");
        header.neologismContentCommentInfo.setText("댓글 " + data.getComments().size());
        NeologismBoardFooterBinding footer =  DataBindingUtil.inflate(getLayoutInflater(), R.layout.neologism_board_footer, null, false);
        footer.boardFooterWriteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(NeologismBoardViewActivity.this, "", Toast.LENGTH_SHORT).show();
            }
        });
        listView.setAdapter(new NeoBoardCommentListViewAdapter(getApplicationContext(), commentList));
        listView.addHeaderView(header.getRoot());
        listView.addFooterView(footer.getRoot());
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
