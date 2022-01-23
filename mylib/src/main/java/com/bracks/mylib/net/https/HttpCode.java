package com.bracks.mylib.net.https;

/**
 * good programmer.
 *
 * @date : 2019-02-15 上午 11:30
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class HttpCode {
    /**
     * 请求成功
     */
    public static final int OK = 200;
    /**
     * 创建成功
     */
    public static final int CREATED = 201;
    /**
     * 更新成功
     */
    public static final int UPDATED = 202;
    /**
     * 请求永久重定向
     */
    public static final int REDIRECT_FOREVER = 301;
    /**
     * 请求临时重定向
     */
    public static final int REDIRECT_TEMP = 302;
    /**
     * 未更改
     */
    public static final int UNCHANGE = 304;
    /**
     * 请求地址不存在或者包含不⽀持的参数
     */
    public static final int BAD_REQUEST = 400;
    /**
     * 未授权，因为未成功进⾏身份认证
     */
    public static final int UNAUTHORIZED = 401;
    /**
     * 被禁⽌访问，因为没有访问特定资源的权限
     */
    public static final int FORBIDDEN = 403;
    /**
     * 请求的资源不存在
     */
    public static final int NOT_FOUND = 404;
    /**
     * 服务器理解请求，但其中包含⾮法参数（如包含XSS攻击⻛险参数）
     */
    public static final int ILLEGAL_PARAMS = 422;
    /**
     * 调⽤请求频率超出限额
     */
    public static final int TOO_MANY_REQUEST = 429;
    /**
     * 服务器内部错误
     */
    public static final int INTERNAL_SERVER_ERROR = 500;
}
