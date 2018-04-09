package indi.zhuyst.base.modules.controller;

import indi.zhuyst.base.common.controller.BaseController;
import indi.zhuyst.base.common.enums.CodeEnum;
import indi.zhuyst.base.common.exception.CommonException;
import indi.zhuyst.base.common.pojo.PageInfo;
import indi.zhuyst.base.common.pojo.Query;
import indi.zhuyst.base.common.pojo.Result;
import indi.zhuyst.base.modules.entity.UserDO;
import indi.zhuyst.base.modules.pojo.UserDTO;
import indi.zhuyst.base.modules.service.UserService;
import indi.zhuyst.base.security.annotation.AdminAuthorize;
import indi.zhuyst.base.security.annotation.SelfAuthorize;
import indi.zhuyst.base.security.annotation.SysAdminAuthorize;
import indi.zhuyst.base.security.pojo.AccessToken;
import indi.zhuyst.base.security.pojo.SecurityUser;
import indi.zhuyst.base.security.service.SecurityService;
import indi.zhuyst.base.security.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 用户相关API
 * @author zhuyst
 */
@RestController
@Api(value = "UserApi",description = "用户相关API")
@RequestMapping("/users")
public class UserController extends BaseController {

    private final UserService userService;

    private final SecurityService securityService;

    @Autowired
    public UserController(UserService userService, SecurityService securityService) {
        this.userService = userService;
        this.securityService = securityService;
    }

    /**
     * 更新用户信息
     * @param id 用户ID
     * @param user 用户对象
     * @return 更新后的用户
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "更新用户信息",notes = NOTES_SELF)
    @SelfAuthorize
    public Result<UserDO> updateUser(@ApiParam("用户ID") @PathVariable("id") @P("id") String id,
                                      @ApiParam("用户对象") @Valid @RequestBody UserDO user){
        user.setId(id);
        UserDO pojo = userService.save(user);
        return produceResult(pojo,"用户信息更新失败");
    }

    /**
     * 根据id获取用户信息
     * @param id 用户ID
     * @return 用户DTO
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取用户信息",notes = NOTES_SELF)
    @SelfAuthorize
    public Result<UserDTO> getUser(@ApiParam("用户ID") @PathVariable("id") @P("id") String id){
        UserDTO user = userService.getUserDTO(id);
        return produceResult(user, CodeEnum.NOT_FOUND.getCode(),"未找到该用户");
    }

    /**
     * 获取当前授权用户信息
     * @return 用户DTO
     */
    @GetMapping("/select")
    @ApiOperation(value = "获取当前授权用户信息",notes = NOTES_PROTECTED)
    @SelfAuthorize
    public Result<UserDTO> getLoginUser(){
        UserDTO user = SecurityUtils.getUser();
        return Result.ok(user);
    }

    /**
     * 注册新用户
     * @param newUser 新用户对象
     * @return 授权Token
     */
    @PostMapping("/public/")
    @ApiOperation(value = "注册新用户",notes = NOTES_PUBLIC)
    public Result<AccessToken> register(@ApiParam("用户对象") @Valid @RequestBody UserDO newUser){
        newUser.setId(null);

        UserDO user = userService.save(newUser);
        if(user == null){
            throw new CommonException("用户注册失败");
        }

        SecurityUser securityUser = new SecurityUser(user);
        AccessToken accessToken = securityService.generateToken(securityUser);
        return Result.ok(accessToken);
    }

    /**
     * 根据id删除用户
     * @param id 用户ID
     * @return 结果对象
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "根据id删除用户",notes = NOTES_SYS_ADMIN)
    @SysAdminAuthorize
    public Result deleteUser(@ApiParam("用户ID") @PathVariable("id")String id){
        userService.delete(id);
        return Result.ok();
    }

    /**
     * 查询用户列表
     * @param query 查询对象
     * @return 用户的分页对象
     */
    @GetMapping("/list/")
    @ApiOperation(value = "查询用户列表",notes = NOTES_ADMIN)
    @AdminAuthorize
    public Result<PageInfo<UserDTO>> listUser(Query<UserDO> query){
        PageInfo<UserDTO> page = userService.listUserDTO(query);
        return Result.ok(page);
    }

}
