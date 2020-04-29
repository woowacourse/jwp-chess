package wooteco.chess.config;

import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.relational.core.mapping.event.BeforeSaveCallback;

import wooteco.chess.repository.jdbc.GameEntity;

@Configuration
public class ChessConfiguration {
	@Bean
	public BeforeSaveCallback<GameEntity> setRandomIdToEntity() {
		return (aggregate, aggregateChange) -> {
			aggregate.setIdIfIdIsNull(createNewId());
			return aggregate;
		};
	}

	private String createNewId() {
		return UUID.randomUUID().toString();
	}
}
