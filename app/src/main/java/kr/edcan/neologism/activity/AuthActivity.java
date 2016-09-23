package kr.edcan.neologism.activity;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import kr.edcan.neologism.R;
import kr.edcan.neologism.databinding.ActivityAuthBinding;

public class AuthActivity extends AppCompatActivity {

    ActivityAuthBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth);
        setDefault();
    }

    private void setDefault() {
        binding.authIDLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginStr = binding.authIdInput.getText().toString().trim();
                String passwordStr = binding.authPasswordInput.getText().toString().trim();
                if (!loginStr.equals("") && !passwordStr.equals("")) {
                    // launch login
                } else Toast.makeText(AuthActivity.this, "빈칸 없이 입력해주세요!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
