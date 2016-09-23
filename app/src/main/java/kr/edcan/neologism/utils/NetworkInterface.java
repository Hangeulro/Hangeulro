package kr.edcan.neologism.utils;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by JunseokOh on 2016. 9. 18..
 */
public interface NetworkInterface {

    @POST("/word/cata")
    @FormUrlEncoded
    Call<ResponseBody> getWordWithType(@Field("cata") String cata);

    @POST("/word")
    Call<ResponseBody> getWordList();

}
