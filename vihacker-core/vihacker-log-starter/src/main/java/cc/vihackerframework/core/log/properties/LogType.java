package cc.vihackerframework.core.log.properties;

/**
 * 日志记录类型
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/23
 */
public enum LogType {

    /**
     * 记录日志到本地
     */
    LOGGER,
    /**
     * 记录日志到数据库
     */
    DB,
    /**
     * 记录日志到KAFKA
     */
    KAFKA,
    ;
}
