package cc.vihackerframework.core.log.event;

import cc.vihackerframework.core.entity.system.SysLog;
import cc.vihackerframework.core.log.feign.ISysLogProvider;
import cc.vihackerframework.core.log.properties.LogProperties;
import cc.vihackerframework.core.log.properties.LogType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 日志事件监听
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/23
 */
@Slf4j
@Component
public class LogListener {

    private ISysLogProvider sysLogProvider;
    private LogProperties logProperties;

    public LogListener(){

    }
    public LogListener(ISysLogProvider sysLogProvider, LogProperties logProperties) {
        this.sysLogProvider = sysLogProvider;
        this.logProperties = logProperties;
    }

    @Async
    @Order
    @EventListener(LogEvent.class)
    public void saveSysLog(LogEvent event) {
        SysLog sysLog = (SysLog) event.getSource();

        log.info("发送日志:{}", sysLog);
        if (logProperties.getLogType().equals(LogType.KAFKA)) {
            /**
             * 发送日志到kafka
             */
            log.info("发送日志到-kafka:{}", sysLog);
            //此处调用发送消息

        } else if(logProperties.getLogType().equals(LogType.DB)) {
            /**
             * 保存到数据库
             */
            sysLogProvider.saveLog(sysLog);
            log.info("保存日志的数据库成功:{}", sysLog);
        }
    }
}
