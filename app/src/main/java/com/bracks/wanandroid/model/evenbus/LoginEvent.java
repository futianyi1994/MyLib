package com.bracks.wanandroid.model.evenbus;

/**
 * good programmer.
 *
 * @date : 2019-06-23 下午 04:31
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class LoginEvent {

    private String userName;
    private boolean isLogin;

    public LoginEvent(String userName, boolean isLogin) {
        this.userName = userName;
        this.isLogin = isLogin;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
}
