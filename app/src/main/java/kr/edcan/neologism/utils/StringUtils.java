package kr.edcan.neologism.utils;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Junseok Oh on 2017-01-29.
 */

public class StringUtils {
    public static String[] tagArr = {
            "사", "우", "서", "화", "공", "일"
    };
    public static int getCodeType(String cata) {
        ArrayList<String> tagArray = new ArrayList<>();
        Collections.addAll(tagArray, tagArr);
        String[] s = cata.split("\"");
        return tagArray.indexOf(s[1]);
    }
    public static int getCodeTypeByTag(String tag) {
        ArrayList<String> tagArray = new ArrayList<>();
        Collections.addAll(tagArray, tagArr);
        return tagArray.indexOf(tag);
    }
}
