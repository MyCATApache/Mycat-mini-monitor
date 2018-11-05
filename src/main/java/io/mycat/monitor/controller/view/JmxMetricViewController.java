package io.mycat.monitor.controller.view;

import io.mycat.monitor.service.JmxMetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @author maxiaoguang
 */
@Controller
@RequestMapping({"/view/jmx_metrics"})
public class JmxMetricViewController {

    @Autowired
    private JmxMetricService jmxMetricService;

    @Value("${monitor.showCpuChart}")
    private boolean showCpuChart;
    @Value("${monitor.showMemeryChart}")
    private boolean showMemeryChart;

    @GetMapping
    public String index(Map<String, Object> model) {
        model.put("ips", jmxMetricService.getEscapeIps());
        model.put("showCpuChart", showCpuChart);
        model.put("showMemeryChart", showMemeryChart);
        model.put("jmxMetrics", jmxMetricService.recentlyJmxMetrics());
        return "index";
    }

}