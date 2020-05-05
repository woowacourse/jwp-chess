package wooteco.chess.domain.game.repository.config;

import java.util.UUID;

import org.springframework.data.relational.core.conversion.AggregateChange;
import org.springframework.data.relational.core.mapping.event.BeforeSaveCallback;
import org.springframework.stereotype.Component;

import wooteco.chess.domain.game.repository.GameEntity;

@Component
public class GameIdProvider implements BeforeSaveCallback<GameEntity> {
	@Override
	public GameEntity onBeforeSave(GameEntity aggregate, AggregateChange<GameEntity> aggregateChange) {
		aggregate.setIdIfIdIsNull(createNewId());
		return aggregate;
	}

	private String createNewId() {
		return UUID.randomUUID().toString();
	}
}
