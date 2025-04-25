package njslcp.project.guess_game.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Register new filter with wildcard pattern
 */
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<RequestIDFilter> filterRegistrationBean() {
        FilterRegistrationBean<RequestIDFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new RequestIDFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }
}
