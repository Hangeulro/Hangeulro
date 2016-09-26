package kr.edcan.neologism.model;

/**
 * Created by JunseokOh on 2016. 9. 27..
 */
public class FacebookUser {


    public String userid, name, token, profile_image;

    public FacebookUser(String userid, String name, String token, String profile_image) {
        this.userid = userid;
        this.name = name;
        this.token = token;
        this.profile_image = profile_image;
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

    public String getProfile_image() {
        return profile_image;
    }
}
