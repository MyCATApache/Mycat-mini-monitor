package io.mycat.monitor.model.param;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author maxiaoguang
 */
@Getter
@Setter
@Accessors(chain = true)
public class PageableParam {

    private List<String> sorts;
    
    private List<String> orders;
    
    private Integer offset;
    
    private Integer limit;
    
}
