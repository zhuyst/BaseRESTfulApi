package indi.zhuyst.base.security.util;

import indi.zhuyst.base.security.pojo.SecurityUser;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Spring Security相关工具类
 * @author zhuyst
 */
public class SecurityUtils {

    /**
     * 获取当前请求的Authentication
     * @return 当前请求的Authentication
     */
    public static Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取当前请求的用户
     * @return 当前请求的用户
     */
    public static SecurityUser getUser(){
        return (SecurityUser) getAuthentication().getPrincipal();
    }

    /**
     * 检查是否登陆
     * @return 是否登陆
     */
    public static boolean checkLogin() {
        return getAuthentication() != null &&
                !(getAuthentication() instanceof AnonymousAuthenticationToken);
    }
}
