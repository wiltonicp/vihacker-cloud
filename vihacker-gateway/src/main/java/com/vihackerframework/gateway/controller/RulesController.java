package com.vihackerframework.gateway.controller;

import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/15
 */
@RestController
public class RulesController {

    @GetMapping("/api")
    @SentinelResource("api")
    public Set<ApiDefinition> apiRules() {
        return GatewayApiDefinitionManager.getApiDefinitions();
    }

    @GetMapping("/gateway")
    @SentinelResource("gateway")
    public Set<GatewayFlowRule> apiGateway() {
        return GatewayRuleManager.getRules();
    }

    @GetMapping("/flow")
    @SentinelResource("flow")
    public List<FlowRule> apiFlow() {
        return FlowRuleManager.getRules();
    }
}
