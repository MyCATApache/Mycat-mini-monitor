package io.mycat.monitor.model.param;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author maxiaoguang
 */
@Getter
@Setter
@Accessors(chain = true)
public class JmxMetricParam extends PageableParam {

    @NotNull
    private Long id;

}
