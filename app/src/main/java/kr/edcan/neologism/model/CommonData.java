package kr.edcan.neologism.model;

import android.graphics.drawable.Drawable;

/**
 * Created by KOHA_DESKTOP on 2016. 6. 19..
 */
public class CommonData {
    private String title;
    private String subtitle;
    private int logo;

    public CommonData(String title, String subtitle, int logo) {
        this.title = title;
        this.subtitle = subtitle;
        this.logo = logo;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getTitle() {
        return title;
    }

    public int getLogo() {
        return logo;
    }
}
