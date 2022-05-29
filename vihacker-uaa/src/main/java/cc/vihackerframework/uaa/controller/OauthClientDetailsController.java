package cc.vihackerframework.uaa.controller;

import cc.vihackerframework.core.api.ViHackerApiResult;
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
    public ViHackerApiResult oauthClientDetailsList(QuerySearch search, OauthClientDetails oAuthClientDetails) {
        Map<String, Object> dataTable = ViHackerUtil.getDataTable(this.oauthClientDetailsService.findOauthClientDetails(request, oAuthClientDetails));
        return ViHackerApiResult.data(dataTable);
    }


    @PostMapping
    @PreAuthorize("hasAuthority('client:add')")
    @ApiOperation(value = "新增客户端", notes = "客户端")
    public void addOauthClientDetails(@Valid OauthClientDetails oAuthClientDetails) {
        try {
            this.oauthClientDetailsService.createOauthClientDetails(oAuthClientDetails);
        } catch (Exception e) {
            String message = "新增客户端失败";
            log.error(message, e);
            Asserts.fail(message);
        }
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('client:delete')")
    @ApiOperation(value = "删除客户端", notes = "客户端")
    public void deleteOauthClientDetails(@NotBlank(message = "{required}") String clientIds) {
        try {
            this.oauthClientDetailsService.deleteOauthClientDetails(clientIds);
        } catch (Exception e) {
            String message = "删除客户端失败";
            log.error(message, e);
            Asserts.fail(message);
        }
    }

    @PutMapping
    @PreAuthorize("hasAuthority('client:update')")
    @ApiOperation(value = "删除客户端", notes = "客户端")
    public void updateOauthClientDetails(@Valid OauthClientDetails oAuthClientDetails) {
        try {
            this.oauthClientDetailsService.updateOauthClientDetails(oAuthClientDetails);
        } catch (Exception e) {
            String message = "修改客户端失败";
            log.error(message, e);
            Asserts.fail(message);
        }
    }
}
