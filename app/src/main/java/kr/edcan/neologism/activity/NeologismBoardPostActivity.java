package kr.edcan.neologism.activity;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.Date;

import kr.edcan.neologism.R;
import kr.edcan.neologism.databinding.ActivityNeologismBoardPostBinding;
import kr.edcan.neologism.utils.DataManager;
import kr.edcan.neologism.utils.NetworkHelper;
import kr.edcan.neologism.utils.NetworkInterface;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NeologismBoardPostActivity extends AppCompatActivity {

    private boolean isFileSelected = false;
    private static int RESULT_LOAD_IMAGE = 6974;
    private String picturePath = "";
    Toolbar toolbar;
    EditText editText;
    ActivityNeologismBoardPostBinding binding;
    DataManager manager;
    Call<ResponseBody> postArticle;
    NetworkInterface service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_neologism_board_post);
        setDefault();
        setAppbarLayout();
    }

    private void setAppbarLayout() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setTitleTextColor(Color.WHITE);
        binding.toolbar.setBackgroundColor(Color.parseColor("#5A5A5A"));
        getSupportActionBar().setTitle("게시글 작성하기");

    }

    private void setDefault() {
        manager = new DataManager(getApplicationContext());
        editText = binding.neologismPostEdittext;
        service = NetworkHelper.getNetworkInstance();
        binding.neologismPostPreview.setScaleType(ImageView.ScaleType.CENTER_CROP);
        binding.neologismPostExecute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            isFileSelected = true;
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
            binding.neologismPostPreview.setImageBitmap(BitmapFactory.decodeFile(picturePath));//이미지뷰에 뿌려줍니다.
        } else isFileSelected = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.postactivity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.post:
                postArticle();
        }
        return super.onOptionsItemSelected(item);
    }

    private void postArticle() {
        String titleStr = binding.neologismPostEditTitle.getText().toString().trim();
        String contentStr = editText.getText().toString().trim();
        if (!titleStr.equals("") && !contentStr.equals("")) {
            RequestBody imageBody = null;
            File file = null;
            if (isFileSelected) {
                file = new File(picturePath);
                imageBody = RequestBody.create(MediaType.parse("image/png"), file);
            }
            RequestBody token = RequestBody.create(MediaType.parse("text/plain"), manager.getActiveUser().second.getToken());
            RequestBody title = RequestBody.create(MediaType.parse("text/plain"), titleStr);
            RequestBody contents = RequestBody.create(MediaType.parse("text/plain"), contentStr);
            postArticle = service.postArticle((isFileSelected && file != null) ? imageBody : null, token, title, contents);
            postArticle.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    switch (response.code()) {
                        case 200:
                            Toast.makeText(NeologismBoardPostActivity.this, "게시가 완료되었습니다!", Toast.LENGTH_SHORT).show();
                            finish();
                            break;
                        default:
                            Toast.makeText(NeologismBoardPostActivity.this, "서버와의 연동에 문제가 발생했습니다", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(NeologismBoardPostActivity.this, "서버와의 연동에 문제가 발생했습니다", Toast.LENGTH_SHORT).show();
                }
            });
        } else
            Toast.makeText(NeologismBoardPostActivity.this, "필드를 전부 입력해주세요!", Toast.LENGTH_SHORT).show();
    }
}
