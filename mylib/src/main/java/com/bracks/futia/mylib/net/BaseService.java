package com.bracks.futia.mylib.net;


import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
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
    /**
     * @param start 从某个字节开始下载数据
     * @param url   文件下载的url
     * @return Observable
     * @Streaming 这个注解必须添加，否则文件全部写入内存，文件过大会造成内存溢出
     */
    @Streaming
    @GET
    Observable<ResponseBody> download(@Header("RANGE") String start, @Url String url);

    @Multipart
    @POST("/robot/qadb/fileQaAdd")
    Observable<ResponseBody> upload(@Part MultipartBody.Part part,
                                    @Part("accountId") RequestBody accountId,
                                    @Part("path") RequestBody path,
                                    @Part("sn") RequestBody sn);
}
