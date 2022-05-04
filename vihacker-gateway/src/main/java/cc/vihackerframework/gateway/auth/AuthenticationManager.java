package cc.vihackerframework.gateway.auth;

import cn.hutool.core.convert.Convert;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Created by Ranger on 2022/5/3.
 */
@Component
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtTokenHelper tokenHelper;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();
        String username;
        try {
            username = tokenHelper.getUsernameFromToken(token);
        } catch (Exception e) {
            username = null;
        }
        if (StringUtils.isNotBlank(username) && tokenHelper.validateToken(token)) {
            Claims claims = tokenHelper.getAllClaimsFromToken(token);
            List<String> authorities = claims.get("authorities", List.class);

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null,
                    AuthorityUtils.createAuthorityList(Convert.toStrArray(authorities))
            );
            return Mono.just(auth);
        } else {
            return Mono.empty();
        }
    }
}
