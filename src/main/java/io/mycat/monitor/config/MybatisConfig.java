package io.mycat.monitor.config;

import io.mycat.monitor.support.mybatis.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author maxiaoguang
 */
@Configuration
public class MybatisConfig {
    
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
    
}
