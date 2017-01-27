package kr.edcan.neologism.utils;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import kr.edcan.neologism.model.DicDBData;
import kr.edcan.neologism.model.DicData;
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
    static Call<String> getCurrentVersion;
    static int currentVersion;

    static void convertDBfromJson(JSONArray array) {
        realm.beginTransaction();
        realm.where(DicDBData.class).findAll().deleteAllFromRealm();
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject content = array.getJSONObject(i);
                JSONArray originTagList = content.getJSONArray("cata");
                String originTagStr = originTagList.toString();
                Log.e("asdf", originTagStr);
                DicDBData data = realm.createObject(DicDBData.class);
                data.setContents(content.getString("id"),
                        content.getString("word"),
                        content.getString("mean"),
                        content.getString("ex"),
                        originTagStr);
            } catch (JSONException e) {
                Log.e("asdf", e.getMessage());
                e.printStackTrace();
            }
        }
        realm.commitTransaction();
    }

    public static ArrayList<DicData> getDBJson() {
        ArrayList<DicData> returnArray = new ArrayList<>();
        realm.beginTransaction();
        RealmResults<DicDBData> results = realm.where(DicDBData.class).findAll();
        for (DicDBData data : results) {
            returnArray.add(new DicData(data));
        }
        realm.commitTransaction();
        return returnArray;
    }

    public static ArrayList<String> getDBStringJson() {
        ArrayList<String> returnArray = new ArrayList<>();
        realm.beginTransaction();
        RealmResults<DicDBData> results = realm.where(DicDBData.class).findAll();
        for (DicDBData data : results) {
            returnArray.add(data.getWord());
            Log.e("asdf", data.getWord() + " word searched");
        }
        realm.commitTransaction();
        return returnArray;
    }

    public DBSync() {
    }

    public static void syncDB(Context c) {
        final DataManager manager = new DataManager(c);
        realm = Realm.getDefaultInstance();
        service = NetworkHelper.getNetworkInstance();
        currentVersion = manager.getCurrentDatabaseVersion();
        getCurrentVersion = service.getDataBaseVersion();
        getCurrentVersion.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
//                int remoteDatabaseVersion = Integer.parseInt(response.body());
                int remoteDatabaseVersion = 3;
                Log.e("asdf", "Current Version : " + currentVersion + " DataBase Version : " + remoteDatabaseVersion);
                if (remoteDatabaseVersion != currentVersion) {
                    manager.saveCurrentDatabaseVersion(remoteDatabaseVersion);
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

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("asdf", t.getMessage());
            }
        });
    }
}
