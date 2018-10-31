package io.mycat.monitor.controller.rest;

import io.mycat.monitor.service.JmxMetricService;
import io.mycat.monitor.entity.JmxMetric;
import io.mycat.monitor.model.param.JmxMetricParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author maxiaoguang
 */
@RestController
@RequestMapping({"/rest/jmx_metrics"})
public class JmxMetricRestController {

    @Autowired
    private JmxMetricService jmxMetricService;

    @GetMapping(value = "/list")
    public List<JmxMetric> listJmxMetrics(@Valid JmxMetricParam jmxMetricParam) {
        return jmxMetricService.listJmxMetrics(jmxMetricParam);
    }

}
