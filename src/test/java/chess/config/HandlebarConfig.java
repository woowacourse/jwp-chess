package chess.config;

import com.github.jknack.handlebars.springmvc.HandlebarsViewResolver;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class HandlebarConfig {

    @Bean
    public HandlebarsViewResolver handlebarsViewResolver() {
        HandlebarsViewResolver resolver = new HandlebarsViewResolver();
        resolver.setPrefix("classpath:/templates");
        resolver.setSuffix(".hbs");
        return resolver;
    }

}
