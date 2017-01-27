package kr.edcan.neologism.model;


import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by JunseokOh on 2016. 9. 19..
 */
public class DicDBData extends RealmObject {

    private String id, word, mean, example, cata;

    public DicDBData() {
    }


    public void setContents(String id, String name, String mean, String example, String cata) {
        this.id = id;
        this.word = name;
        this.mean = mean;
        this.example = example;
        this.cata = cata;
    }

    public String getCata() {
        return cata;
    }

    public void setCata(String cata) {
        this.cata = cata;
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
