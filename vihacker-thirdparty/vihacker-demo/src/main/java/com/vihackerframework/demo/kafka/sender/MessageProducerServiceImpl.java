package com.vihackerframework.demo.kafka.sender;

import com.vihackerframework.demo.kafka.messaging.ViHackerSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;

/**
 * <p>
 * 因为我们这边是个消息生产这，所以说是个消息的源头，用Source
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/21
 */
@Slf4j
@EnableBinding(ViHackerSource.class)
public class MessageProducerServiceImpl  implements MessageProducerService {
    @Autowired
    private ViHackerSource source;

    @Override
    public void sendMes(String content) {
        boolean send = source.output().send(MessageBuilder.withPayload(content).build());
        if(send){
            log.info("消息发送成功：消息：{}",content);
        }else {
            log.error("消息发送失败：消息：{}",content);
        }
    }
}


