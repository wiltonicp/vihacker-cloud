package cc.vihackerframework.gateway.auth;

import cc.vihackerframework.core.constant.Oauth2Constant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Base64;
import java.util.Date;

/**
 * Created by Ranger on 2022/5/3.
 */
@Component
public class JwtTokenHelper implements Serializable{

    private static final long serialVersionUID = 1579222883969867182L;

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(Base64.getEncoder().encodeToString(Oauth2Constant.SIGN_KEY.getBytes()))
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsernameFromToken(String token) {
        return (String) getAllClaimsFromToken(token).get("username");
    }

    public Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }
}
