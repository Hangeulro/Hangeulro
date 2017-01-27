package kr.edcan.neologism.utils;

import android.util.Log;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by Junseok Oh on 2017-01-27.
 */

public class DBMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();
        Log.e("asdf DicDBData Count", realm.where("DicDBData").count() + "");
        Log.e("asdf DicDBData Count", schema.get("DicDBData").getFieldNames().contains("cata") + "");
        Log.e("asdf DicDBData", realm.getVersion() + "");
        if (oldVersion == 0) {
            schema.get("DicDBData").addField("cata", String.class);
            oldVersion++;
        }
    }
}

