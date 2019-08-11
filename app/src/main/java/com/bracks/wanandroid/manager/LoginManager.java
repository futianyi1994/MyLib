package com.bracks.wanandroid.manager;

import com.bracks.mylib.rx.RxBus;
import com.bracks.mylib.utils.save.SPUtils;
import com.bracks.wanandroid.Contants;
import com.bracks.wanandroid.model.evenbus.LoginEvent;

/**
 * good programmer.
 *
 * @date : 2019-06-23 下午 02:38
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class LoginManager {
    public static void login(String username) {
        SPUtils.getInstance().put(Contants.SP_USER_NAME, username);
        SPUtils.getInstance().put(Contants.SP_IS_LOGIN, true);
        RxBus
                .getDefault()
                .post(new LoginEvent(username, true));
    }

    public static void logout() {
        SPUtils.getInstance().remove(Contants.SP_USER_NAME);
        SPUtils.getInstance().remove(Contants.SP_IS_LOGIN);
        RxBus
                .getDefault()
                .post(new LoginEvent("", false));
    }
}
