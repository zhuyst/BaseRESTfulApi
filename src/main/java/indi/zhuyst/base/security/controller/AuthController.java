package indi.zhuyst.base.security.controller;

import indi.zhuyst.base.common.controller.BaseController;
import indi.zhuyst.base.common.pojo.Result;
import indi.zhuyst.base.modules.entity.UserDO;
import indi.zhuyst.base.modules.service.UserService;
import indi.zhuyst.base.security.pojo.AccessToken;
import indi.zhuyst.base.security.pojo.SecurityUser;
import indi.zhuyst.base.security.service.SecurityService;
import indi.zhuyst.base.common.controller.BaseController;
import indi.zhuyst.base.common.pojo.Result;
import indi.zhuyst.base.modules.service.UserService;
import indi.zhuyst.base.security.pojo.SecurityUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 授权相关Controller
 * @author zhuyst
 */
@RestController
@RequestMapping("/auth")
@Api(value = "AuthApi",description = "授权相关API")
public class AuthController extends BaseController {

    private final SecurityService securityService;

    private final UserService userService;

    @Autowired
    public AuthController(SecurityService securityService, UserService userService) {
        this.securityService = securityService;
        this.userService = userService;
    }

    /**
     * @param username 用户名
     * @param password 密码
     * @return AccessToken
     */
    @ApiOperation(value = "登录，获取Token")
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public Result<AccessToken> login(@ApiParam("用户名") @RequestParam String username,
                                     @ApiParam("密码") @RequestParam String password) {
        AccessToken accessToken = securityService.login(username,password);
        return Result.ok(accessToken);
    }

    /**
     * 通过老Token换取新Token，顺便记录访问日志
     * @param token 老Token
     * @return 新Token
     */
    @ApiOperation("通过老Token换取新Token")
    @RequestMapping(value = "/refresh",method = RequestMethod.POST)
    public Result<AccessToken> refresh(@ApiParam("老Token") @RequestParam String token){
        String userId = securityService.getIDByToken(token);
        UserDO user = userService.getByID(userId);

        AccessToken accessToken = securityService.generateToken(new SecurityUser(user));
        return Result.ok(accessToken);
    }
}
