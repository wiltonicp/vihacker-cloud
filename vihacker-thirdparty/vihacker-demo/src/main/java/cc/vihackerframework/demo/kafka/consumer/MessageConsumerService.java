package cc.vihackerframework.demo.kafka.consumer;

import cc.vihackerframework.demo.kafka.messaging.ViHackerSink;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;

/**
 * <p>
 * 这里是消息消费者，所以就就是接收者，所以使用Sink
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/21
 */
@EnableBinding(ViHackerSink.class)
public class MessageConsumerService {

    @StreamListener(ViHackerSink.INPUT_NAME)
    public void recevieMes(Message<String> message){
        System.out.println("-----我接受到的消息是："+message.getPayload());
    }
}
