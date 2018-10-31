package io.mycat.monitor.support.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author maxiaoguang
 */
@Slf4j
@ControllerAdvice("io.mycat.monitor.controller.view")
public class ViewExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus
    public String d(Model model, Throwable e) {
        LOGGER.error("未知异常", e);
        model.addAttribute("code", "100000");
        model.addAttribute("message", "未知异常");
        return "error";
    }

}
