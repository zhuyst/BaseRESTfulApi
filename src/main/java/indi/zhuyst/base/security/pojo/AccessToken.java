package indi.zhuyst.base.security.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 权限Token，包含过期时间以及当前用户对象
 * @author zhuyst
 */
@Data
@NoArgsConstructor
public class AccessToken implements Serializable{

    private static final long serialVersionUID = 5027023587582035877L;

    /**
     * AccessToken
     */
    @ApiModelProperty("AccessToken")
    private String token;

    /**
     * 过期时间
     */
    @ApiModelProperty("过期时间")
    private Long expire;

    /**
     * Token对应用户对象
     */
    @ApiModelProperty("Token对应用户对象")
    private SecurityUser user;
}
