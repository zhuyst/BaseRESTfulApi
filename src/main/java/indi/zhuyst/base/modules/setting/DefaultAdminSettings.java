package indi.zhuyst.base.modules.setting;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 初始化系统管理员默认配置
 * @author zhuyst
 */
@Data
@Component
@ConfigurationProperties(prefix = "base.admin")
public class DefaultAdminSettings {

    /**
     * 管理员的默认用户名
     * @see indi.zhuyst.base.modules.entity.UserDO#username
     */
    private String username;

    /**
     * 管理员的默认密码
     * @see indi.zhuyst.base.modules.entity.UserDO#password
     */
    private String password;

    /**
     * 管理员的默认昵称
     * @see indi.zhuyst.base.modules.entity.UserDO#nickname
     */
    private String nickname;
}
