package wooteco.chess.entity;

import java.util.UUID;

import org.springframework.data.relational.core.conversion.AggregateChange;
import org.springframework.data.relational.core.mapping.event.BeforeSaveCallback;
import org.springframework.stereotype.Component;

@Component
public class RandomUUIDProvider implements BeforeSaveCallback<ChessGameEntity> {
	@Override
	public ChessGameEntity onBeforeSave(final ChessGameEntity aggregate,
			final AggregateChange<ChessGameEntity> aggregateChange) {
		aggregate.setId(UUID.randomUUID().toString());
		return aggregate;
	}
}
