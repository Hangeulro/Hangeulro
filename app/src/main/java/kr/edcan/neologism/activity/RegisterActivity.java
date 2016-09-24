package kr.edcan.neologism.activity;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import kr.edcan.neologism.R;
import kr.edcan.neologism.databinding.ActivityRegisterBinding;
import kr.edcan.neologism.utils.NetworkHelper;
import kr.edcan.neologism.utils.NetworkInterface;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {


    Call<ResponseBody> call;
    NetworkInterface service;
    ActivityRegisterBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = NetworkHelper.getNetworkInstance();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        setDefault();
    }

    private void setDefault() {
        binding.authIDLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call = service.userRegister(binding.authIdInput.getText().toString().trim(), binding.authPasswordInput.getText().toString().trim(), binding.authNameInput.getText().toString().trim());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        switch (response.code()){
                            case 200:
                                Toast.makeText(RegisterActivity.this, "회원가입이 완료되었습니다!", Toast.LENGTH_SHORT).show();
                                finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("asdf", t.getMessage());
                    }
                });
            }
        });
    }
}
