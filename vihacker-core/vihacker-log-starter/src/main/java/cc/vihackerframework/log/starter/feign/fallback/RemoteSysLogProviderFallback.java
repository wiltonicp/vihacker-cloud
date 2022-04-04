package cc.vihackerframework.log.starter.feign.fallback;

import cc.vihackerframework.core.api.ViHackerApiResult;
import cc.vihackerframework.log.starter.feign.ISysLogProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * Created by Ranger on 2022/3/6
 */
@Slf4j
//@Component
public class RemoteSysLogProviderFallback implements FallbackFactory<ISysLogProvider> {
    @Override
    public ISysLogProvider create(Throwable throwable) {
        return sysLog -> ViHackerApiResult.failed("调用失败");

    }
}
