package indi.zhuyst.base.common.exception;

import indi.zhuyst.base.common.pojo.FieldError;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 字段错误异常
 * @author zhuyst
 */
public class FieldErrorException extends RuntimeException{

    private static final long serialVersionUID = 9206230120204238961L;

    /**
     * 字段错误列表
     */
    @Getter
    private List<FieldError> errors;

    /**
     * 通过Error或Error数组初始化异常
     * @param errors 字段错误数组
     */
    public FieldErrorException(FieldError... errors){
        this(Arrays.asList(errors));
    }

    /**
     * 通过Error集合初始化异常
     * @param errors 字段错误集合
     */
    public FieldErrorException(Collection<FieldError> errors){
        super(errors.toString());
        this.errors = new ArrayList<>(errors);
    }
}
