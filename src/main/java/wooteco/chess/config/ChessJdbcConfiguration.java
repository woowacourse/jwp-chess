package wooteco.chess.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;

import wooteco.chess.converter.PositionToStringConverter;

@Configuration
public class ChessJdbcConfiguration extends AbstractJdbcConfiguration {

	@Override
	@Bean
	public JdbcCustomConversions jdbcCustomConversions() {
		return new JdbcCustomConversions(Collections.singletonList(new PositionToStringConverter()));
	}
}
