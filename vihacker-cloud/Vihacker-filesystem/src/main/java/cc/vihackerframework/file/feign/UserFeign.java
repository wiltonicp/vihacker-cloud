package cc.vihackerframework.file.feign;

import cc.vihackerframework.core.entity.system.SysUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author jiangshanchen
 * @title: UserFeign
 * @projectName vihacker-cloud-master
 * @description: TODO
 * @date 2022/3/1下午9:44
 */
@FeignClient("znjx-system")
public interface UserFeign {

    @PostMapping("/get/user")
    SysUser getUserById(long id);
}
