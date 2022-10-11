package com.sg.rl.common.constants;


import com.sg.rl.common.utils.StringUtils;

public enum ExceptionDefineEnum{

    OPTR_SUC(200,"success!!"),
    NO_PERMITION(401,"Unauthorized"),
    OPTR_FAILED(500,"op failed！！"),

    METHOD_NOT_ALLOWED(405,"method not found"),


    //module-1 exception from 20000 to 21000
    //module-2 exception from 22000 to 23000
    //...


    //db exception from 30000 to 31000
    EXCEPTION_CODE_DB_ACCESS_FAILED(30001,"db access failed,check db link"),
    EXCEPTION_CODE_DB_DUP_FAILED(30002,"db update failed,dup key"),
    EXCEPTION_CODE_DB_UPDATE_FAILED (30003,"db update failed,maybe some fields passed don't match the db-schema"),
    EXCEPTION_CODE_DB_UNKNOW_FAILED(30004,"db operator failed,un-known error"),

    //others exception from 40000
    EXCEPTION_CODE_FORBIDDEN(40001," forbidden access"),
    EXCEPTION_CODE_PARM_VALIDATION_ERR(40002," parameter validation failed"),


    SYS_ERR(500," system failed");


    private Integer code;
    private String message;

    private ExceptionDefineEnum(int code, String desc){
        this.code = code;
        this.message = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public final static ExceptionDefineEnum newInstance(int code) {
        ExceptionDefineEnum[] s_ = ExceptionDefineEnum.values();
        if (StringUtils.isNotEmpty(s_)) {
            for (ExceptionDefineEnum s_s : s_) {
                if (s_s.getCode() == code) {
                    return s_s;
                }
            }
        }
        return null;
    }
}