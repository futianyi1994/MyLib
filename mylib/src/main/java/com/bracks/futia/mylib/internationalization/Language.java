package com.bracks.futia.mylib.internationalization;

import android.content.Context;


/**
 * good programmer.
 *
 * @date : 2019-05-17 上午 10:41
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class Language {
    private static volatile Language singleton = null;

    /**
     * 单利构造器私有化
     */
    private Language() {
    }

    /**
     * 对外唯一实例的接口
     */
    public static Language getInstance() {
        if (singleton == null) {
            synchronized (Language.class) {
                if (singleton == null) {
                    singleton = new Language();
                }
            }
        }
        return singleton;
    }

    private String language = "zh";

    public void setLanguage(String language) {
        this.language = language;
    }

    public String language() {
        //Language.language 为对应的资源格式后缀，比如"zh"
        return language;
    }

    /**
     * 判断当前国家语言
     *
     * @param context
     * @return
     */
    public String getCurCountryLan(Context context) {
        return context.getResources().getConfiguration().locale.getLanguage() + "-" + context.getResources().getConfiguration().locale.getCountry();
    }

    /**
     * 判断是否是中国
     *
     * @param context
     * @return
     */
    public boolean isZhCN(Context context) {
        String lang = context.getResources().getConfiguration().locale.getCountry();
        return "CN".equalsIgnoreCase(lang);
    }

}
