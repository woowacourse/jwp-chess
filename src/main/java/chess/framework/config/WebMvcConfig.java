package chess.framework.config;

import chess.framework.formatter.StringToPositionFormatter;
import chess.framework.interceptor.LoggingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoggingInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/static/**", "/**.ico");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new StringToPositionFormatter());
    }
}
