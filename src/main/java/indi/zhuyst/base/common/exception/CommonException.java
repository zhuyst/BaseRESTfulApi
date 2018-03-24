package indi.zhuyst.base.common.exception;

import indi.zhuyst.base.common.enums.CodeEnum;

/**
 * 全局公用异常，与{@link indi.zhuyst.base.common.pojo.Result}配合使用
 * @author zhuyst
 */
public class CommonException extends RuntimeException{

    private Integer code;

    private String message;

    public CommonException(int code,String message){
        this.code = code;
        this.message = message;
    }

    public CommonException(CodeEnum codeEnum){
        this.code = codeEnum.getCode();
        this.message = codeEnum.getDefaultMessage();
    }

    public CommonException(String message){
        this.code = CodeEnum.ERROR.getCode();
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
