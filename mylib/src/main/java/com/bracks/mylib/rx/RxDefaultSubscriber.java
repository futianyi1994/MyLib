package com.bracks.mylib.rx;

import android.text.TextUtils;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.bracks.mylib.R;
import com.bracks.mylib.base.model.Result;
import com.bracks.mylib.exception.ExceptionReason;
import com.bracks.mylib.utils.log.TLog;
import com.google.gson.JsonParseException;

import org.json.JSONException;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.ParseException;

import retrofit2.HttpException;


/**
 * good programmer.
 *
 * @date : 2018-08-19 下午 03:27
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public abstract class RxDefaultSubscriber<T extends Result> implements Subscriber<T> {
    private static final String TAG = "RxDefaultSubscriber";
    private Subscription subscription;

    @Override
    public void onSubscribe(Subscription s) {
        subscription = s;
    }

    @Override
    public void onNext(T response) {
        if (response.OK()) {
            onSuccess(response);
        } else {
            onFail(response);
        }
    }

    @Override
    public void onError(Throwable e) {
        TLog.e(TAG, "onError: " + e.getMessage());
        //HTTP错误
        if (e instanceof HttpException) {
            onException(ExceptionReason.BAD_NETWORK);
            //连接错误
        } else if (e instanceof ConnectException
                || e instanceof UnknownHostException) {
            onException(ExceptionReason.CONNECT_ERROR);
            //连接超时
        } else if (e instanceof InterruptedIOException) {
            onException(ExceptionReason.CONNECT_TIMEOUT);
            //解析错误
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            onException(ExceptionReason.PARSE_ERROR);
        } else {
            onException(ExceptionReason.UNKNOWN_ERROR);
        }
    }

    @Override
    public void onComplete() {
    }

    /**
     * 请求成功
     *
     * @param response 服务器返回的数据
     */
    abstract public void onSuccess(T response);

    /**
     * 服务器返回数据，但响应码不为200
     *
     * @param response 服务器返回的数据
     */
    public void onFail(T response) {
        if (response.isExpired()) {
            TLog.e(TAG, "onFail: isExpired()");
        } else if (response.isRedirect()) {
            TLog.e(TAG, "onFail: isRedirect()");
        } else {
            ToastUtils.showLong(TextUtils.isEmpty(response.getMsg()) ? "服务器返回数据失败！" : response.getMsg());
        }
    }

    /**
     * 请求异常
     *
     * @param reason
     */
    public void onException(@ExceptionReason.Reason int reason) {
        switch (reason) {
            case ExceptionReason.CONNECT_ERROR:
                ToastUtils.showLong(R.string.error_connect, Toast.LENGTH_SHORT);
                break;
            case ExceptionReason.CONNECT_TIMEOUT:
                ToastUtils.showLong(R.string.error_timeout, Toast.LENGTH_SHORT);
                break;
            case ExceptionReason.BAD_NETWORK:
                ToastUtils.showLong(R.string.error_bad_network, Toast.LENGTH_SHORT);
                break;
            case ExceptionReason.PARSE_ERROR:
                ToastUtils.showLong(R.string.error_parse, Toast.LENGTH_SHORT);
                break;
            case ExceptionReason.UNKNOWN_ERROR:
            default:
                ToastUtils.showLong(R.string.error_unknown, Toast.LENGTH_SHORT);
                break;
        }
    }

    public void cancelRequest() {
        if (subscription != null) {
            subscription.cancel();
        }
    }
}