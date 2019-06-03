package com.bracks.mylib.rx;

import com.bracks.mylib.base.model.Result;
import com.bracks.mylib.exception.ApiException;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * good programmer.
 *
 * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
 * @date : 2018-12-28 下午 05:53
 * @author: futia
 * @email : futianyi1994@126.com
 * @description : 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
 */
public class RxMapResultFunc<T> implements Function<Result<T>, T> {
    @Override
    public T apply(@NonNull Result<T> result) {
        if (result.OK()) {
            return result.getData();
        } else {
            throw new ApiException(result.getCode(), result.getMsg());
        }
    }
}