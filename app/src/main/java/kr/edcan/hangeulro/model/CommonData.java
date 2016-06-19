package kr.edcan.hangeulro.model;

/**
 * Created by KOHA_DESKTOP on 2016. 6. 19..
 */
public class CommonData {
    private String title;
    private int logo;

    public CommonData(String title, int logo) {
        this.title = title;
        this.logo = logo;
    }

    public String getTitle() {
        return title;
    }

    public int getLogo() {
        return logo;
    }
}
