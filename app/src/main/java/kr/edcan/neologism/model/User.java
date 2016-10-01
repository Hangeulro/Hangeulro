package kr.edcan.neologism.model;

/**
 * Created by JunseokOh on 2016. 9. 24..
 */
public class User {
    private int userType;
    private String userid, name, token, profileImageUrl, level, point;

    public User(int userType, String userid, String name, String token, String profileImageUrl, String level, String point) {
        this.userType = userType;
        this.userid = userid;
        this.name = name;
        this.token = token;
        this.profileImageUrl = profileImageUrl;
        this.level = level;
        this.point = point;
    }

    public User(int userType, String userid, String name, String token, String profileImageUrl) {
        this.userType = userType;
        this.userid = userid;
        this.name = name;
        this.token = token;
        this.profileImageUrl = profileImageUrl;
    }

    public int getUserType() {
        return userType;
    }

    public String getUserid() {
        return userid;
    }

    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getLevel() {
        return level;
    }

    public String getPoint() {
        return point;
    }
}
