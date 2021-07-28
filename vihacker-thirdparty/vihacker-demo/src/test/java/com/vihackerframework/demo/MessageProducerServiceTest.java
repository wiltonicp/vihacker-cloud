package com.vihackerframework.demo;

import com.vihackerframework.demo.kafka.sender.MessageProducerService;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * <p>
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/21
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MessageProducerServiceTest {
    @Autowired
    private MessageProducerService messageProducerService;

    @Test
    @SneakyThrows
    public void testSendMes(){
        for (int i = 0; i < 100; i++) {
            messageProducerService.sendMes("我是第 " + i + "条消息");
            Thread.sleep(1000);
        }
    }
}

