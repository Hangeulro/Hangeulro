package kr.edcan.neologism.utils;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

/**
 * Created by Junseok on 2016. 2. 18..
 */
public class KillProcess extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityCompat.finishAffinity(this);
        System.exit(0);
        super.onCreate(savedInstanceState);
    }
}
