package indi.zhuyst.base.security.exception;

/**
 * Token异常
 * @see indi.zhuyst.base.security.service.impl.SecurityServiceImpl#getClaimByToken(String)
 * @author zhuyst
 */
public class TokenException extends RuntimeException{

    public TokenException(String message){
        super(message);
    }
}
