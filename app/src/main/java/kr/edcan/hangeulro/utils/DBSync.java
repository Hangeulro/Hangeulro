package kr.edcan.hangeulro.utils;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import kr.edcan.hangeulro.model.DicDBData;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by JunseokOh on 2016. 9. 19..
 */
public class DBSync {
    static Realm realm;
    static NetworkInterface service;
    static Call<ResponseBody> getWordList;

    static void convertDBfromJson(JSONArray array) {
        realm.beginTransaction();
        realm.where(DicDBData.class).findAll().deleteAllFromRealm();
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject content = array.getJSONObject(i);
                DicDBData data = realm.createObject(DicDBData.class);
                data.setContents(content.getString("id"), content.getString("word"), content.getString("mean"));
            } catch (JSONException e) {
                Log.e("asdf", e.getMessage());
                e.printStackTrace();
            }
        }
        realm.commitTransaction();
    }

    public DBSync() {
    }

    public static void syncDB() {
        realm = Realm.getDefaultInstance();
        service = NetworkHelper.getNetworkInstance();
        getWordList = service.getWordList();
        getWordList.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                switch (response.code()) {
                    case 200:
                        try {
                            convertDBfromJson(new JSONArray(response.body().string()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 401:
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
