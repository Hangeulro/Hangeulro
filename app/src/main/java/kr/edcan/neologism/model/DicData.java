package kr.edcan.neologism.model;

/**
 * Created by JunseokOh on 2016. 9. 19..
 */
public class DicData {

    private String id, word, mean, example, cata;

    public DicData() {
    }
    public DicData(DicDBData data) {
        setMean(data.getMean());
        setCata(data.getCata());
        setExample(data.getExample());
        setId(data.getId());
        setWord(data.getWord());
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
