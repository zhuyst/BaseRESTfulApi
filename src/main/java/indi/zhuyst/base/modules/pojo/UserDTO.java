package indi.zhuyst.base.modules.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import indi.zhuyst.base.modules.entity.UserDO;
import indi.zhuyst.base.security.enums.RoleEnum;
import indi.zhuyst.base.security.enums.StatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

/**
 * 用户DTO，包含角色枚举类
 * @author zhuyst
 */
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class UserDTO extends UserDO {

    private static final long serialVersionUID = -9051994604875275894L;

    /**
     * 角色枚举类，通过{@link #role}获取
     */
    @Getter
    @JsonIgnore
    private RoleEnum roleEnum;

    /**
     * 状态枚举类，通过{@link #status}获取
     */
    @Getter
    @JsonIgnore
    private StatusEnum statusEnum;

    /**
     * 是否为管理员
     * @see RoleEnum#isAdmin()
     */
    @ApiModelProperty("是否为管理员")
    @Getter
    private Boolean admin;

    /**
     * 账户是否被锁定
     * @see StatusEnum#isLocked()
     */
    @ApiModelProperty("账户是否被锁定")
    @Getter
    private Boolean locked;

    public UserDTO(UserDO user){
        BeanUtils.copyProperties(user,this,
                "role","status");

        this.setRole(user.getRole());

        Integer status = user.getStatus();
        if(status != null){
            this.setStatus(status);
        }
    }

    @Override
    public void setRole(Integer role) {
        super.setRole(role);
        roleEnum = RoleEnum.getById(role);
        admin = roleEnum.isAdmin();
    }

    @Override
    public void setStatus(Integer status) {
        super.setStatus(status);
        statusEnum = StatusEnum.getById(status);
        locked = statusEnum.isLocked();
    }
}
