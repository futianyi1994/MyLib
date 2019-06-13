package com.bracks.wanandroid.net;

import com.blankj.utilcode.util.ActivityUtils;
import com.bracks.mylib.net.interceptor.ResponseParamInterceptor;
import com.bracks.mylib.utils.json.JsonUtil;
import com.bracks.wanandroid.activity.LoginUi;
import com.bracks.wanandroid.model.bean.Result;

/**
 * good programmer.
 *
 * @data : 2018-02-27 下午 02:03
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class LoginInterceptor extends ResponseParamInterceptor {

    @Override
    protected ResponseParamInterceptor.ResponseParamCallback responseParamCallback() {
        return (String bodyString) -> {
            Result result = JsonUtil.fromJson(bodyString, Result.class);
            if (!result.OK()) {
                //重新登录
                if (result.isExpired() || result.isRedirect()) {
                    ActivityUtils.startActivity(LoginUi.class);
                }
            }
        };
    }
}
