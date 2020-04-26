package wooteco.chess;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import wooteco.chess.dao.JdbcConfiguration;

@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@Configuration
public class WebConfiguration {
	@Bean
	public JdbcConfiguration provideJdbcConfiguration(JdbcConfiguration.Builder builder) {
		return builder.build();
	}

	@ConfigurationProperties(prefix = "spring.datasource")
	@Bean
	public JdbcConfiguration.Builder provideJdbcConfigurationBuilder() {
		return new JdbcConfiguration.Builder();
	}
}
