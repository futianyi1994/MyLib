package com.bracks.wanandroid.net;


import com.bracks.wanandroid.model.bean.Collect;
import com.bracks.wanandroid.model.bean.History;
import com.bracks.wanandroid.model.bean.Login;
import com.bracks.wanandroid.model.bean.PublicList;
import com.bracks.wanandroid.model.bean.Result;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * good programmer.
 *
 * @date : 2018-12-21 下午 05:44
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public interface DataApi {

    /**
     * 获取公众号列表
     *
     * @return
     */
    @GET("wxarticle/chapters/json")
    Observable<PublicList> getPublicList();

    /**
     * 在某个公众号中搜索历史文章
     *
     * @param id
     * @param page
     * @param k
     * @return
     */
    @GET("wxarticle/list/{id}/{page}/json")
    Observable<History> getHistoryList(@Path("id") int id,
                                       @Path("page") int page,
                                       @Query("k") String k);

    /**
     * 登陆
     *
     * @param username
     * @param password
     * @return
     */
    @POST("user/login")
    @FormUrlEncoded
    Observable<Login> login(@Field("username") String username,
                            @Field("password") String password);

    /**
     * 注册
     *
     * @param username
     * @param password
     * @param repassword
     * @return
     */
    @POST("user/register")
    @FormUrlEncoded
    Observable<Login> register(@Field("username") String username,
                               @Field("password") String password,
                               @Field("repassword") String repassword);

    /**
     * 推出登录
     *
     * @return
     */
    @GET("user/logout/json")
    Observable<Login> logout();

    /**
     * 收藏
     *
     * @param id
     * @return
     */
    @POST("lg/collect/{id}/json")
    Observable<Result<String>> collect(@Path("id") int id);

    /**
     * 收藏列表
     *
     * @param page
     * @return
     */
    @GET("lg/collect/list/{page}/json")
    Observable<Collect> collectList(@Path("page") int page);

    /**
     * 取消收藏
     *
     * @param id
     * @return
     */
    @POST("lg/uncollect_originId/{id}/json")
    Observable<Result<String>> cancelCollect(@Path("id") int id);


}
