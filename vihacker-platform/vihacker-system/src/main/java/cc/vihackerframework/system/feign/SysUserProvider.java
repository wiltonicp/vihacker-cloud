package cc.vihackerframework.system.feign;

import cc.vihackerframework.core.api.ViHackerApiResult;
import cc.vihackerframework.system.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户远程调用
 * Created by Ranger on 2022/4/5.
 */
@Slf4j
@RestController
@AllArgsConstructor
@Api(tags = "用户远程调用")
public class SysUserProvider {

    private final IUserService userService;

    @PostMapping("provider/user/get")
    @ApiOperation(value = "根据 id 查询用户", notes = "查询用户")
    public ViHackerApiResult selectUserById(Long userId) {
        return ViHackerApiResult.data(userService.getById(userId));
    }
}
