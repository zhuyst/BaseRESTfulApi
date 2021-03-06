package indi.zhuyst.base.security.service.impl;

import indi.zhuyst.base.common.exception.CommonException;
import indi.zhuyst.base.common.service.BaseService;
import indi.zhuyst.base.common.util.ServletUtils;
import indi.zhuyst.base.modules.entity.UserDO;
import indi.zhuyst.base.security.exception.TokenException;
import indi.zhuyst.base.security.pojo.AccessToken;
import indi.zhuyst.base.security.pojo.SecurityUser;
import indi.zhuyst.base.security.service.SecurityService;
import indi.zhuyst.base.security.setting.JwtSettings;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * SecurityService实现类
 * @author zhuyst
 */
@Service
public class SecurityServiceImpl extends BaseService implements SecurityService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtSettings jwtSettings;

    /**
     * {@link SecurityUser#username}在Claims中的name
     */
    private static final String CLAIM_USERNAME = "username";

    /**
     * {@link SecurityUser#role}在Claims中的name
     */
    private static final String CLAIM_ROLE = "role";

    @Override
    public AccessToken login(String username, String password) {
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                username, password);

        HttpServletRequest request = ServletUtils.getRequest();
        authRequest.setDetails(new WebAuthenticationDetails(request));

        try{
            Authentication authentication = authenticationManager.authenticate(authRequest);
            SecurityUser user = (SecurityUser) authentication.getPrincipal();

            return generateToken(user);
        }catch (BadCredentialsException e){
            throw new CommonException("用户名/密码不正确");
        }catch (LockedException e){
            throw new CommonException("账号被锁定，请联系管理员");
        }
    }

    @Override
    public AccessToken generateToken(SecurityUser user) {
        if(user.getLocked()){
            throw new CommonException("账号被锁定，请联系管理员");
        }

        AccessToken accessToken = new AccessToken();
        long expire = jwtSettings.getExpire();

        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + expire * 1000);

        // 构建JWT
        String token = Jwts.builder()

                // 将User的标识信息放入JWT中
                .setSubject(user.getId())
                .claim(CLAIM_USERNAME,user.getUsername())
                .claim(CLAIM_ROLE,user.getRole())

                // 设置过期时间
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)

                // 设置加密方式
                .signWith(SignatureAlgorithm.HS512,
                        jwtSettings.getSecret())
                .compact();

        accessToken.setToken(token);
        accessToken.setExpire(expire);
        accessToken.setUser(user);

        return accessToken;
    }

    @Override
    public String getIDByToken(String token) {
        Claims claims = this.getClaimByToken(token);
        return claims.getSubject();
    }

    @Override
    public SecurityUser getUserByToken(String token) {
        Claims claims = this.getClaimByToken(token);

        // 从JWT中读取信息，构建User
        UserDO user = new UserDO();
        user.setId(claims.getSubject());
        user.setUsername(claims.get(CLAIM_USERNAME,String.class));
        user.setRole(claims.get(CLAIM_ROLE,Integer.class));

        return new SecurityUser(user);
    }

    @Override
    public boolean isTokenValid(String token) {
        Claims claims = this.getClaimByToken(token);
        return isTokenValid(claims);
    }

    @Override
    public String getHeader() {
        return jwtSettings.getHeader();
    }

    /**
     * 获取JWT的Claims
     * @param token JWT
     * @return Claims
     */
    private Claims getClaimByToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(jwtSettings.getSecret())
                    .parseClaimsJws(token)
                    .getBody();

        } catch (ExpiredJwtException e){

            throw new TokenException("token失效，请重新登录");

        } catch (JwtException e){

            // Token转换异常，表示不合法，抛出异常
            throw new TokenException("invalid token");
        } catch (IllegalArgumentException e){

            throw new TokenException("没有包含Token字段");
        }
    }

    /**
     * 通过Claims判断Token是否合法
     * @param claims Claims
     * @return 是否合法
     */
    private boolean isTokenValid(Claims claims){
        return isTokenExpired(claims.getExpiration());
    }

    /**
     * 判断Token是否过期
     * @param expiration 过期时间
     * @return 是否过期
     */
    private boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }
}
