# Mycat-mini-monitor

## 配置application.yml：
配置需要监控的机器例如

    monitor:
        ips: 10.0.0.1, 10.0.0.2, 10.0.0.3

监控间隔时间（秒）例如

    monitor:
        fetchInteval: 10

数据保留时间（秒）（-1为永久）例如

    monitor:
        cleanInteval: 86400
        
## 打包：
mvn clean package

## 运行：
java -jar monitor-1.0.0.war

## 主页：
http://localhost:8080/view/jmx_metrics

