package cc.vihackerframework.core.context;

import cc.vihackerframework.core.entity.CurrentUser;

/**
 * 用户上下文
 * Created by Ranger on 2022/5/4.
 */
public class UserContext {

    private static ThreadLocal<CurrentUser> userHolder = new ThreadLocal<CurrentUser>();

    public static void setUser(CurrentUser currentUser) {
        userHolder.set(currentUser);
    }

    public static CurrentUser current() {
        return userHolder.get();
    }
}
