<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">
<head>
    <script type="text/javascript" src="/static/js/jquery-1.12.4.min.js"></script>
    <script type="text/javascript" src="/static/js/echarts.common.min.js"></script>
</head>
<body>
    <c:forEach var="ip" items="${ips}">
        <div id="${ip}CpuContainer" style="width: 25%; height: 250px; float: left"></div>
        <div id="${ip}MemeryContainer" style="width: 25%; height: 250px; float: left"></div>
    </c:forEach>
    <script type="text/javascript">
        var lastJmxMetricId = 0;
        var ipJmxMetric = {};
        var ipMemeryChart = {};
        var ipCpuChart = {};
        <c:forEach var="ip" items="${ips}">
            ipJmxMetric.${ip} = {usedMemorys: [], committedMemorys: [], maxMemorys: [], processCpuLoads: [], systemCpuLoads: [], systemLoadAverages: []};
            ipCpuChart.${ip} = echarts.init(document.getElementById('${ip}CpuContainer'));
            ipMemeryChart.${ip} = echarts.init(document.getElementById('${ip}MemeryContainer'));
        </c:forEach>
        <c:forEach var="jmxMetric" items="${jmxMetrics}">
            lastJmxMetricId = ${jmxMetric.id};
            var createdDate = new Date('${jmxMetric.createdDate}');
            ipJmxMetric.${jmxMetric.ip}.usedMemorys.push([createdDate, new Number(${jmxMetric.usedMemory / (1024 * 1024 * 1024)}).toFixed(2)]);
            ipJmxMetric.${jmxMetric.ip}.committedMemorys.push([createdDate, new Number(${jmxMetric.committedMemory / (1024 * 1024 * 1024)}).toFixed(2)]);
            ipJmxMetric.${jmxMetric.ip}.maxMemorys.push([createdDate, new Number(${jmxMetric.maxMemory / (1024 * 1024 * 1024)}).toFixed(2)]);
            ipJmxMetric.${jmxMetric.ip}.processCpuLoads.push([createdDate, ${jmxMetric.processCpuLoad * 100}]);
            ipJmxMetric.${jmxMetric.ip}.systemCpuLoads.push([createdDate, ${jmxMetric.systemCpuLoad * 100}]);
            ipJmxMetric.${jmxMetric.ip}.systemLoadAverages.push([createdDate, ${jmxMetric.systemLoadAverage * 100}]);
        </c:forEach>
        <c:forEach var="ip" items="${ips}">
            ipCpuChart.${ip}.setOption(getCpuOption("${ip}"), true);
            ipMemeryChart.${ip}.setOption(getMemeryOption("${ip}"), true);
        </c:forEach>
        setInterval(function () {
            $.ajax({
                url : '/rest/jmx_metrics/list',
                type : 'GET',
                traditional : true,
                data : {
                    id : lastJmxMetricId,
                    sorts : ['id'],
                    orders : ['asc'],
                    offset : 0,
                    limit : 720
                },
                dataType :'json',
                success : function(jmxMetrics) {
                    for(var i in jmxMetrics) {
                        var jmxMetric = jmxMetrics[i];
                        lastJmxMetricId = jmxMetrics[i].id;
                        var createdDate = new Date(jmxMetric.createdDate);
                        if (ipJmxMetric[jmxMetric.ip].usedMemorys.length >= 720) {
                            ipJmxMetric[jmxMetric.ip].usedMemorys.splice(0, 1);
                        }
                        ipJmxMetric[jmxMetric.ip].usedMemorys.push([createdDate, new Number(jmxMetric.usedMemory / (1024 * 1024 * 1024)).toFixed(2)]);
                        if (ipJmxMetric[jmxMetric.ip].committedMemorys.length >= 720) {
                            ipJmxMetric[jmxMetric.ip].committedMemorys.splice(0, 1);
                        }
                        ipJmxMetric[jmxMetric.ip].committedMemorys.push([createdDate, new Number(jmxMetric.committedMemory / (1024 * 1024 * 1024)).toFixed(2)]);
                        if (ipJmxMetric[jmxMetric.ip].maxMemorys.length >= 720) {
                            ipJmxMetric[jmxMetric.ip].maxMemorys.splice(0, 1);
                        }
                        ipJmxMetric[jmxMetric.ip].maxMemorys.push([createdDate, new Number(jmxMetric.maxMemory / (1024 * 1024 * 1024)).toFixed(2)]);
                        if (ipJmxMetric[jmxMetric.ip].processCpuLoads.length >= 720) {
                            ipJmxMetric[jmxMetric.ip].processCpuLoads.splice(0, 1);
                        }
                        ipJmxMetric[jmxMetric.ip].processCpuLoads.push([createdDate, jmxMetric.processCpuLoad * 100]);
                        if (ipJmxMetric[jmxMetric.ip].systemCpuLoads.length >= 720) {
                            ipJmxMetric[jmxMetric.ip].systemCpuLoads.splice(0, 1);
                        }
                        ipJmxMetric[jmxMetric.ip].systemCpuLoads.push([createdDate, jmxMetric.systemCpuLoad * 100]);
                        if (ipJmxMetric[jmxMetric.ip].systemLoadAverages.length >= 720) {
                            ipJmxMetric[jmxMetric.ip].systemLoadAverages.splice(0, 1);
                        }
                        ipJmxMetric[jmxMetric.ip].systemLoadAverages.push([createdDate, jmxMetric.systemLoadAverage * 100]);
                        ipCpuChart[jmxMetric.ip].setOption({
                            series : [{
                                name : '进程',
                                data : ipJmxMetric[jmxMetric.ip].processCpuLoads
                            }, {
                                name : '系统',
                                data : ipJmxMetric[jmxMetric.ip].systemCpuLoads
                            }, {
                                name : '负载',
                                data : ipJmxMetric[jmxMetric.ip].systemLoadAverages
                            }]
                        });
                        ipMemeryChart[jmxMetric.ip].setOption({
                            series : [{
                                name : '使用',
                                data : ipJmxMetric[jmxMetric.ip].usedMemorys
                            }, {
                                name : '申请',
                                data : ipJmxMetric[jmxMetric.ip].committedMemorys
                            }, {
                                name : '最大',
                                data : ipJmxMetric[jmxMetric.ip].maxMemorys
                            }]
                        });
                    }
                }});
        }, 30000);

        function getCpuOption(ip) {
            return {
                color:['#006000', '#00AEAE', '#FF6347'],
                title : {
                    text : ip.substr(ip.lastIndexOf("_") + 1)
                },
                legend : {
                    selected: {
                        '进程' : false
                    },
                    data : ['进程', '系统', '负载']
                },
                toolbox : {
                    show : false
                },
                tooltip : {
                    trigger : 'axis'
                },
                dataZoom : {
                    show : true,
                    start : 0
                },
                grid: {
                    left: '12%',
                    top: '13%',
                    bottom: '30%'
                },
                xAxis : [
                    {
                        type : 'time'
                    }
                ],
                yAxis : [
                    {
                        type : 'value',
                        min : 0,
                        axisLabel : {
                            formatter: '{value}%'
                        },
                    }
                ],
                series : [
                    {
                        name : '进程',
                        type : 'line',
                        symbolSize : 1,
                        data : ipJmxMetric[ip].processCpuLoads
                    },
                    {
                        name : '系统',
                        type : 'line',
                        symbolSize : 1,
                        data : ipJmxMetric[ip].systemCpuLoads
                    },
                    {
                        name : '负载',
                        type : 'line',
                        symbolSize : 1,
                        data : ipJmxMetric[ip].systemLoadAverages
                    }
                ]
            };
        }

        function getMemeryOption(ip) {
            return {
                color:['#006000', '#00AEAE', '#FF6347'],
                title : {
                    text : ip.substr(ip.lastIndexOf("_") + 1)
                },
                legend : {
                    data : ['使用', '申请', '最大']
                },
                toolbox : {
                    show : false
                },
                tooltip : {
                    trigger: 'axis'
                },
                dataZoom : {
                    show : true,
                    start : 0
                },
                grid: {
                    left: '12%',
                    top: '13%',
                    bottom: '30%'
                },
                xAxis : [
                    {
                        type : 'time'
                    }
                ],
                yAxis : [
                    {
                        type : 'value',
                        min : 0,
                        axisLabel : {
                            formatter: '{value}GB'
                        },
                    }
                ],
                series : [
                    {
                        name : '使用',
                        type : 'line',
                        symbolSize : 1,
                        data : ipJmxMetric[ip].usedMemorys
                    },
                    {
                        name : '申请',
                        type : 'line',
                        symbolSize : 1,
                        data : ipJmxMetric[ip].committedMemorys
                    },
                    {
                        name : '最大',
                        type : 'line',
                        symbolSize : 1,
                        data : ipJmxMetric[ip].maxMemorys
                    }
                ]
            };
        }
    </script>
</body>
</html>