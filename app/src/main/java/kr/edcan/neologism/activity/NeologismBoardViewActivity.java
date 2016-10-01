package kr.edcan.neologism.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.Date;

import kr.edcan.neologism.R;
import kr.edcan.neologism.adapter.NeoBoardCommentListViewAdapter;
import kr.edcan.neologism.databinding.ActivityNeologismBoardViewBinding;
import kr.edcan.neologism.databinding.NeologismBoardFooterBinding;
import kr.edcan.neologism.databinding.NeologismBoardListcontentBinding;
import kr.edcan.neologism.model.Board;
import kr.edcan.neologism.model.Word;
import kr.edcan.neologism.utils.DataManager;
import kr.edcan.neologism.utils.ImageSingleTon;
import kr.edcan.neologism.utils.NetworkHelper;
import kr.edcan.neologism.utils.NetworkInterface;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NeologismBoardViewActivity extends AppCompatActivity {

    DataManager manager;
    String boardid;
    Intent intent;
    ListView listView;
    NetworkInterface service;
    ActivityNeologismBoardViewBinding binding;
    Call<Board> getBoardInfo;
    Board data;
    ArrayList<Board.Comments> commentList = new ArrayList<>();
    NeoBoardCommentListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_neologism_board_view);
        setAppbarLayout();
        setDefault(false);
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

    private void setDefault(final boolean append) {
        if (!append) {
            manager = new DataManager(getApplicationContext());
            intent = getIntent();
            boardid = intent.getStringExtra("boardid");
            Log.e("asdf", boardid);
            service = NetworkHelper.getNetworkInstance();
        }
        getBoardInfo = service.getBoardInfo(boardid);
        getBoardInfo.enqueue(new Callback<Board>() {
            @Override
            public void onResponse(Call<Board> call, Response<Board> response) {
                switch (response.code()) {
                    case 200:
                        data = response.body();
                        if (append) commentList.clear();
                        for (Board.Comments c : response.body().getComments()) {
                            commentList.add(c);
                        }
                        setUI(append);
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

    private void setUI(boolean append) {
        if (!append) {
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
            NeologismBoardFooterBinding footer = DataBindingUtil.inflate(getLayoutInflater(), R.layout.neologism_board_footer, null, false);
            footer.boardFooterWriteLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showCommentDialog();
                }
            });
            listView.addHeaderView(header.getRoot());
            listView.addFooterView(footer.getRoot());
            adapter = new NeoBoardCommentListViewAdapter(getApplicationContext(), commentList);
            listView.setAdapter(adapter);
        } else runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void showCommentDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.common_dialog_input_view, null);
        TextView title = (TextView) view.findViewById(R.id.dialog_title);
        final EditText editText = (EditText) view.findViewById(R.id.dialog_edittext);
        final EditText editTextSum = (EditText) view.findViewById(R.id.dialog_edittext_summary);
        editTextSum.setVisibility(View.GONE);
        title.setText(manager.getActiveUser().second.getName() + " 으로 댓글 작성");
        new MaterialDialog.Builder(NeologismBoardViewActivity.this)
                .customView(view, false)
                .positiveColor(getResources().getColor(R.color.colorPrimary))
                .positiveText("댓글 달기")
                .negativeText("취소")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        final String result = editText.getText().toString().trim();
                        if (result.equals("")) {
                            Toast.makeText(NeologismBoardViewActivity.this, "댓글을 입력해주세요!", Toast.LENGTH_SHORT).show();
                            editText.setText("");
                        } else {
                            Call<ResponseBody> addCommentToBoard = service.addCommentToBoard(manager.getActiveUser().second.getToken(), boardid, result, new Date(System.currentTimeMillis()));
                            addCommentToBoard.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    switch (response.code()) {
                                        case 200:
                                            Toast.makeText(NeologismBoardViewActivity.this, "댓글을 작성했습니다!", Toast.LENGTH_SHORT).show();
                                            setDefault(true);
                                            break;
                                        default:
                                            Toast.makeText(NeologismBoardViewActivity.this, "서버와의 연동에 오류가 발생했습니다!", Toast.LENGTH_SHORT).show();
                                            break;
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Log.e("asdf", t.getMessage());
                                    Toast.makeText(NeologismBoardViewActivity.this, "서버와의 연동에 오류가 발생했습니다!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                })
                .show();
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
