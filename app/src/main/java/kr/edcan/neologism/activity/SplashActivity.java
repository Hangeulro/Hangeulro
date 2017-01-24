package kr.edcan.neologism.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import kr.edcan.neologism.R;
import kr.edcan.neologism.model.FacebookUser;
import kr.edcan.neologism.model.User;
import kr.edcan.neologism.utils.ClipBoardService;
import kr.edcan.neologism.utils.DataManager;
import kr.edcan.neologism.utils.NetworkHelper;
import kr.edcan.neologism.utils.NetworkInterface;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    Call<ResponseBody> userLogin, autoLogin, twitterLogin;
    Call<FacebookUser> facebookLogin;
    NetworkInterface service;
    DataManager dataManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        new MaterialDialog.Builder(this)
//                .title("서비스 점검중입니다.")
//                .content("점검 기간 : 2017.01.02 ~ 2017.01.14\n점검 기간은 변경될 수 있습니다.\n더욱 더 좋은 서비스로 찾아뵙겠습니다.")
//                .cancelable(false)
//                .positiveText("확인")
//                .onPositive(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        finish();
//                    }
//                })
//                .show();
//        stopService(new Intent(getApplicationContext(), ClipBoardService.class));

        setDefault();
        validateUserToken();
    }

    private void setDefault() {
        dataManager = new DataManager(getApplicationContext());
        service = NetworkHelper.getNetworkInstance();
    }

    private void validateUserToken() {
        Pair<Boolean, User> userPair = dataManager.getActiveUser();
        if (!userPair.first) {
            startActivity(new Intent(getApplicationContext(), AuthActivity.class));
            finish();
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
                                for (String s : twitterCredientials) {
                                    Log.e("asdfasdf", s);
                                }
                                Toast.makeText(SplashActivity.this, dataManager.getActiveUser().second.getName() + " 님 환영합니다!", Toast.LENGTH_SHORT).show();
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
                            startActivity(new Intent(getApplicationContext(), AuthActivity.class));
                            finish();
                            break;
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    startActivity(new Intent(getApplicationContext(), AuthActivity.class));
                    finish();
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
            facebookLogin.enqueue(new retrofit2.Callback<FacebookUser>() {
                @Override
                public void onResponse(Call<FacebookUser> call, Response<FacebookUser> response) {
                    switch (response.code()) {
                        case 200:
                            dataManager.saveFacebookUserInfo(response.body());
                            Toast.makeText(SplashActivity.this, response.body().getName() + "님 환영합니다!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                            break;
                        case 401:
                            Toast.makeText(SplashActivity.this, "인증 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), AuthActivity.class));
                            finish();
                            break;
                    }
                }

                @Override
                public void onFailure(Call<FacebookUser> call, Throwable t) {
                    startActivity(new Intent(getApplicationContext(), AuthActivity.class));
                    finish();
                    Log.e("test", t.getMessage());
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
                                Toast.makeText(SplashActivity.this, dataManager.getActiveUser().second.getName() + " 님 환영합니다!", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(SplashActivity.this, "로그인 인증 중에 문제가 발생했습니다.\n서비스 관리자에게 문의해주세요.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), AuthActivity.class));
                            finish();
                            break;
                    }
                    Log.e("asdf", response.code() + "");
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("asdf", t.getMessage());
                    startActivity(new Intent(getApplicationContext(), AuthActivity.class));
                    finish();
                }
            });
            return null;
        }
    }
}
