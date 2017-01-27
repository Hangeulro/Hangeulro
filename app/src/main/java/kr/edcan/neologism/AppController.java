package kr.edcan.neologism;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import java.io.FileNotFoundException;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmMigrationNeededException;
import kr.edcan.neologism.utils.DBMigration;

/**
 * Created by JunseokOh on 2016. 9. 19..
 */
public class AppController extends Application {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "SvRMQBeHtW8aIZVYQZnrxnorN";
    private static final String TWITTER_SECRET = "At91tGX1v5MMwwUvqzNUgjvpZrnCB6O41VehdJASHs86bieaFd";
    RealmConfiguration realmConfig;
    @Override
    public void onCreate() {
        super.onCreate();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        Realm.removeDefaultConfiguration();
        try {
            realmConfig = new RealmConfiguration.Builder(getApplicationContext())
                    .schemaVersion(1)
                    .migration(new DBMigration())
                    .build();
        } catch (RealmMigrationNeededException e){
            try {
                Realm.migrateRealm(realmConfig);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        }
        Realm.setDefaultConfiguration(realmConfig);
    }
}
