package com.seezoon.web.advice;

import com.seezoon.core.concept.infrastructure.exception.BizException;
import com.seezoon.core.concept.infrastructure.exception.SysException;
import com.seezoon.web.api.DefaultCodeMsgBundle;
import com.seezoon.web.api.Result;
import javax.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * 异常advice
 *
 * @author hdf
 */
@RestControllerAdvice
public class ExceptionAdvice {

    /**
     * 日志对象
     */
    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    /**
     * 具体Excetion 场景参见其java doc
     *
     * @param e
     * @return
     */
    @ExceptionHandler({MissingServletRequestParameterException.class, MethodArgumentNotValidException.class,
            ConstraintViolationException.class, MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class})
    public Result parameterInvalidException(Exception e) {
        return Result.error(DefaultCodeMsgBundle.PARAM_INVALID, e.getMessage());
    }

    @ExceptionHandler({BindException.class})
    public Result bindException(BindException e) {
        return Result.error(DefaultCodeMsgBundle.PARAM_BIND_ERROR, e.getMessage());
    }

    /**
     * 使用{@link org.springframework.util.Assert} 来验证参数
     *
     * @param e
     * @return
     */
    @ExceptionHandler({IllegalArgumentException.class})
    public Result illegalArgumentException(IllegalArgumentException e) {
        return Result.error(DefaultCodeMsgBundle.PARAM_INVALID, e.getMessage());
    }

    @ExceptionHandler(BizException.class)
    public Result businessException(BizException e) {
        return Result.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(SysException.class)
    public Result sysException(SysException e) {
        return Result.error(e.getCode(), e.getMessage());
    }

    /**
     * 可以细化异常，spring 从小异常抓，抓到就不往后走
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result exception(Exception e) throws Exception {
        logger.error("global exception ", e);
        return Result.error(DefaultCodeMsgBundle.UNKNOWN, e.getMessage());
    }
}
