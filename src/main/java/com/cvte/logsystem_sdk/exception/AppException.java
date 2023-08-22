package com.cvte.logsystem_sdk.exception;

import com.cvte.logsystem_sdk.response.ResultCode;
import lombok.Data;

@Data
public class AppException extends RuntimeException{
    private String code;
    private String msg;

    public AppException(ResultCode code){
        this.code = code.getCode();
        this.msg = code.getMsg();
    }

    public AppException(ResultCode code,String msg){
        this.code = code.getCode();
        this.msg = msg == null ? code.getMsg() : msg;
    }

    @Override
    public String toString() {
        return "AppException{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}

