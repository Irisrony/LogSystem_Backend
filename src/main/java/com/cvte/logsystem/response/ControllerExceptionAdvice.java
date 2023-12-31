package com.cvte.logsystem.response;

import com.cvte.logsystem.exception.AppCheckException;
import com.cvte.logsystem.exception.AppCreateException;
import com.cvte.logsystem.exception.AuthException;
import com.cvte.logsystem.exception.LoginException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.security.SignatureException;
import java.sql.SQLException;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionAdvice extends BasicResponse {

    /**
     * 参数校验异常
     * @param e 错误异常
     * @return  返回结果
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    protected Object methodArgNotValid(Exception e) {
        log.error(e.getMessage());
        return responseFail(ResultCode.VALIDATION_FAILED);
    }

    /**
     * 缺少参数
     * @param e 错误异常
     * @return  返回结果
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected Object missingServletRequestParameter(MissingServletRequestParameterException e) {
        log.error(e.getMessage());
        return responseFail(ResultCode.MISSING_PARAM);
    }

    /**
     * 不支持的请求类型
     * @param e 错误异常
     * @return  返回结果
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected Object httpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        log.error(e.getMessage());
        return responseFail(ResultCode.UNSUPPORTED_METHOD);
    }

    /**
     * 签名验证失败
     * @param e 错误异常
     * @return  返回结果
     */
    @ExceptionHandler(SignatureException.class)
    protected Object signatureException(SignatureException e) {
        log.error(e.getMessage());
        return responseFail(ResultCode.UNAUTHORIZED);
    }

    /**
     * 权限验证失败
     * @param e 错误异常
     * @return  返回结果
     */
    @ExceptionHandler(AuthException.class)
    protected Object authException(AuthException e) {
        log.error(e.getMessage());
        return responseFail(ResultCode.UNAUTHORIZED);
    }

    /**
     * appid不存在
     * @param e 错误异常
     * @return  返回结果
     */
    @ExceptionHandler(AppCheckException.class)
    protected Object appInfoException(AppCheckException e){
        log.error(e.getMessage());
        return responseFail(ResultCode.APPID_NOT_EXIST);
    }

    /**
     * appid申请失败
     * @param e 错误异常
     * @return  返回结果
     */
    @ExceptionHandler(AppCreateException.class)
    protected Object appInfoException(AppCreateException e){
        log.error(e.getMessage());
        return responseFail(ResultCode.APPID_CREATE_FAILED);
    }

    /**
     * 登陆失败
     * @param e 错误异常
     * @return  返回结果
     */
    @ExceptionHandler(LoginException.class)
    protected Object loginException(LoginException e){
        log.error(e.toString());
        return responseFail(ResultCode.USER_LOGIN_ERROR);
    }

    /**
     * 其他常见错误
     * @param e 错误异常
     * @return  返回结果
     */
    @ExceptionHandler({HttpClientErrorException.class, IOException.class, Exception.class, SQLException.class})
    protected Object commonException(Exception e) {
        log.error(e.getMessage());
        return responseFail(ResultCode.SYSTEM_ERROR);
    }
}
