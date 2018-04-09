package indi.zhuyst.base.modules.service;

import indi.zhuyst.base.common.pojo.PageInfo;
import indi.zhuyst.base.common.pojo.Query;
import indi.zhuyst.base.common.service.BaseCrudService;
import indi.zhuyst.base.modules.entity.UserDO;
import indi.zhuyst.base.modules.pojo.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 用户服务接口
 * @author zhuyst
 */
public interface UserService extends BaseCrudService<UserDO>,UserDetailsService{

    /**
     * 根据用户名获取用户对象
     * @param username 用户名
     * @return 用户对象
     */
    UserDO getByUsername(String username);

    /**
     * 根据昵称获取用户对象
     * @param nickname 昵称
     * @return 用户对象
     */
    UserDO getByNickName(String nickname);

    /**
     * 根据ID获取用户DTO
     * @param id 用户ID
     * @return 用户DTO
     */
    UserDTO getUserDTO(String id);

    /**
     * 通过Query查询用户DTO的分页对象
     * @param query 查询对象
     * @return 用户DTO分页对象
     */
    PageInfo<UserDTO> listUserDTO(Query<UserDO> query);
}
