package cc.vihackerframework.uaa.granter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.provider.*;
import java.util.List;

/**
 * 社交登录TokenGranter
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/8/2
 */
@Slf4j
public class SocialTokenGranter extends CompositeTokenGranter {

    private static final String GRANT_TYPE = "social";

    private static final String PREFIX = "SOCIAL::STATE::";


    public SocialTokenGranter(List<TokenGranter> tokenGranters) {
        super(tokenGranters);
    }
}
