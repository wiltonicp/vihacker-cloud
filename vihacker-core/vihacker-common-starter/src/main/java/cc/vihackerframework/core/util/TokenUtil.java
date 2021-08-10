package cc.vihackerframework.core.util;

import java.util.Base64;

import cc.vihackerframework.core.constant.Oauth2Constant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
 * Token 工具类
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/7/29
 */
public class TokenUtil {

    public static String BEARER = "bearer";
    public static Integer AUTH_LENGTH = 7;

    /**
     * 获取token串
     *
     * @param auth token
     * @return String
     */
    public static String getToken(String auth) {
        if ((auth != null) && (auth.length() > AUTH_LENGTH)) {
            String headStr = auth.substring(0, 6).toLowerCase();
            if (headStr.compareTo(BEARER) == 0) {
                auth = auth.substring(7);
            }
            return auth;
        }
        return null;
    }

    /**
     * 获取jwt中的claims信息
     *
     * @param token
     * @return claim
     */
    public static Claims getClaims(String token) {
        Claims claims = null;
        String key = Base64.getEncoder().encodeToString(Oauth2Constant.SIGN_KEY.getBytes());
        try {
            claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        }catch (Exception e){
        }
        return claims;
    }
}
