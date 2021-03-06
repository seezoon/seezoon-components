package com.seezoon.web.api;

import com.seezoon.web.i18n.Message;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * web 层通用返回结果，按照约定，只出现在web层
 *
 * 泛型是为了让swagger 能获取字段信息
 *
 * 默认code = '0' 成功 ，code = '-1' 错误
 *
 * @author hdf
 */
@Getter
@Setter
@NoArgsConstructor
@SuppressWarnings("all")
public class Result<T> {

    public final static Result SUCCESS = new Result(DefaultCodeMsgBundle.SUCCESS);

    /**
     * 响应码-0成功
     */
    private String code;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 业务字段
     */
    private T data;

    private Result(String code, String msg, Object... args) {
        this.code = code;
        this.msg = (null != args ? String.format(msg, args) : msg);
    }

    private Result(CodeMsg codeMsg, Object... args) {
        this(codeMsg.code(), codeMsg.msg(), args);
    }

    public static <T> Result<T> ok() {
        return ok(null);
    }

    public static <T> Result<T> ok(T data) {
        Result<T> result = new Result(DefaultCodeMsgBundle.SUCCESS);
        result.setData(data);
        return result;
    }

    /**
     * 通常从国际化资源文件中获取
     *
     * @param code
     * @param args
     * @param <T>
     * @return
     */
    public static <T> Result<T> errorI18n(String code, Object... args) {
        return new Result<>(code, Message.get(code, args));
    }

    /**
     * 通常从国际化资源文件中获取
     *
     * @param code
     * @param defaultMessage not support format
     * @param args
     * @param <T>
     * @return
     */
    public static <T> Result<T> errorI18nWithDefaultMessage(String code, String defaultMessage, Object... args) {
        return new Result<>(code, Message.getWithDefault(code, defaultMessage, args));
    }


    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<T>(DefaultCodeMsgBundle.FAIL.code(), msg);
        return result;
    }

    /**
     * 支持{@link String#format}格式化参数
     *
     * @param code
     * @param msg
     * @param agrs
     * @param <T>
     * @return
     */
    public static <T> Result<T> error(String code, String msg, Object... agrs) {
        Result<T> result = new Result<>(code, msg, agrs);
        return result;
    }

    /**
     * 支持{@link String#format}格式化参数
     *
     * @param codeMsg
     * @param agrs 参数
     * @param <T>
     * @return
     */
    public static <T> Result<T> error(CodeMsg codeMsg, Object... agrs) {
        Result<T> result = new Result<T>(codeMsg, agrs);
        return result;
    }
}
