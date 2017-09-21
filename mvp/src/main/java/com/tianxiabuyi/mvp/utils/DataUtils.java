package com.tianxiabuyi.mvp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 文件,数据存储相关工具
 * <p>
 * Created in 2017/9/21 14:21.
 *
 * @author Wang YaoDong.
 */
public class DataUtils {

    private static final String SP_NAME = "config";

    private static SharedPreferences mSP;

    private DataUtils() {
        throw new IllegalStateException("you can't instantiate me!");
    }

    /**
     * 存储key值
     */
    public static void putStringSP(Context context, String key, String value) {
        if (mSP == null) {
            mSP = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        mSP.edit().putString(key, value).apply();
    }

    /**
     * 存储key值
     */
    public static void putIntergerSP(Context context, String key, int value) {
        if (mSP == null) {
            mSP = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        mSP.edit().putInt(key, value).apply();
    }

    /**
     * 返回key值
     */
    public static String getStringSP(Context context, String key) {
        if (mSP == null) {
            mSP = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        return mSP.getString(key, null);
    }

    /**
     * 返回key值
     */
    public static int getIntergerSP(Context context, String key) {
        if (mSP == null) {
            mSP = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        return mSP.getInt(key, -1);
    }

    /**
     * 移除key
     */
    public static void removeSPKey(Context context, String key) {
        if (mSP == null) {
            mSP = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        mSP.edit().remove(key).apply();
    }

    /**
     * 清除sp
     */
    public static void clearShareprefrence(Context context) {
        if (mSP == null) {
            mSP = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        mSP.edit().clear().apply();
    }

    /**
     * 存储对象sp
     */
    public static <T> boolean putDeviceData(Context context, String key, T device) {
        if (mSP == null) {
            mSP = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // Device为自定义类
        try {
            // 创建对象输出流，并封装字节流
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            // 将对象写入字节流
            oos.writeObject(device);
            // 将字节流编码成base64的字符串
            String oAuth_Base64 = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            mSP.edit().putString(key, oAuth_Base64).apply();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 取出对象 sp
     */
    public static <T> T getDeviceData(Context context, String key) {
        if (mSP == null) {
            mSP = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        T device = null;
        String productBase64 = mSP.getString(key, null);
        if (productBase64 == null) {
            return null;
        }
        // 读取字节
        byte[] base64 = Base64.decode(productBase64.getBytes(), Base64.DEFAULT);
        // 封装到字节流
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        try {
            // 再次封装
            ObjectInputStream bis = new ObjectInputStream(bais);
            // 读取对象
            device = (T) bis.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return device;
    }

    /**
     * 返回缓存文件夹
     */
    public static File getCacheFile(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // 获取系统管理的sd卡缓存文件
            File file = context.getExternalCacheDir();
            // 如果获取的文件为空,就使用自己定义的缓存文件夹做缓存路径
            if (file == null) {
                String packageName = context.getPackageName();
                String path = Environment.getExternalStorageDirectory().getPath()
                        + File.separator + packageName;
                file = new File(path);
                makeDirs(file);
            }
            return file;
        } else {
            return context.getCacheDir();
        }
    }

    /**
     * 创建文件夹
     */
    public static File makeDirs(File file) {
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 获取目录文件大小
     */
    public static long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += file.length();
                dirSize += getDirSize(file);
            }
        }
        return dirSize;
    }

    /**
     * 删除文件夹
     */
    public static boolean deleteDir(File dir) {
        if (dir == null) {
            return false;
        }
        if (!dir.isDirectory()) {
            return false;
        }
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                deleteDir(file);
            }
        }
        return true;
    }

    /**
     * 输入流转字符串
     */
    public static String bytyToString(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) != -1) {
            out.write(buf, 0, buf.length);
        }
        String result = out.toString();
        out.close();
        return result;
    }

}
