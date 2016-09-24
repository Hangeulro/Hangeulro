package kr.edcan.neologism.model;

/**
 * Created by KOHA_DESKTOP on 2016. 6. 19..
 */
public class QuizData {
    private String title;
    private String subtitle;
    private String data;
    private int logo;

    public QuizData(String title, String subtitle, String item, int logo) {
        this.title = title;
        this.subtitle = subtitle;
        this.logo = logo;
        this.data = item;
    }

    public String getData() {
        return data;
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
