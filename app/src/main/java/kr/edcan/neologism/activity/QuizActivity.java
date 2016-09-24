package kr.edcan.neologism.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import kr.edcan.neologism.R;
import kr.edcan.neologism.adapter.CommonListViewAdapter;
import kr.edcan.neologism.adapter.QuizListViewAdapter;
import kr.edcan.neologism.databinding.ActivityQuizBinding;
import kr.edcan.neologism.model.CommonData;
import kr.edcan.neologism.model.QuizData;
import kr.edcan.neologism.utils.DataManager;
import kr.edcan.neologism.utils.NetworkHelper;
import kr.edcan.neologism.utils.NetworkInterface;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizActivity extends AppCompatActivity {


    DataManager manager;
    Call<ResponseBody> getUserInfo;
    NetworkInterface service;
    ActivityQuizBinding binding;
    ArrayList<QuizData> arrayList;
    JSONObject result = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quiz);
        setData();
    }

    private void setData() {
        manager = new DataManager(getApplicationContext());
        service = NetworkHelper.getNetworkInstance();
        getUserInfo = service.getUserInfo(manager.getActiveUser().second.getToken());
        getUserInfo.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                switch (response.code()){
                    case 200:
                        try {
                            result = new JSONObject(response.body().string());
                        } catch (JSONException e) {
                            Log.e("asdf", e.getMessage());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        setDefault();
                        break;
                    case 401:
                        Toast.makeText(QuizActivity.this, "서버오류입니다. 다시 시도해주세요!", Toast.LENGTH_SHORT).show();
                        Log.e("asdf", "401");
                        break;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("asdf", t.getMessage());
            }
        });
    }
    private void setDefault() {
        arrayList = new ArrayList<>();
        try {
            arrayList.add(new QuizData("현재 레벨", "Now Level", result.getString("level"), R.drawable.ic_quizmain_l));
            arrayList.add(new QuizData("포인트", "Point", result.getString("point"),R.drawable.ic_quizmain_p));
            arrayList.add(new QuizData("누적 포인트", "Total Point", result.getString("point"),R.drawable.ic_quizmain_t));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        arrayList.add(new QuizData("게임 초기화", "Game Reset", "",R.drawable.ic_quizmain_r));
        binding.quizListview.setAdapter(new QuizListViewAdapter(getApplicationContext(), arrayList));
        binding.quizStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), QuizViewActivity.class));
                finish();
            }
        });
    }

}
