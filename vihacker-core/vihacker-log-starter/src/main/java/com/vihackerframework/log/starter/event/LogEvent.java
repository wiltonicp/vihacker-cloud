package com.vihackerframework.log.starter.event;

import com.vihackerframework.core.entity.system.SysLog;
import org.springframework.context.ApplicationEvent;

/**
 * 日志事件
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/23
 */
public class LogEvent extends ApplicationEvent {

    public LogEvent(SysLog sysLog) {
        super(sysLog);
    }
}
