package cc.vihackerframework.core.web.configure;

import cc.vihackerframework.core.util.ViHackerUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;

/**
 * @Description
 * @Author: Ranger
 * @Date: 2021/1/18 17:42
 * @Email: wilton.icp@gmail.com
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ViHackerStartedUpRunnerConfigure implements ApplicationRunner {
    private final ConfigurableApplicationContext context;
    private final Environment environment;

    @Override
    public void run(ApplicationArguments args) throws UnknownHostException {
        if (context.isActive()) {
            ViHackerUtil.printStartUpBanner(environment);
        }
    }
}
