package wooteco.chess.repository;

import java.util.Objects;
import java.util.UUID;

import org.springframework.data.relational.core.conversion.AggregateChange;
import org.springframework.data.relational.core.mapping.event.BeforeSaveCallback;
import org.springframework.stereotype.Component;

import wooteco.chess.entity.GameEntity;

@Component
public class RandomUUIDProvider implements BeforeSaveCallback<GameEntity> {
    @Override
    public GameEntity onBeforeSave(final GameEntity aggregate, final AggregateChange<GameEntity> aggregateChange) {
        if (Objects.isNull(aggregate.getId())) {
            aggregate.setId(UUID.randomUUID().toString());
        }
        return aggregate;
    }
}
