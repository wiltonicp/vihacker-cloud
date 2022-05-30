package cc.vihackerframework.uaa.controller;

import cc.vihackerframework.core.api.ViHackerApiResult;
import cc.vihackerframework.core.datasource.entity.QuerySearch;
import cc.vihackerframework.core.log.annotation.LogEndpoint;
import cc.vihackerframework.uaa.entity.OauthClientDetails;
import cc.vihackerframework.uaa.service.OauthClientDetailsService;
import cc.vihackerframework.core.exception.Asserts;
import cc.vihackerframework.core.util.ViHackerUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * OauthClientDetails管理
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/8
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("client")
@Api(tags = "Oauth客户端管理")
public class OauthClientDetailsController {

    private final OauthClientDetailsService oauthClientDetailsService;


    @GetMapping("check/{clientId}")
    @ApiOperation(value = "校验客户端", notes = "客户端")
    public boolean checkUserName(@NotBlank(message = "{required}") @PathVariable String clientId) {
        OauthClientDetails client = this.oauthClientDetailsService.findById(clientId);
        return client == null;
    }

    @GetMapping("secret/{clientId}")
    @PreAuthorize("hasAuthority('client:decrypt')")
    @ApiOperation(value = "校验客户端", notes = "客户端")
    public ViHackerApiResult getOriginClientSecret(@NotBlank(message = "{required}") @PathVariable String clientId) {
        OauthClientDetails client = this.oauthClientDetailsService.findById(clientId);
        String origin = client != null ? client.getOriginSecret() : StringUtils.EMPTY;
        return ViHackerApiResult.data(origin);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('client:view')")
    @ApiOperation(value = "分页查询客户端", notes = "客户端")
    @LogEndpoint(value = "分页查询客户端", exception = "分页查询客户端失败")
    public ViHackerApiResult oauthClientDetailsList(QuerySearch search) {
        return ViHackerApiResult.data(this.oauthClientDetailsService.findOauthClientDetails(search));
    }


    @PostMapping
    @PreAuthorize("hasAuthority('client:add')")
    @ApiOperation(value = "新增客户端", notes = "客户端")
    @LogEndpoint(value = "新增客户端",exception = "新增客户端失败")
    public ViHackerApiResult addOauthClientDetails(@Valid @RequestBody OauthClientDetails oAuthClientDetails) {
        return ViHackerApiResult.success(this.oauthClientDetailsService.createOauthClientDetails(oAuthClientDetails));
    }

    @PutMapping
    @PreAuthorize("hasAuthority('client:update')")
    @ApiOperation(value = "修改客户端", notes = "客户端")
    @LogEndpoint(value = "修改客户端",exception = "修改客户端失败")
    public ViHackerApiResult updateOauthClientDetails(@Valid @RequestBody OauthClientDetails oAuthClientDetails) {
       return ViHackerApiResult.success(this.oauthClientDetailsService.updateOauthClientDetails(oAuthClientDetails));
    }

    @DeleteMapping("/{clientIds}")
    @PreAuthorize("hasAuthority('client:delete')")
    @ApiOperation(value = "删除客户端", notes = "客户端")
    @LogEndpoint(value = "删除客户端",exception = "删除客户端失败")
    public ViHackerApiResult deleteOauthClientDetails(@NotBlank(message = "{required}") @PathVariable String clientIds) {
        return ViHackerApiResult.success(this.oauthClientDetailsService.deleteOauthClientDetails(clientIds));
    }
}
