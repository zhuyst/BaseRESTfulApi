package indi.zhuyst.base.security.setting;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT相关设置
 * @author zhuyst
 */
@Data
@Component
@ConfigurationProperties(prefix = "base.jwt")
public class JwtSettings {

    /**
     * 密钥
     */
    private String secret;

    /**
     * Token在Header中的Name
     */

    private String header;

    /**
     * Token过期时间
     */

    private Long expire;
}
