package cc.vihackerframework.core.context;

import cc.vihackerframework.core.entity.system.AdminAuthUser;

/**
 * 用户上下文
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/22
 */
public class UserContext {

    private static ThreadLocal<AdminAuthUser> userHolder = new ThreadLocal<AdminAuthUser>();

    public static void setUser(AdminAuthUser loginUser) {
        userHolder.set(loginUser);
    }

    public static AdminAuthUser getUser() {
        return userHolder.get();
    }
}
