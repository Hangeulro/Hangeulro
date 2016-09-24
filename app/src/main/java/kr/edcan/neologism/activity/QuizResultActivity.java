package kr.edcan.neologism.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import kr.edcan.neologism.R;
import kr.edcan.neologism.utils.DataManager;
import kr.edcan.neologism.utils.NetworkHelper;
import kr.edcan.neologism.utils.NetworkInterface;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizResultActivity extends AppCompatActivity {

    DataManager manager;
    NetworkInterface service;
    Call<ResponseBody> upscore;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);
        manager = new DataManager(getApplicationContext());
        service = NetworkHelper.getNetworkInstance();
        intent = getIntent();
        int score = intent.getIntExtra("score", 0);
        TextView text = (TextView) findViewById(R.id.text);
        text.setText(score + " 점입니다.");
        if(manager.getActiveUser().first) {
            upscore = service.scoreUp(manager.getActiveUser().second.getToken(), score);
            upscore.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.e("asdf", response.code()+"");
                    switch (response.code()){
                        case 200:
                            Toast.makeText(QuizResultActivity.this, "데이터 저장이 완료되었습니다!", Toast.LENGTH_SHORT).show();
                            finishThisActivity();
                            break;

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(QuizResultActivity.this, "서버와의 연동에 오류가 발생했습니다!", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void finishThisActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 2000);
    }
}
