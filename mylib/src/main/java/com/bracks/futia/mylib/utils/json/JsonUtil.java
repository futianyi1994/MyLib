package com.bracks.futia.mylib.utils.json;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author futia
 * @date 2017/10/4
 */
public class JsonUtil {

    public static GsonBuilder getGsonBuilder() {
        return new GsonBuilder()
                //设置日期的格式
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                //避免gson解析json时出现空指针
                .registerTypeAdapter(String.class, new StringConverter())
                //Gson解析服务器返回json字符串 为null做处理
                .registerTypeAdapterFactory(new NullStringEmptyTypeAdapterFactory())
                //序列化为null对象
                .serializeNulls();
    }

    /**
     * 把对象转换成字符串，包括null值也会转换成字符串
     *
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        Gson gson = getGsonBuilder().create();
        return gson.toJson(object);
    }

    /**
     * 排除掉没有被expose注解的字段
     *
     * @param object
     * @return
     */
    public static String toJsonWithExpose(Object object) {
        Gson gson = getGsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        return gson.toJson(object);
    }

    /**
     * 把json字符串解析成对象
     *
     * @param json
     * @param classOfT
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String json, Class<T> classOfT) {
        //建造者模式设置不同的配置
        Gson gson = getGsonBuilder()
                //防止对网址乱码 忽略对特殊字符的转换
                .disableHtmlEscaping()
                .create();

        return gson.fromJson(json, classOfT);
    }

    /**
     * json中动态字段取值
     *
     * @param json : json格式如下
     *             {
     *             "code": 200,
     *             "msg": "成功",
     *             "data": {
     *             "1.png": "common/12958fbf-9436-4180-a989-6827572f6f6c.png"
     *             "2.png": "common/12958fbf-9436-4180-a989-682757easdd.png"
     *             }
     *             }
     * @return
     */
    public static List<String> jsonParseDynamicKey(String json) {
        List<String> values = new ArrayList<>();
        JSONObject jsonObject = null;
        JSONObject mapJSON = null;
        try {
            jsonObject = new JSONObject(json);
            mapJSON = jsonObject.getJSONObject("data");
            // 动态获取key值
            Iterator<String> iterator = mapJSON.keys();
            while (iterator.hasNext()) {
                // 获得key
                String key = iterator.next();
                // 根据key获得value, value也可以是JSONObject,JSONArray,使用对应的参数接收即可
                String value = mapJSON.getString(key);
                values.add(value);
            }
            return values;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 实现了序列化接口
     * 对为null的字段进行转换
     * 避免gson解析json时出现空指针
     */
    public static class StringConverter implements JsonSerializer<String>, JsonDeserializer<String> {
        /**
         * 字符串为null 转换成"",否则为字符串类型
         *
         * @param json
         * @param typeOfT
         * @param context
         * @return
         * @throws JsonParseException
         */
        @Override
        public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return json.getAsJsonPrimitive().getAsString();
        }

        @Override
        public JsonElement serialize(String src, Type typeOfSrc, JsonSerializationContext context) {
            return src == null || "null".equals(src) ? new JsonPrimitive("") : new JsonPrimitive(src.toString());
        }
    }

    /**
     * Gson解析服务器返回json字符串 对String 为null做处理
     */
    public static class StringNullAdapter extends TypeAdapter<String> {
        @Override
        public String read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return "";
            }
            return reader.nextString();
        }
        @Override
        public void write(JsonWriter writer, String value) throws IOException {
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.value(value);
        }
    }

    /**
     *  对字符串类型做处理
     * @param <T>
     */
    public static class NullStringEmptyTypeAdapterFactory<T> implements TypeAdapterFactory {
        @Override
        @SuppressWarnings("unchecked")
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            Class<T> rawType = (Class<T>) type.getRawType();
            if (rawType != String.class) {
                return null;
            }
            return (TypeAdapter<T>) new StringNullAdapter();
        }
    }

}
