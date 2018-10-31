package io.mycat.monitor.model.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author maxiaoguang
 */
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class ErrorResult {
    
    private String code;
    
    private String message;
    
}
