package com.tianxiabuyi.mvp.dagger.scope;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created in 2017/9/21 20:21.
 *
 * @author Wang YaoDong.
 */
@Scope
@Documented
@Retention(RUNTIME)
public @interface FragmentScope {}
