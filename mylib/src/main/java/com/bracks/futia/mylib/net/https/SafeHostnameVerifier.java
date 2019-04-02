package com.bracks.futia.mylib.net.https;


import com.bracks.futia.mylib.Constants;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;


/**
 * good programmer.
 *
 * @date : 2018-09-01 下午 05:14
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */

public class SafeHostnameVerifier implements HostnameVerifier {
    @Override
    public boolean verify(String hostname, SSLSession session) {
        //校验hostname是否正确，如果正确则建立连接
        if (Constants.IP.equals(hostname)) {
            return true;
        }
        return false;
    }
}
