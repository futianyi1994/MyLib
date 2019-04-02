package com.bracks.futia.mylib.internationalization;

import android.content.Context;


/**
 * Good programmer.
 * Created by futia on 2017-11-07 下午 03:45.
 * Email:futianyi1994@126.com
 * Description:公共的语言
 */

public class Language {
    private static volatile Language singleton = null;

    /**
     * 单利构造器私有化
     */
    private Language(Context context) {
        this.context = context;
    }

    /**
     * 对外唯一实例的接口
     */
    public static final Language getInstance(Context context) {
        if (singleton == null) {
            synchronized (Language.class) {
                if (singleton == null) {
                    singleton = new Language(context);
                }
            }
        }
        return singleton;
    }

    private Context context;
    public String Language = "zh";

    public String language() {
        //Language.Language 为对应的资源格式后缀，比如"zh"

        return Language;
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
        if (lang.equalsIgnoreCase("CN")) {
            return true;
        }
        return false;
    }

}
