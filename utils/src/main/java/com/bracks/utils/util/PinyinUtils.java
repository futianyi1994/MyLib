package com.bracks.utils.util;


import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.collection.SimpleArrayMap;

import com.github.promeg.pinyinhelper.Pinyin;
import com.github.promeg.pinyinhelper.PinyinDict;
import com.github.promeg.pinyinhelper.PinyinMapDict;

import java.util.HashMap;
import java.util.Map;


/**
 * good programmer.
 *
 * @date : 2020-03-03 17:35
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public final class PinyinUtils {

    // 多音字姓氏映射表
    private static final SimpleArrayMap<Character, String> SURNAMES;

    static {
        SURNAMES = new SimpleArrayMap<>(35);
        SURNAMES.put('乐', "YUE");
        SURNAMES.put('乘', "SHENG");
        SURNAMES.put('乜', "NIE");
        SURNAMES.put('仇', "QIU");
        SURNAMES.put('会', "GUI");
        SURNAMES.put('便', "PIAN");
        SURNAMES.put('区', "OU");
        SURNAMES.put('单', "SHAN");
        SURNAMES.put('参', "SHEN");
        SURNAMES.put('句', "GOU");
        SURNAMES.put('召', "SHAO");
        SURNAMES.put('员', "YUN");
        SURNAMES.put('宓', "FU");
        SURNAMES.put('弗', "FEI");
        SURNAMES.put('折', "SHE");
        SURNAMES.put('曾', "ZENG");
        SURNAMES.put('朴', "PIAO");
        SURNAMES.put('查', "ZHA");
        SURNAMES.put('洗', "XIAN");
        SURNAMES.put('盖', "GE");
        SURNAMES.put('祭', "ZHAI");
        SURNAMES.put('种', "CHONG");
        SURNAMES.put('秘', "BI");
        SURNAMES.put('繁', "PO");
        SURNAMES.put('缪', "MIAO");
        SURNAMES.put('能', "NAI");
        SURNAMES.put('蕃', "PI");
        SURNAMES.put('覃', "QIN");
        SURNAMES.put('解', "XIE");
        SURNAMES.put('谌', "SHAN");
        SURNAMES.put('适', "KUO");
        SURNAMES.put('都', "DU");
        //SURNAMES.put('阿', "E");
        SURNAMES.put('难', "NING");
        SURNAMES.put('黑', "HE");
        SURNAMES.put('翟', "ZHAI");
        init(Pinyin.newConfig().with(new PinyinMapDict() {
            @Override
            public Map<String, String[]> mapping() {
                HashMap<String, String[]> map = new HashMap<>();
                map.put("澹台", new String[]{"TANTAI"});
                map.put("尉迟", new String[]{"YUCHI"});
                map.put("万俟", new String[]{"MOQI"});
                map.put("单于", new String[]{"CHANYU"});
                map.put("乐", new String[]{"YUE"});
                map.put("乘", new String[]{"SHENG"});
                map.put("乜", new String[]{"NIE"});
                map.put("仇", new String[]{"QIU"});
                map.put("会", new String[]{"GUI"});
                map.put("便", new String[]{"PIAN"});
                map.put("区", new String[]{"OU"});
                map.put("单", new String[]{"SHAN"});
                map.put("参", new String[]{"SHEN"});
                map.put("句", new String[]{"GOU"});
                map.put("召", new String[]{"SHAO"});
                map.put("员", new String[]{"YUN"});
                map.put("宓", new String[]{"FU"});
                map.put("弗", new String[]{"FEI"});
                map.put("折", new String[]{"SHE"});
                map.put("曾", new String[]{"ZENG"});
                map.put("朴", new String[]{"PIAO"});
                map.put("查", new String[]{"ZHA"});
                map.put("洗", new String[]{"XIAN"});
                map.put("盖", new String[]{"GE"});
                map.put("祭", new String[]{"ZHAI"});
                map.put("种", new String[]{"CHONG"});
                map.put("秘", new String[]{"BI"});
                map.put("繁", new String[]{"PO"});
                map.put("缪", new String[]{"MIAO"});
                map.put("能", new String[]{"NAI"});
                map.put("蕃", new String[]{"PI"});
                map.put("覃", new String[]{"QIN"});
                map.put("解", new String[]{"XIE"});
                map.put("谌", new String[]{"SHAN"});
                map.put("适", new String[]{"KUO"});
                map.put("都", new String[]{"DU"});
                //map.put("阿", new String[]{"E"});
                map.put("难", new String[]{"NING"});
                map.put("黑", new String[]{"HE"});
                map.put("翟", new String[]{"ZHAI"});
                return map;
            }
        }));
    }

    private PinyinUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void init(Pinyin.Config config) {
        Pinyin.init(config);
    }

    public static void add(PinyinDict dict) {
        Pinyin.add(dict);
    }

    public static Pinyin.Config newConfig() {
        return Pinyin.newConfig();
    }

    public static String toPinyin(String str, String separator) {
        return Pinyin.toPinyin(str, separator);
    }

    public static String toPinyin(String str) {
        return Pinyin.toPinyin(str, "");
    }


    public static String toPinyin(char c) {
        return Pinyin.toPinyin(c);
    }

    public static boolean isChinese(char c) {
        return Pinyin.isChinese(c);
    }

    /**
     * 根据名字获取姓氏的拼音
     *
     * @param name      名字
     * @param isChinese 是否中文
     * @return 姓氏的拼音
     */
    public static String getSurnamePinyin(@Nullable CharSequence name, boolean isChinese) {
        if (TextUtils.isEmpty(name)) {
            return "";
        }
        char ch = name.charAt(0);
        if (isChinese) {
            if (name.length() >= 2) {
                CharSequence str = name.subSequence(0, 2);
                if (str.equals("澹台")) {
                    return "TANTAI";
                } else if (str.equals("尉迟")) {
                    return "YUCHI";
                } else if (str.equals("万俟")) {
                    return "MOQI";
                } else if (str.equals("单于")) {
                    return "CHANYU";
                }
            }
            if (SURNAMES.containsKey(ch)) {
                return SURNAMES.get(ch);
            }
        }

        if (ch >= 0x4E00 && ch <= 0x9FA5) {
            return toPinyin(String.valueOf(name), "");
        } else if ((ch >= 0x61 && ch <= 0x7a) || (ch >= 0x41 && ch <= 0x5a)) {
            return String.valueOf(ch);
        } else {
            return "#";
        }
    }

    /**
     * 根据名字获取姓氏的首字母
     *
     * @param name      名字
     * @param isChinese 是否中文
     * @return 姓氏的首字母
     */
    public static String getSurnameFirstLetter(@Nullable CharSequence name, boolean isChinese) {
        String surname = getSurnamePinyin(name, isChinese);
        return TextUtils.isEmpty(surname) ? "" : String.valueOf(surname.charAt(0));
    }
}