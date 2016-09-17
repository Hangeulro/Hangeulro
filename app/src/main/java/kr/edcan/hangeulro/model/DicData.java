package kr.edcan.hangeulro.model;

/**
 * Created by JunseokOh on 2016. 9. 17..
 */
public class DicData {
    private String title, meaning, example;
    private int searchCount;

    public DicData(String title, String meaning, String example, int searchCount) {
        this.title = title;
        this.meaning = meaning;
        this.example = example;
        this.searchCount = searchCount;
    }

    public String getTitle() {
        return title;
    }

    public String getMeaning() {
        return meaning;
    }

    public String getExample() {
        return example;
    }

    public int getSearchCount() {
        return searchCount;
    }
}
