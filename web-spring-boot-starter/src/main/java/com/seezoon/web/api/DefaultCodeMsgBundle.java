package com.seezoon.web.api;

/**
 * 通用响应码及响应消息，子业务模块如果需要规范错误码可以继承该类
 *
 * 不采用枚举的原因是因为，子业务模块都需要自己的定义响应码及响应消息，但枚举无法继承
 *
 * @author hdf
 */
public class DefaultCodeMsgBundle {

    public static final CodeMsg SUCCESS = new CodeMsg("0", "Success");
    public static final CodeMsg FAIL = new CodeMsg("-1", null);
    public static final CodeMsg UNKNOWN = new CodeMsg("UNKNOWN", "请联系管理员,错误信息:%s");
    public static final CodeMsg PARAM_INVALID = new CodeMsg("PARAM_INVALID", "参数错误,%s");
    public static final CodeMsg PARAM_BIND_ERROR = new CodeMsg("PARAM_BIND_ERROR", "参数绑定错误,%s");
    public static final CodeMsg SAVE_ERROR = new CodeMsg("SAVE_ERROR", "保存失败，影响条数:%d,请联系管理员");
    public static final CodeMsg UPDATE_ERROR = new CodeMsg("UPDATE_ERROR", "更新失败，影响条数:%d,请联系管理员");
    public static final CodeMsg DELETE_ERROR = new CodeMsg("DELETE_ERROR", "删除失败，影响条数:%d,请联系管理员");
}
