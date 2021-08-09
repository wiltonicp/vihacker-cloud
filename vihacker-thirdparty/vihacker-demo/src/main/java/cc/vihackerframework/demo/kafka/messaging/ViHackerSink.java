package cc.vihackerframework.demo.kafka.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * <p>
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/21
 */
public interface ViHackerSink {

    String INPUT_NAME="inputChannel";

    @Input(INPUT_NAME)
    SubscribableChannel input();
}
