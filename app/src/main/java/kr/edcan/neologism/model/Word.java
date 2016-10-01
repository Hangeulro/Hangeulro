package kr.edcan.neologism.model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by JunseokOh on 2016. 9. 29..
 */
public class Word {
    private String id, word, mean, ex;
    private int see;
    private ArrayList<String> similar, cata, tag;
    private ArrayList<Comment> comments;

    public Word(String id, String word, String mean, String ex, int see, ArrayList<String> similar, ArrayList<String> cata, ArrayList<String> tag, ArrayList<Comment> comments) {
        this.id = id;
        this.word = word;
        this.mean = mean;
        this.ex = ex;
        this.see = see;
        this.similar = similar;
        this.cata = cata;
        this.tag = tag;
        this.comments = comments;
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

    public String getEx() {
        return ex;
    }

    public int getSee() {
        return see;
    }

    public ArrayList<String> getSimilar() {
        return similar;
    }

    public ArrayList<String> getCata() {
        return cata;
    }

    public ArrayList<String> getTag() {
        return tag;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public class Comment{
        private String writer, summary;
        private Date date;

        public Comment(String writer, String summary, Date date) {
            this.writer = writer;
            this.summary = summary;
            this.date = date;
        }

        public String getWriter() {
            return writer;
        }

        public String getSummary() {
            return summary;
        }

        public Date getDate() {
            return date;
        }
    }
}
