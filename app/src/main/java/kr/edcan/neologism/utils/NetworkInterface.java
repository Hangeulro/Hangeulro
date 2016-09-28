package kr.edcan.neologism.utils;

import java.util.List;

import kr.edcan.neologism.model.FacebookUser;
import kr.edcan.neologism.model.Quiz;
import kr.edcan.neologism.model.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by JunseokOh on 2016. 9. 18..
 */
public interface NetworkInterface {

    @POST("/my")
    @FormUrlEncoded
    Call<User> getUserInfo(@Field("token") String token);

    @POST("/auth/login")
    @FormUrlEncoded
    Call<ResponseBody> userLogin(@Field("userid") String userid, @Field("pw") String password);

    @POST("/auth/auto")
    @FormUrlEncoded
    Call<ResponseBody> userAutoLogin(@Field("token") String token);

    @POST("/auth/register")
    @FormUrlEncoded
    Call<ResponseBody> userRegister(@Field("userid") String userid, @Field("pw") String password, @Field("name") String username);

    @GET("/auth/fb/token")
    Call<FacebookUser> facebookLogin(@Query("access_token") String accessToken);

    @GET("/auth/tw/token")
    Call<ResponseBody> twitterLogin(
            @Query("oauth_token") String accessToken,
            @Query("oauth_token_secret") String accessTokenSecret,
            @Query("user_id") String userid);

    @GET("/version")
    Call<String> getDataBaseVersion();

    @POST("/word/cata")
    @FormUrlEncoded
    Call<ResponseBody> getWordWithType(@Field("cata") String cata);

    @POST("/word")
    Call<ResponseBody> getWordList();

    @POST("/quize")
    Call<List<Quiz>> getQuizList();

    @POST("/my/pointUp")
    @FormUrlEncoded
    Call<ResponseBody> scoreUp(@Field("token") String token, @Field("pointUp") int point);
}
