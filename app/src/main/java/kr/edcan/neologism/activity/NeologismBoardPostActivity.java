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
                startActivityForResult(i, RESULT_LOAD_IMAGE);//위에서 선언한 1이라는 결과 코드로 액티비티를 선언
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            Log.e("파일 경로", picturePath);
            cursor.close();
            binding.neologismPostPreview.setImageBitmap(BitmapFactory.decodeFile(picturePath));//이미지뷰에 뿌려줍니다.
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.postactivity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.post:
                postArticle();
        }
        return super.onOptionsItemSelected(item);
    }

    private void postArticle() {
        File file = new File(picturePath);
        RequestBody body = RequestBody.create(MediaType.parse("image/png"), file);
        RequestBody token = RequestBody.create(MediaType.parse("text/plain"), manager.getActiveUser().second.getToken());
        RequestBody title = RequestBody.create(MediaType.parse("text/plain"), binding.neologismPostEditTitle.getText().toString().trim());
        RequestBody contents= RequestBody.create(MediaType.parse("text/plain"), editText.getText().toString().trim());
        Log.e("asdf",body.contentType().toString());
        postArticle = service.postArticle(body, token, title, contents);
        postArticle.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("asdf", response.code()+"");
                switch (response.code()){
                    case 200:
                        Toast.makeText(NeologismBoardPostActivity.this, "완료", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("asdf", t.getMessage());
            }
        });

    }
}
