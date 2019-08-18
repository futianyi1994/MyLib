package com.bracks.mylib.net.https;

import androidx.annotation.NonNull;

import com.bracks.mylib.Constants;
import com.bracks.mylib.utils.save.SPUtils;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/**
 * @author futia
 * @date 20/01/2017
 * 只有返回HTTP的状态码为401时，才会使用Authenticator接口
 */
public abstract class TokenAuth implements Authenticator {
    private String tokenName = "yui2-token";

    public TokenAuth() {
    }

    public TokenAuth(String tokenName) {
        this.tokenName = tokenName;
    }

    @Override
    public Request authenticate(Route route, @NonNull Response response) throws IOException {

        Request oriRequest = response.request();

        String oriUrl = oriRequest.url().toString();

        if (synToken()) {
            HttpUrl.Builder authorizedUrlBuilder = oriRequest
                    .url()
                    .newBuilder()
                    .scheme(oriRequest.url().scheme())
                    .host(oriRequest.url().host());
            Request.Builder newRequestBuilder = oriRequest
                    .newBuilder()
                    .url(authorizedUrlBuilder.build())
                    .removeHeader(tokenName)
                    .addHeader(tokenName, SPUtils.getInstance().getString(Constants.TOKEN))
                    .method(oriRequest.method(), oriRequest.body());
            return newRequestBuilder.build();
        } else {
            throw new IOException("登陆已过期,请重新登陆");
        }
    }

    /**
     * 重新获取最新token
     *
     * @return true：获取成功
     */
    protected abstract boolean synToken();
}
