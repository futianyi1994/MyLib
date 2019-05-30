package com.bracks.futia.mylib.utils.save;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import com.blankj.utilcode.util.EncryptUtils;
import com.bracks.futia.mylib.utils.CommonUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * good programmer.
 *
 * @date : 2018-12-13 上午 11:44
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class SPUtils {
    private static final String SP_NAME = "config";
    private static final String AES_KEY = "aes_key";
    private static final String AES_TRANSFORMATION = "aes";

    public static SharedPreferences getSp(Context mContext, String preferenceName) {
        return mContext.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
    }

    public static SharedPreferences getSp() {
        return getSp(CommonUtils.getContext(), SP_NAME);
    }

    /**
     * 万能的put方法     (能存储String/int/boolean类型的值)
     *
     * @param key
     * @param value
     */
    public static boolean put(String key, Object value) {
        SharedPreferences.Editor editor = getSp().edit();
        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else {
            editor.putString(key, value.toString());
        }
        return SharedPreferencesCompat.apply(editor);
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object get(String key, Object defaultObject) {
        SharedPreferences sp = getSp();
        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }
        return null;
    }

    /**
     * 万能的put方法     (能存储String/int/boolean类型的加密值)
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean putEncrypt(String key, Object value) {
        String target = "";
        if (value instanceof Boolean) {
            target = EncryptUtils.encryptAES2HexString(
                    ((boolean) value ? "1" : "0").getBytes()
                    , AES_KEY.getBytes()
                    , AES_TRANSFORMATION,
                    null
            );
        } else {
            target = EncryptUtils.encryptAES2HexString(
                    value.toString().getBytes()
                    , AES_KEY.getBytes()
                    , AES_TRANSFORMATION,
                    null
            );
        }
        return put(key, target);
    }

    /**
     * 得到机密后的保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getDecrypt(String key, Object defaultObject) {
        String source = getString(key);
        if (TextUtils.isEmpty(source)) {
            return defaultObject;
        }
        byte[] bytes = EncryptUtils.decryptHexStringAES(
                source,
                AES_KEY.getBytes(),
                AES_TRANSFORMATION,
                null);
        source = new String(bytes);
        if (defaultObject instanceof String) {
            return source;
        } else if (defaultObject instanceof Integer) {
            return Integer.parseInt(source);
        } else if (defaultObject instanceof Boolean) {
            return "1".equals(source);
        } else if (defaultObject instanceof Float) {
            return Float.parseFloat(source);
        } else if (defaultObject instanceof Long) {
            return Long.parseLong(source);
        }
        return null;
    }

    /**
     * 保存List
     *
     * @param key
     * @param datalist
     */
    public static <T> boolean putList(String key, List<T> datalist) {
        if (null == datalist || datalist.size() <= 0) {
            return false;
        }
        //转换成json数据，再保存
        return put(key, new Gson().toJson(datalist));
    }

    /**
     * 获取List
     *
     * @param key
     * @return
     */
    public static <T> List<T> getList(String key) {
        String strJson = getString(key, null);
        return strJson == null
                ?
                new ArrayList<>()
                :
                new Gson()
                        .fromJson(strJson, new TypeToken<List<T>>() {
                        }.getType());
    }

    /**
     * 获取List
     * 需要传入Class[].class解决泛型类型擦除问题
     *
     * @param key
     * @param classOfT
     * @param <T>
     * @return
     */
    public static <T> List<T> getList(String key, Class<T[]> classOfT) {
        String strJson = getString(key, null);
        return strJson == null
                ?
                new ArrayList<>()
                :
                Arrays.asList(new Gson().fromJson(strJson, classOfT));
    }

    /**
     * 保存List<Map<String, Integer>>
     *
     * @param key
     * @param datas
     */
    public static boolean putListMap(String key, List<Map<String, Integer>> datas) {
        JSONArray mJsonArray = new JSONArray();
        for (int i = 0; i < datas.size(); i++) {
            Map<String, Integer> itemMap = datas.get(i);
            Iterator<Map.Entry<String, Integer>> iterator = itemMap.entrySet().iterator();

            JSONObject object = new JSONObject();

            while (iterator.hasNext()) {
                Map.Entry<String, Integer> entry = iterator.next();
                try {
                    object.put(entry.getKey(), entry.getValue());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            mJsonArray.put(object);
        }
        return put(key, mJsonArray.toString());
    }

    /**
     * 获得List<Map<String, Integer>>
     *
     * @param key
     * @return
     */
    public static List<Map<String, Integer>> getListMap(String key) {
        List<Map<String, Integer>> datas = new ArrayList<Map<String, Integer>>();
        try {
            JSONArray array = new JSONArray(getString(key));
            for (int i = 0; i < array.length(); i++) {
                JSONObject itemObject = array.getJSONObject(i);
                Map<String, Integer> itemMap = new HashMap<String, Integer>();
                JSONArray names = itemObject.names();
                if (names != null) {
                    for (int j = 0; j < names.length(); j++) {
                        String name = names.getString(j);
                        Integer value = itemObject.getInt(name);
                        itemMap.put(name, value);
                    }
                }
                datas.add(itemMap);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return datas;
    }

    /**
     * 保存序列化对象
     *
     * @param key
     * @param settings
     */
    public static boolean putObject(String key, Object settings) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(settings);
            String temp = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            return put(key, temp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取序列化对象
     *
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getObject(String key, Object defaultObject) {
        ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decode(getString(key).getBytes(), Base64.DEFAULT));
        Object settings = defaultObject;
        try {
            ObjectInputStream ois = new ObjectInputStream(bais);
            settings = (Object) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return settings;
    }

    /**
     * 获取String
     *
     * @param key
     * @return
     */
    public static String getString(String key) {
        return getString(key, "");
    }

    /**
     * 获取String
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getString(String key, String defaultValue) {
        return getSp().getString(key, defaultValue);
    }

    /**
     * 获取int
     *
     * @param key
     * @return
     */
    public static int getInt(String key) {
        return getSp().getInt(key, 0);
    }

    /**
     * 获取int
     *
     * @param key
     * @param defalut
     * @return
     */
    public static int getInt(String key, int defalut) {
        return getSp().getInt(key, defalut);
    }

    /**
     * 获取Boolean
     *
     * @param key
     * @return
     */
    public static boolean getBoolean(String key) {
        return getSp().getBoolean(key, false);
    }

    /**
     * 获取Boolean
     *
     * @param key
     * @param defalut
     * @return
     */
    public static boolean getBoolean(String key, boolean defalut) {
        return getSp().getBoolean(key, defalut);
    }

    /**
     * 获取Long
     *
     * @param key
     * @return
     */
    public static long getLong(String key) {
        return getSp().getLong(key, 0L);
    }

    /**
     * 获取Long
     *
     * @param key
     * @param defualt
     * @return
     */
    public static long getLong(String key, long defualt) {
        return getSp().getLong(key, defualt);
    }

    /**
     * remove
     *
     * @param key
     * @return
     */
    public static boolean remove(String key) {
        SharedPreferences.Editor edit = getSp().edit();
        edit.remove(key);
        return SharedPreferencesCompat.apply(edit);
    }

    /**
     * 清空首选项
     */
    public static boolean clear() {
        SharedPreferences.Editor edit = getSp().edit();
        edit.clear();
        return SharedPreferencesCompat.apply(edit);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param key
     * @return
     */
    public static boolean contains(String key) {
        SharedPreferences sp = getSp();
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @return
     */
    public static Map<String, ?> getAll() {
        return getSp().getAll();
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     */
    private static class SharedPreferencesCompat {
        private static final Method APPLY_METHOD = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static boolean apply(SharedPreferences.Editor editor) {
            try {
                if (APPLY_METHOD != null) {
                    APPLY_METHOD.invoke(editor);
                    return true;
                }
            } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return editor.commit();
        }
    }

    /**
     * 修改SharedPreferences默认保存路径
     * 在Application初始化时调用该方法即可
     *
     * @param context
     * @param pathname
     */
    public static void changeSPPath(Context context, String pathname) {
        try {
            Field field;
            //获取ContextWrapper对象中的mBase变量。该变量保存了ContextImpl对象
            field = ContextWrapper.class.getDeclaredField("mBase");
            field.setAccessible(true);
            //获取mBase变量
            Object obj = field.get(context);
            //获取ContextImpl。mPreferencesDir变量，该变量保存了数据文件的保存路径
            field = obj.getClass().getDeclaredField("mPreferencesDir");
            field.setAccessible(true);
            //创建自定义路径
            File file = new File(pathname);
            //修改mPreferencesDir变量的值
            field.set(obj, file);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
