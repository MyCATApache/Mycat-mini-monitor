package io.mycat.monitor.support.spring;

import io.mycat.monitor.model.result.ErrorResult;
import io.mycat.monitor.model.result.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author maxiaoguang
 */
@Slf4j
@ControllerAdvice("io.mycat.monitor.controller.rest")
public class RestExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus
    @ResponseBody
    public ErrorResult d(Throwable e) {
        LOGGER.error("未知异常", e);
        return new ErrorResult("100000", "未知异常");
    }

}
