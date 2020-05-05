package wooteco.chess;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import wooteco.chess.repository.BinaryToUUIDConverter;
import wooteco.chess.repository.UUIDtoBinaryConverter;

import java.util.Arrays;

@Configuration
public class ChessJdbcConfiguration extends AbstractJdbcConfiguration {
    @Override
    public JdbcCustomConversions jdbcCustomConversions() {
        return new JdbcCustomConversions(Arrays.asList(new BinaryToUUIDConverter(), new UUIDtoBinaryConverter()));
    }
}
