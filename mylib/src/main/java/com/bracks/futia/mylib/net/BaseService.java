package com.bracks.futia.mylib.net;


import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * good programmer.
 *
 * @date : 2018-09-02 下午 03:13
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public interface BaseService {
    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);

    @Multipart
    @POST("/robot/qadb/fileQaAdd")
    Observable<ResponseBody> upload(@Part MultipartBody.Part part,
                                    @Part("accountId") RequestBody accountId,
                                    @Part("path") RequestBody path,
                                    @Part("sn") RequestBody sn);
}
