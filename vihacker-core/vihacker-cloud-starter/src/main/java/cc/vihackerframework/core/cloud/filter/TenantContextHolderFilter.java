package cc.vihackerframework.core.cloud.filter;

import cc.vihackerframework.core.constant.TenantConstant;
import cc.vihackerframework.core.context.TenantContextHolder;
import cc.vihackerframework.core.entity.CurrentUser;
import cc.vihackerframework.core.util.SecurityUtil;
import cc.vihackerframework.core.util.StringUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 上下文过滤器
 * Created by Ranger on 2022/5/4.
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TenantContextHolderFilter extends GenericFilterBean {

    @Override
    @SneakyThrows
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if(request.getRequestURI().contains("oauth")){
            filterChain.doFilter(request, response);
        }
        try {
            //优先取请求参数中的tenantId值
            String tenantId = request.getHeader(TenantConstant.VIHACKER_TENANT_ID);
            if (StringUtil.isBlank(tenantId)) {
                String token = SecurityUtil.getHeaderToken(request);
                if (StringUtil.isNotBlank(token)) {
                    //取token中的tenantId值
                    CurrentUser currentUser = SecurityUtil.getCurrentUser(request);
                    if (currentUser != null) {
                        tenantId = String.valueOf(currentUser.getTenantId());
                    }
                }
            }
            log.info("获取到的租户ID为:{}", tenantId);
            if (StringUtil.isNotBlank(tenantId)) {
                TenantContextHolder.setTenantId(tenantId);
            } else {
                if (StringUtil.isBlank(TenantContextHolder.getTenantId())) {
                    TenantContextHolder.setTenantId(TenantConstant.TENANT_ID_DEFAULT);
                }
            }
            filterChain.doFilter(request, response);
        } finally {
            TenantContextHolder.clear();
        }
    }
}
