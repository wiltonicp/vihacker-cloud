package cc.vihackerframework.core.auth.context;

import cc.vihackerframework.core.auth.entity.AdminAuthUser;
import cn.hutool.core.util.ObjectUtil;

/**
 * Created By Ranger on 2022/3/16
 */
public class UserContext {

    private static ThreadLocal<AdminAuthUser> threadLocal = new ThreadLocal<>();

    public static AdminAuthUser current() {
        return threadLocal.get();
    }

    public static String currentUserName() {
        AdminAuthUser userInfo = threadLocal.get();
        if (ObjectUtil.isNotNull(userInfo)) {
            return userInfo.getUsername();
        }
        return null;
    }

    public static void setUserInfo(AdminAuthUser userInfo) {
        threadLocal.set(userInfo);
    }
}
