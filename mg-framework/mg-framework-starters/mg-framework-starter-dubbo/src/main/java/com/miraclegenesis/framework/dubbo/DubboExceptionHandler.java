package com.miraclegenesis.framework.dubbo;

import com.miraclegenesis.framework.common.exception.BaseResultCodeEnum;
import com.miraclegenesis.framework.common.result.ResultWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.RpcException;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.miraclegenesis.framework.common.exception.ExceptionOrderConstant.EXCEPTION_ORDER_LOW;

/**
 * @author robert
 */
@Slf4j
@ControllerAdvice
@Order(EXCEPTION_ORDER_LOW)
public class DubboExceptionHandler {

    /**
     * 处理dubbo 调用异常
     */
    @ResponseBody
    @ExceptionHandler(RpcException.class)
    public ResultWrapper<Object> rpcException(RpcException rpcException) {
        log.error("dubbo调用异常", rpcException);
        return ResultWrapper.fail(BaseResultCodeEnum.SYSTEM_ERROR, "dubbo调用异常:" + rpcException.getMessage());
    }
}
