package kr.edcan.hangeulro.utils;

import android.content.Context;

import io.realm.Realm;

/**
 * Created by JunseokOh on 2016. 9. 19..
 */
public class DBSync{
    private Context context;
    private Realm realm;

    public DBSync(Context context) {
        this.context = context;
        realm = Realm.getDefaultInstance();
    }

}
