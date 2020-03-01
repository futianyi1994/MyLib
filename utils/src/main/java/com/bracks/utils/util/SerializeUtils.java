package com.bracks.utils.util;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * good programmer.
 *
 * @date : 2018/9/3 15:41
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class SerializeUtils {
    private static final String TAG = "SerializeUtils";

    /**
     * 序列化对象
     *
     * @param object
     * @return
     * @throws IOException
     */
    public static String serialize(Object object) {
        double startTime;
        double endTime;
        startTime = System.currentTimeMillis();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            String serStr = byteArrayOutputStream.toString("ISO-8859-1");
            serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
            objectOutputStream.close();
            byteArrayOutputStream.close();
            Log.d("serial", "serialize str =" + serStr);
            endTime = System.currentTimeMillis();
            Log.d(TAG, "序列化耗时为:" + (endTime - startTime));
            return serStr;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 反序列化对象
     *
     * @param str
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static Object deSerialization(String str) {
        double startTime;
        double endTime;
        startTime = System.currentTimeMillis();
        String redStr = null;
        try {
            redStr = java.net.URLDecoder.decode(str, "UTF-8");
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(redStr.getBytes(StandardCharsets.ISO_8859_1));
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object object = objectInputStream.readObject();
            objectInputStream.close();
            byteArrayInputStream.close();
            endTime = System.currentTimeMillis();
            Log.d(TAG, "反序列化耗时为:" + (endTime - startTime));
            return object;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
