package indi.zhuyst.base.common.exception;

import indi.zhuyst.base.common.enums.CodeEnum;
import indi.zhuyst.base.common.pojo.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.List;

/**
 * 全局异常处理器
 * @author zhuyst
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理异常 - {@link MethodArgumentNotValidException}
     * @param e 字段绑定失败，通常由{@link javax.validation.Valid}触发
     * @return 错误结果对象
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e){
        List<FieldError> errors = e.getBindingResult().getFieldErrors();

        Result result = Result.error(CodeEnum.ERROR);
        for(FieldError fieldError : errors){
            indi.zhuyst.base.common.pojo.FieldError error = new indi.zhuyst.base.common.pojo.FieldError();

            error.setField(fieldError.getField());
            error.setMessage(fieldError.getDefaultMessage());

            result.addError(error);
        }

        result.setMessage(getFieldErrorMessage(result));
        return result;
    }

    /**
     * 处理异常 - {@link FieldErrorException}
     * @param e 字段错误，自定义异常
     * @return 错误结果对象
     */
    @ExceptionHandler(FieldErrorException.class)
    public Result fieldErrorExceptionHandler(FieldErrorException e){
        Result result = Result.error(CodeEnum.ERROR);

        List<indi.zhuyst.base.common.pojo.FieldError> list = e.getErrors();
        result.addError(list);

        result.setMessage(getFieldErrorMessage(result));
        return result;
    }

    /**
     * 处理异常 - {@link AccessDeniedException}
     * @param e 授权异常
     * @return 授权失败/错误结果对象
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Result accessDeniedExceptionHandler(AccessDeniedException e){
        return Result.error(CodeEnum.FORBIDDEN.getCode(),e.getMessage());
    }

    /**
     * 处理异常 - {@link MaxUploadSizeExceededException}
     * @param e 上传文件大小过大时抛出的异常
     * @return 提醒支持文件最大大小的错误对象
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Result maxUploadSizeExceededException(MaxUploadSizeExceededException e){
        return Result.error("上传文件过大，最大支持上传大小为"
                + e.getMaxUploadSize() + "的文件");
    }

    /**
     * 处理异常 - {@link CommonException}
     * @param e 自定义异常
     * @return 错误对象
     */
    @ExceptionHandler(CommonException.class)
    public Result commonExceptionHandler(CommonException e){
        return Result.error(e.getCode(),e.getMessage());
    }

//    /**
//     * 捕获没有被捕捉的{@link RuntimeException}
//     * @return 错误对象
//     */
//    @ExceptionHandler(RuntimeException.class)
//    public Result runtimeExceptionHandler(){
//        return Result.error(CodeEnum.ERROR);
//    }

    /**
     * 将{@link Result#errors}中的错误转为String
     * @param result 要转换的Result
     * @return 转换后的String
     */
    private String getFieldErrorMessage(Result result){
        StringBuilder builder = new StringBuilder();
        for(Object message : result.getErrors().values()){
            builder.append(message).append("\t");
        }
        return builder.toString();
    }
}
