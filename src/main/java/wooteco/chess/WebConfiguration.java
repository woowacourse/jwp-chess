package wooteco.chess;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import wooteco.chess.dao.DataSource;
import wooteco.chess.dao.JdbcConfiguration;
import wooteco.chess.dao.JdbcTemplate;

@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@Configuration
public class WebConfiguration {
	@Bean
	public JdbcTemplate provideJdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	@Bean
	public DataSource provideDataSource(JdbcConfiguration jdbcConfiguration) {
		return new DataSource(jdbcConfiguration);
	}

	@ConfigurationProperties(prefix = "spring.datasource")
	@Bean
	public JdbcConfiguration provideJdbcConfiguration() {
		return new JdbcConfiguration();
	}
}
