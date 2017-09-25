package com.tianxiabuyi.txmvp.mvp.model.bean;

/**
 * Created in 2017/9/21 15:34.
 *
 * @author Wang YaoDong.
 */
public class UserBean {

    private final int id;
    private final String login;
    private final String avatar_url;

    public UserBean(int id, String login, String avatar_url) {
        this.id = id;
        this.login = login;
        this.avatar_url = avatar_url;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getAvatarUrl() {
        if (avatar_url.isEmpty()) return avatar_url;
        return avatar_url.split("\\?")[0];
    }

    @Override
    public String toString() {
        return "id -> " + id + " login -> " + login;
    }
}
