package kr.edcan.neologism.utils;

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

    @POST("/auth/login")
    @FormUrlEncoded
    Call<ResponseBody> userLogin(@Field("userid") String userid, @Field("pw") String password);

    @POST("/auth/login/auto")
    @FormUrlEncoded
    Call<ResponseBody> userAutoLogin(@Field("id") String userid, @Field("token") String token);

    @POST("/auth/login/register")
    @FormUrlEncoded
    Call<ResponseBody> userRegister(@Field("userid") String userid, @Field("pw") String password, @Field("username") String username);

    @GET("/auth/fb/token")
    Call<ResponseBody> facebookLogin(@Query("access_token") String accessToken);

    @GET("/auth/tw/token")
    Call<ResponseBody> twitterLogin(
            @Query("oauth_token") String accessToken,
            @Query("oauth_token_secret") String accessTokenSecret,
            @Query("user_id") String userid);

    @POST("/word/cata")
    @FormUrlEncoded
    Call<ResponseBody> getWordWithType(@Field("cata") String cata);

    @POST("/word")
    Call<ResponseBody> getWordList();

}
