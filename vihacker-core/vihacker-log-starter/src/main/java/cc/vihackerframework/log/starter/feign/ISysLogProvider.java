package cc.vihackerframework.log.starter.feign;

import cc.vihackerframework.core.api.ViHackerApiResult;
import cc.vihackerframework.core.entity.system.SysLog;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 调用vihacker-system存储日志
 *
 * Created by Ranger on 2022/2/20
 */
@FeignClient(value = "vihacker-system")
public interface ISysLogProvider {

    /**
     * 日志打包保存
     * @param sysLog
     * @return
     */
    @PostMapping("provider/log/set")
    ViHackerApiResult saveLog(@RequestBody SysLog sysLog);
}
