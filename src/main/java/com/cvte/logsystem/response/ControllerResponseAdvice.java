package com.cvte.logsystem.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一返回处理
 */
@RestControllerAdvice
public class ControllerResponseAdvice extends BasicResponse implements ResponseBodyAdvice {

    private final static String DEFAULT_KEY = "status";

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return !returnType.getParameterType().isAssignableFrom(ResultVo.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (returnType.getGenericParameterType().equals(String.class)) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.writeValueAsString(responseSuccess(body));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        if(body == null || (body instanceof HashMap<?,?> && ((HashMap<?, ?>) body).size() == 0)){
            Map<String,Boolean> res = new HashMap<>();
            res.put(DEFAULT_KEY,true);
            return responseSuccess(res);
        }
        return responseSuccess(body);
    }
}
