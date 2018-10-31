package io.mycat.monitor.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author maxiaoguang
 */
@Getter
@Setter
@Accessors(chain = true)
public class JmxMetric {

    private Long id;

    private String ip;

    private BigDecimal processCpuLoad;

    private BigDecimal systemCpuLoad;

    private BigDecimal systemLoadAverage;

    private Long usedMemory;

    private Long committedMemory;

    private Long maxMemory;

    private Integer duration;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;

}
