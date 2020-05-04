package wooteco.chess;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.relational.core.mapping.event.BeforeSaveCallback;
import wooteco.chess.repository.BinaryToUUIDConverter;
import wooteco.chess.repository.RoomEntity;
import wooteco.chess.repository.UUIDtoBinaryConverter;

import java.util.Arrays;
import java.util.UUID;

@Configuration
public class ChessJdbcConfiguration extends AbstractJdbcConfiguration {
    @Override
    public JdbcCustomConversions jdbcCustomConversions() {
        return new JdbcCustomConversions(Arrays.asList(new BinaryToUUIDConverter(), new UUIDtoBinaryConverter()));
    }

    @Bean
    BeforeSaveCallback<RoomEntity> unorderedLambdaReceiverCallback() {
        return (it, ac) -> {
            UUID uuid = UUID.randomUUID();
            ;
            return it;
        };
    }
}
