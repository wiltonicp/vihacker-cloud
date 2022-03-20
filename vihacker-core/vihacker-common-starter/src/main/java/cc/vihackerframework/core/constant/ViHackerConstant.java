package cc.vihackerframework.core.constant;

/**
 * @Description
 * @Author: Ranger
 * @Date: 2021/1/25 14:57
 * @Email: wilton.icp@gmail.com
 */
public class ViHackerConstant {

    /**
     * 默认头像
     */
    public static final String DEFAULT_AVATAR = "https://oss.wiltonic.cn/defult.jpeg";
    /**
     * 默认密码
     */
    public static final String DEFAULT_PASSWORD = "vihacker123";

    /**
     * gif类型
     */
    public static final String GIF = "gif";
    /**
     * png类型
     */
    public static final String PNG = "png";

    /**
     * 排序规则：降序
     */
    public static final String ORDER_DESC = "DESC";
    /**
     * 排序规则：升序
     */
    public static final String ORDER_ASC = "ASC";

    /**
     * 允许下载的文件类型，根据需求自己添加（小写）
     */
    public static final String[] VALID_FILE_TYPE = {"xlsx", "zip"};

    /**
     * OAUTH2 令牌类型 https://oauth.net/2/bearer-tokens/
     */
    public static final String OAUTH2_TOKEN_TYPE = "bearer";

    /**
     * Gateway请求头TOKEN名称（不要有空格）
     */
    public static final String GATEWAY_TOKEN_HEADER = "GatewayToken";
    /**
     * Gateway请求头TOKEN值
     */
    public static final String GATEWAY_TOKEN_VALUE = "vihacker:gateway:123456";

    /**
     * json类型报文，UTF-8字符集
     */
    public static final String JSON_UTF8 = "application/json;charset=UTF-8";
}
