package com.tianxiabuyi.txmvp.model.api.service;

import com.tianxiabuyi.txmvp.model.bean.UserBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created in 2017/9/21 20:30.
 *
 * @author Wang YaoDong.
 */
public interface UserService {

    String HEADER_API_VERSION = "Accept: application/vnd.github.v3+json";

    @Headers({HEADER_API_VERSION})
    @GET("/users")
    Observable<List<UserBean>> getUsers(@Query("since") int lastIdQueried,
                                        @Query("per_page") int perPage);
}
