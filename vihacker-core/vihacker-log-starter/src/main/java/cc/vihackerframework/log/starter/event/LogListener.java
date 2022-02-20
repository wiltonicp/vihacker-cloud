package cc.vihackerframework.log.starter.event;

import cc.vihackerframework.core.entity.system.SysLog;
import cc.vihackerframework.log.starter.mapper.ISysLogMapper;
import cc.vihackerframework.log.starter.properties.LogProperties;
import cc.vihackerframework.log.starter.properties.LogType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired(required = false)
    private ISysLogMapper sysLogMapper;

    @Autowired(required = false)
    private LogProperties logProperties;
    public LogListener(){

    }

    @Async
    @Order
    @EventListener(LogEvent.class)
    public void saveSysLog(LogEvent event) {
        SysLog sysLog = (SysLog) event.getSource();

        if (logProperties.getLogType().equals(LogType.KAFKA)) {
            /**
             * 发送日志到kafka
             */
            log.info("发送日志到-kafka:{}", sysLog);
            //此处调用发送消息
        } else {
            /**
             * 保存到数据库
             */
            sysLogMapper.insert(sysLog);
            log.info("保存日志的数据库成功:{}", sysLog);
        }
    }
}
