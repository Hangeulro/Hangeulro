package kr.edcan.neologism.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import kr.edcan.neologism.R;
import kr.edcan.neologism.databinding.ActivityAuthBinding;
import kr.edcan.neologism.model.User;
import kr.edcan.neologism.utils.DataManager;
import kr.edcan.neologism.utils.NetworkHelper;
import kr.edcan.neologism.utils.NetworkInterface;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;


public class AuthActivity extends AppCompatActivity implements View.OnClickListener {

    DataManager dataManager;
    ActivityAuthBinding binding;
    Call<ResponseBody> userLogin, autoLogin, twitterLogin, facebookLogin;
    NetworkInterface service;
    CallbackManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth);
        setDefault();
        validateUserToken();
    }

    private void validateUserToken() {
        Pair<Boolean, User> userPair = dataManager.getActiveUser();
        if (userPair.first == false) {
            setTwitterCallback();
            setFacebookCallback();
        } else {
            // validate
            switch (userPair.second.getUserType()) {
                case 0:
                    new LoadFacebookInfo().execute(dataManager.getFacebookUserCredential());
                    break;
                case 1:
                    new LoadTwitterInfo().execute(dataManager.getTwitterUserCredentials());
                    break;
                case 2:
                    // Kakao
                    break;
                case 3:
                    // Naver
                    break;
                case 4:
                    new LoadNativeUserInfo().execute(dataManager.getActiveUser().second.getToken());
            }
        }
    }

    private void setDefault() {
        dataManager = new DataManager(getApplicationContext());
        manager = CallbackManager.Factory.create();
        String permissions[] = new String[]{"email", "user_about_me", "user_friends"};
        binding.authFacebookLaunch.setReadPermissions(permissions);
        service = NetworkHelper.getNetworkInstance();
        binding.authIDLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginStr = binding.authIdInput.getText().toString().trim();
                String passwordStr = binding.authPasswordInput.getText().toString().trim();
                if (!loginStr.equals("") && !passwordStr.equals("")) {
                    userLogin = service.userLogin(loginStr, passwordStr);
                    userLogin.enqueue(new retrofit2.Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            switch (response.code()) {
                                case 200:
                                    try {
                                        dataManager.saveNativeLoginUserInfo(new JSONObject(response.body().string()));
                                        Toast.makeText(AuthActivity.this, dataManager.getActiveUser().second.getName() + " 님 환영합니다!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        finish();
                                    } catch (JSONException e) {
                                        Log.e("asdf", e.getMessage());
                                    } catch (IOException e) {
                                        Log.e("asdf", e.getMessage());
                                        e.printStackTrace();
                                    }
                                    break;
                                case 400:
                                    Toast.makeText(AuthActivity.this, "아이디 혹은 비밀번호가 잘못되었습니다!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("asdf", t.getMessage());
                        }
                    });
                } else
                    Toast.makeText(AuthActivity.this, "빈칸 없이 입력해주세요!", Toast.LENGTH_SHORT).show();
            }
        });
        binding.authTwitter.setOnClickListener(this);
        binding.authFacebook.setOnClickListener(this);
        binding.authKakao.setOnClickListener(this);
        binding.authNaver.setOnClickListener(this);
        binding.authRegisterLaunch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.authRegisterLaunch:
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                break;
            case R.id.authTwitter:
                binding.authTwitterLaunch.performClick();
                break;
            case R.id.authFacebook:
                Toast.makeText(AuthActivity.this, "페이스북 로그인은 다음 버전에서 지원할 예정입니다!", Toast.LENGTH_SHORT).show();
//                binding.authFacebookLaunch.performClick();
                break;
            case R.id.authNaver:
                Toast.makeText(AuthActivity.this, "네이버 로그인은 다음 버전에서 지원할 예정입니다!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.authKakao:
                Toast.makeText(AuthActivity.this, "카카오 로그인은 다음 버전에서 지원할 예정입니다!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        manager.onActivityResult(requestCode, resultCode, data);
        binding.authTwitterLaunch.onActivityResult(requestCode, resultCode, data);
    }


    private void setFacebookCallback() {
        LoginManager.getInstance().registerCallback(manager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                new LoadFacebookInfo().execute(loginResult.getAccessToken().getToken());
                Log.e("asdf_tokenfb", loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(AuthActivity.this, "로그인 인증 중에 문제가 발생했습니다.\n서비스 관리자에게 문의해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setTwitterCallback() {
        binding.authTwitterLaunch.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                TwitterSession session = result.data;
                new LoadTwitterInfo().execute(session.getAuthToken().token, session.getAuthToken().secret, String.valueOf(session.getUserId()));
                Toast.makeText(AuthActivity.this, session.getAuthToken().token + "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(TwitterException exception) {
                Log.e("authasdf", exception.getMessage());
            }
        });
    }

    class LoadTwitterInfo extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            final String[] twitterCredientials = strings;
            twitterLogin = service.twitterLogin(twitterCredientials[0], twitterCredientials[1], twitterCredientials[2]);
            twitterLogin.enqueue(new retrofit2.Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    switch (response.code()) {
                        case 200:
                            try {
                                dataManager.saveTwitterUserInfo(new JSONObject(response.body().string()));
                                dataManager.saveUserCredential(twitterCredientials);
                                Toast.makeText(AuthActivity.this, dataManager.getActiveUser().second.getName() + " 님 환영합니다!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            } catch (IOException e) {
                                Log.e("asdf", e.getMessage());
                            } catch (JSONException e) {
                                Log.e("asdf", e.getMessage());
                                e.printStackTrace();
                            }
                            break;
                        case 400:
                            Toast.makeText(getApplicationContext(), "서버 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("asdf", t.getMessage());
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    class LoadFacebookInfo extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(final String... strings) {
            facebookLogin = service.facebookLogin(strings[0]);
            facebookLogin.enqueue(new retrofit2.Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.e("asdf", response.code()+"");
                    switch (response.code()) {
                        case 200:
                            try {
                                Log.e("asdf", response.body().string());
                                dataManager.saveFacebookUserInfo(new JSONObject(response.body().string()));
                            } catch (IOException e) {
                                Log.e("asdfio", e.getMessage());
                            } catch (JSONException e) {
                                Log.e("asdf", e.getMessage());
                                e.printStackTrace();
                            }
                            dataManager.saveUserCredential(strings[0]);
//                            Toast.makeText(AuthActivity.this, dataManager.getActiveUser().second.getName() + " 님 환영합니다!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                            break;
                        case 400:
                            break;
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("asdf", t.getMessage());
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    class LoadNativeUserInfo extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(final String... strings) {
            autoLogin = service.userAutoLogin(strings[0]);
            autoLogin.enqueue(new retrofit2.Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    switch (response.code()) {
                        case 200:
                            try {
                                dataManager.saveNativeLoginUserInfo(new JSONObject(response.body().string()));
                                Toast.makeText(AuthActivity.this, dataManager.getActiveUser().second.getName() + " 님 환영합니다!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            } catch (JSONException e) {
                                Log.e("asdf", e.getMessage());
                            } catch (IOException e) {
                                Log.e("asdf", e.getMessage());
                                e.printStackTrace();
                            }
                            break;
                        case 400:
                            Toast.makeText(AuthActivity.this, "로그인 인증 중에 문제가 발생했습니다.\n서비스 관리자에게 문의해주세요.", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    Log.e("asdf", response.code() + "");
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("asdf", t.getMessage());
                }
            });
            return null;
        }
    }
}