package cc.vihackerframework.demo.kafka.task;

import cc.vihackerframework.demo.kafka.sender.MessageProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * <p>
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/21
 */
@Component
public class SenderTask {

    @Autowired
    private MessageProducerService messageProducerService;

    @Scheduled(cron = "*/5 * * * * *")
    public void testSendMes() throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            messageProducerService.sendMes("我是第 " + i + "条消息");
            Thread.sleep(500);
        }
    }
}
