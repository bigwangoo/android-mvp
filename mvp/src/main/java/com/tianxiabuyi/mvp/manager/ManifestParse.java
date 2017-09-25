package com.tianxiabuyi.mvp.manager;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.tianxiabuyi.mvp.dagger.model.GlobalConfigModule;
import com.tianxiabuyi.mvp.manager.ConfigModule;

import java.util.ArrayList;
import java.util.List;

/**
 * AndroidManifest 配置解析工具
 * <p>
 * Created in 2017/9/22 9:05.
 *
 * @author Wang YaoDong.
 */
public class ManifestParse {

    private static final String MODULE_VALUE = "GlobalConfigModule";

    /**
     * 解析配置
     */
    public static List<ConfigModule> parse(Context context) {
        List<ConfigModule> modules = new ArrayList<>();
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = appInfo.metaData;
            if (bundle != null) {
                for (String key : bundle.keySet()) {
                    if (MODULE_VALUE.equals(bundle.get(key))) {
                        modules.add(parseModule(key));
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Unable to find metadata to parse GlobalConfigModule", e);
        }
        return modules;
    }

    /**
     * 反射初始化 className
     */
    private static ConfigModule parseModule(String className) {
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Unable to find GlobalConfigModule implementation", e);
        }
        Object module;
        try {
            module = clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Unable to instantiate GlobalConfigModule implementation for " + clazz, e);
        }
        if (!(module instanceof ConfigModule)) {
            throw new RuntimeException("Expected instanceof GlobalConfigModule, but found: " + module);
        }
        return (ConfigModule) module;
    }
}
