package kr.edcan.neologism.model;

/**
 * Created by JunseokOh on 2016. 9. 24..
 */
public class Quiz {
    private String word, mean;

    public Quiz(String word, String mean) {
        this.word = word;
        this.mean = mean;
    }

    public String getWord() {
        return word;
    }

    public String getMean() {
        return mean;
    }
}
