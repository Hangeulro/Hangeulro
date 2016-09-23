package kr.edcan.neologism.model;


import io.realm.RealmObject;

/**
 * Created by JunseokOh on 2016. 9. 19..
 */
public class DicDBData extends RealmObject{

    private String id, word, mean, example;

    public DicDBData() {
    }

    public DicDBData(String id, String word, String mean, String example) {
        this.id = id;
        this.word = word;
        this.mean = mean;
        this.example = example;
    }

    public void setContents(String id, String name, String mean, String example){
        this.id = id;
        this.word = name;
        this.mean = mean;
    }
    public void setId(String id) {
        this.id = id;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setMean(String mean) {
        this.mean = mean;
    }

    public String getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    public String getMean() {
        return mean;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }
}
