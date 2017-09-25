package com.tianxiabuyi.txmvp.dagger.component;

import com.tianxiabuyi.mvp.dagger.component.AppComponent;
import com.tianxiabuyi.mvp.dagger.scope.ActivityScope;
import com.tianxiabuyi.txmvp.dagger.module.UserModule;
import com.tianxiabuyi.txmvp.ui.activity.MainActivity;

import dagger.Component;

/**
 * Created in 2017/9/21 19:51.
 *
 * @author Wang YaoDong.
 */
@ActivityScope
@Component(modules = UserModule.class, dependencies = AppComponent.class)
public interface UserComponent {
    void inject(MainActivity activity);
}
