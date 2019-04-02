package com.bracks.futia.mylib.exception;

/**
 * good programmer.
 *
 * @date : 2018-12-28 下午 05:59
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class ApiException extends RuntimeException {

    private final int errorCode;

    public ApiException(int errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}