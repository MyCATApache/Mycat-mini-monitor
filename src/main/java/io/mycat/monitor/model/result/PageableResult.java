package io.mycat.monitor.model.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.Delegate;

import java.util.List;

/**
 * @author maxiaoguang
 */
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class PageableResult<E> implements List<E> {
    
    private Long total;
    
    @Delegate
    private List<E> data;
    
}
