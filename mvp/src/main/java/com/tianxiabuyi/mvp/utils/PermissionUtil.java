package com.tianxiabuyi.mvp.utils;

import android.Manifest;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tianxiabuyi.mvp.http.error.ErrorHandleSubscriber;
import com.tianxiabuyi.mvp.http.error.RxErrorHandler;

import java.util.ArrayList;
import java.util.List;


import timber.log.Timber;

/**
 * 权限管理工具
 * <p>
 * Created in 2017/9/21 15:44.
 *
 * @author Wang YaoDong.
 */
public class PermissionUtil {

    public static final String TAG = "Permission";

    private PermissionUtil() {
        throw new IllegalStateException("you can't instantiate me!");
    }

    public interface RequestPermission {
        void onRequestPermissionSuccess();

        void onRequestPermissionFailure();
    }

    public static void requestPermission(final RequestPermission requestPermission,
                                         RxPermissions rxPermissions,
                                         RxErrorHandler errorHandler, String... permissions) {
        if (permissions == null || permissions.length == 0) return;

        List<String> needRequest = new ArrayList<>();
        //过滤调已经申请过的权限
        for (String permission : permissions) {
            if (!rxPermissions.isGranted(permission)) {
                needRequest.add(permission);
            }
        }

        if (needRequest.size() == 0) {
            //全部权限都已经申请过，直接执行操作
            requestPermission.onRequestPermissionSuccess();
        } else {
            //没有申请过,则开始申请
            rxPermissions.request(needRequest.toArray(new String[needRequest.size()]))
                    .subscribe(new ErrorHandleSubscriber<Boolean>(errorHandler) {
                        @Override
                        public void onNext(Boolean granted) {
                            if (granted) {
                                Timber.tag(TAG).d("Request permissions success");
                                requestPermission.onRequestPermissionSuccess();
                            } else {
                                Timber.tag(TAG).d("Request permissions failure");
                                requestPermission.onRequestPermissionFailure();
                            }
                        }
                    });
        }
    }

    /**
     * 请求获取手机状态的权限
     */
    public static void readPhonestate(RequestPermission requestPermission,
                                      RxPermissions rxPermissions,
                                      RxErrorHandler errorHandler) {
        requestPermission(requestPermission, rxPermissions, errorHandler,
                Manifest.permission.READ_PHONE_STATE);
    }

    /**
     * 请求外部存储的权限
     */
    public static void externalStorage(RequestPermission requestPermission,
                                       RxPermissions rxPermissions,
                                       RxErrorHandler errorHandler) {
        requestPermission(requestPermission, rxPermissions, errorHandler,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    /**
     * 请求摄像头权限
     */
    public static void launchCamera(RequestPermission requestPermission,
                                    RxPermissions rxPermissions,
                                    RxErrorHandler errorHandler) {
        requestPermission(requestPermission, rxPermissions, errorHandler,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA);
    }

    /**
     * 请求发送短信权限
     */
    public static void sendSms(RequestPermission requestPermission,
                               RxPermissions rxPermissions,
                               RxErrorHandler errorHandler) {
        requestPermission(requestPermission, rxPermissions, errorHandler,
                Manifest.permission.SEND_SMS);
    }

    /**
     * 请求打电话权限
     */
    public static void callPhone(RequestPermission requestPermission,
                                 RxPermissions rxPermissions,
                                 RxErrorHandler errorHandler) {
        requestPermission(requestPermission, rxPermissions, errorHandler,
                Manifest.permission.CALL_PHONE);
    }
}
