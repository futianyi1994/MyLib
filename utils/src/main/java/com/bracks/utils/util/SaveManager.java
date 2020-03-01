package com.bracks.utils.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.List;

/**
 * good programmer.
 *
 * @date : 2020/2/29 17:04
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :保存对象到本地 Data/Data  或者 sdCard
 */
@SuppressLint("WorldReadableFiles")
public class SaveManager<T> {

    /**
     * 将对象保存到本地
     *
     * @param context
     * @param fileName 文件名
     * @param bean     对象
     * @return true 保存成功
     */
    public boolean writeObjectIntoLocal(Context context, String fileName, T bean) {
        try {
            // 通过openFileOutput方法得到一个输出流，方法参数为创建的文件名（不能有斜杠），操作模式
            @SuppressWarnings("deprecation")
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_WORLD_READABLE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            //写入
            oos.writeObject(bean);
            //关闭输入流
            fos.close();
            oos.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将对象写入sd卡
     *
     * @param fileName 文件名
     * @param bean     对象
     * @return true 保存成功
     */
    public boolean writObjectIntoSDcard(String fileName, T bean) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //获取sd卡目录
            File sdCardDir = Environment.getExternalStorageDirectory();
            File sdFile = new File(sdCardDir, fileName);
            try {
                FileOutputStream fos = new FileOutputStream(sdFile);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                //写入
                oos.writeObject(bean);
                fos.close();
                oos.close();
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 将集合写入sd卡
     *
     * @param fileName 文件名
     * @param list     集合
     * @return true 保存成功
     */
    public boolean writeListIntoSDcard(String fileName, List<T> list) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //获取sd卡目录
            File sdCardDir = Environment.getExternalStorageDirectory();
            File sdFile = new File(sdCardDir, fileName);
            try {
                FileOutputStream fos = new FileOutputStream(sdFile);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                //写入
                oos.writeObject(list);
                fos.close();
                oos.close();
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 读取本地对象
     *
     * @param context
     * @param fielName 文件名
     * @return
     */
    @SuppressWarnings("unchecked")
    public T readObjectFromLocal(Context context, String fielName) {
        T bean;
        try {
            //获得输入流
            FileInputStream fis = context.openFileInput(fielName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            bean = (T) ois.readObject();
            fis.close();
            ois.close();
            return bean;
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
            return null;
        } catch (OptionalDataException e) {
            e.printStackTrace();
            return null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 读取sd卡对象
     *
     * @param fileName 文件名
     * @return
     */
    @SuppressWarnings("unchecked")
    public T readObjectFromSdCard(String fileName) {
        //检测sd卡是否存在
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            T bean;
            File sdCardDir = Environment.getExternalStorageDirectory();
            File sdFile = new File(sdCardDir, fileName);
            try {
                FileInputStream fis = new FileInputStream(sdFile);
                ObjectInputStream ois = new ObjectInputStream(fis);
                bean = (T) ois.readObject();
                fis.close();
                ois.close();
                return bean;
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
                return null;
            } catch (OptionalDataException e) {
                e.printStackTrace();
                return null;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 读取sd卡对象
     *
     * @param fileName 文件名
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<T> readListFromSdCard(String fileName) {
        //检测sd卡是否存在
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            List<T> list;
            File sdCardDir = Environment.getExternalStorageDirectory();
            File sdFile = new File(sdCardDir, fileName);
            try {
                FileInputStream fis = new FileInputStream(sdFile);
                ObjectInputStream ois = new ObjectInputStream(fis);
                list = (List<T>) ois.readObject();
                fis.close();
                ois.close();
                return list;
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
                return null;
            } catch (OptionalDataException e) {
                e.printStackTrace();
                return null;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }
}
