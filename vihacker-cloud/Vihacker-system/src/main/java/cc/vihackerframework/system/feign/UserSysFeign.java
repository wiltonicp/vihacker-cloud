package cc.vihackerframework.system.feign;

import cc.vihackerframework.core.api.ViHackerApiResult;
import cc.vihackerframework.core.entity.system.SysUser;
import cc.vihackerframework.system.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jiangshanchen
 * @title: UserSysMapper
 * @projectName vihacker-cloud-master
 * @description: TODO
 * @date 2022/3/1下午8:27
 */
@RestController
@AllArgsConstructor
public class UserSysFeign{

    private IUserService userService;

    @PostMapping("/get/user")
    public ViHackerApiResult getUserById(long id){

        SysUser user = userService.getById(id);
        return ViHackerApiResult.data(user);
    };

}
