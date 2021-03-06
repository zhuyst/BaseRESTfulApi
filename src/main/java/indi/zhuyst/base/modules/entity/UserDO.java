package indi.zhuyst.base.modules.entity;

import indi.zhuyst.base.common.entity.BaseDO;
import indi.zhuyst.base.security.enums.StatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Pattern;

/**
 * 用户
 * @author zhuyst
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Document(collection = "user")
public class UserDO extends BaseDO {

    private static final long serialVersionUID = 955034318028237994L;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    @Length(min = 4, max = 10, message = "用户名长度应在{min}与{max}之间")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "用户名应为字母或数字的组合")
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    @Length(min = 6, max = 20, message = "密码长度应在{min}与{max}之间")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "密码应为字母或数字的组合")
    private String password;

    /**
     * 昵称
     */
    @ApiModelProperty("昵称")
    @Length(min = 2,max = 8, message = "昵称长度应在{min}与{max}之间")
    private String nickname;

    /**
     * 角色ID
     * @see indi.zhuyst.base.security.enums.RoleEnum
     */
    @ApiModelProperty("角色ID")
    private Integer role;

    /**
     * 状态ID
     * @see StatusEnum
     */
    @ApiModelProperty("状态ID")
    private Integer status;
}