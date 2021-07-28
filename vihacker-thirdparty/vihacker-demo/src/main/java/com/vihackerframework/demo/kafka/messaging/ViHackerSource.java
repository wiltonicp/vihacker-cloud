package com.vihackerframework.demo.kafka.messaging;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * <p>
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/21
 */
public interface ViHackerSource {

    String OUTPUT_NAME="outputChannel";

    @Output(OUTPUT_NAME)
    MessageChannel output();
}
