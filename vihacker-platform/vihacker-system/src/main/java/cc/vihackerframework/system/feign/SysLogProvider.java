package cc.vihackerframework.system.feign;

import cc.vihackerframework.core.api.ViHackerApiResult;
import cc.vihackerframework.core.entity.system.SysLog;
import cc.vihackerframework.core.log.feign.ISysLogProvider;
import cc.vihackerframework.system.service.ISysLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Ranger on 2022/2/20
 */
@Slf4j
@RestController
@AllArgsConstructor
@Api(tags = "日志远程调用")
public class SysLogProvider implements ISysLogProvider {

    private final ISysLogService sysLogService;

    @Override
    @PostMapping("provider/log/set")
    @ApiOperation(value = "日志打包保存", notes = "日志保存")
    public ViHackerApiResult saveLog(SysLog sysLog) {
        return ViHackerApiResult.data(sysLogService.save(sysLog));
    }
}
