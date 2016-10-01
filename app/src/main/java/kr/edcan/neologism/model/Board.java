package kr.edcan.neologism.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

import retrofit2.http.Field;

/**
 * Created by JunseokOh on 2016. 9. 28..
 */
public class Board {
    private String boardid, title, writer, writerToken, contents, imageurl, writer_profile;
    private Date date;
    private int good, bad, share;
    private ArrayList<Comments> comments;

    public Board(String boardid, String title, String writer, String writerToken, String contents, String imageurl, String writer_profile, Date date, int good, int bad, int share, ArrayList<Comments> comments) {
        this.boardid = boardid;
        this.title = title;
        this.writer = writer;
        this.writerToken = writerToken;
        this.contents = contents;
        this.imageurl = imageurl;
        this.writer_profile = writer_profile;
        this.date = date;
        this.good = good;
        this.bad = bad;
        this.share = share;
        this.comments = comments;
    }

    public class Comments{
        private String writer, summary, profile_image;
        private Date date;

        public Comments(String writer, String summary, String profile_image, Date date) {
            this.writer = writer;
            this.summary = summary;
            this.profile_image = profile_image;
            this.date = date;
        }

        public String getWriter() {
            return writer;
        }

        public String getSummary() {
            return summary;
        }

        public String getProfile_image() {
            return profile_image;
        }

        public Date getDate() {
            return date;
        }
    }

    public String getBoardid() {
        return boardid;
    }

    public String getTitle() {
        return title;
    }

    public String getWriter() {
        return writer;
    }

    public String getWriterToken() {
        return writerToken;
    }

    public String getContents() {
        return contents;
    }

    public String getImageurl() {
        return imageurl;
    }

    public Date getDate() {
        return date;
    }

    public int getGood() {
        return good;
    }

    public int getBad() {
        return bad;
    }

    public int getShare() {
        return share;
    }

    public ArrayList<Comments> getComments() {
        return comments;
    }

    public String getWriter_profile() {
        return writer_profile;
    }
}
