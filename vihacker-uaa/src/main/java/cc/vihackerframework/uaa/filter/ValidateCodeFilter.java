package cc.vihackerframework.uaa.filter;

import cc.vihackerframework.core.api.ViHackerApiResult;
import cc.vihackerframework.uaa.properties.ValidateCodeProperties;
import cc.vihackerframework.uaa.service.ValidateCodeService;
import cc.vihackerframework.core.exception.ValidateCodeException;
import cc.vihackerframework.core.util.ViHackerUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证码过滤器
 * @Author: Ranger
 * @Date: 2021/1/25 14:44
 * @Email: wilton.icp@gmail.com
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ValidateCodeFilter extends OncePerRequestFilter {

    private final ValidateCodeService validateCodeService;
    private final ValidateCodeProperties properties;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        RequestMatcher matcher = new AntPathRequestMatcher("/oauth/token", HttpMethod.POST.toString());
        if (matcher.matches(httpServletRequest)
                && StringUtils.equalsIgnoreCase(httpServletRequest.getParameter("grant_type"), "password")) {
            try {
                if (properties.getEnable()) {
                    validateCode(httpServletRequest);
                }
                filterChain.doFilter(httpServletRequest, httpServletResponse);
            } catch (ValidateCodeException e) {
                ViHackerUtil.response(httpServletResponse, MediaType.APPLICATION_JSON_VALUE,
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ViHackerApiResult.failed(e.getMessage()));
                log.error(e.getMessage(), e);
            }
        } else {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }

    private void validateCode(HttpServletRequest httpServletRequest) throws ValidateCodeException {
        String code = httpServletRequest.getHeader("code");
        String key = httpServletRequest.getHeader("key");
        validateCodeService.check(key, code);
    }
}
