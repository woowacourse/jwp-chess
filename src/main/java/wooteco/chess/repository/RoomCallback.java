package wooteco.chess.repository;

import org.springframework.data.relational.core.conversion.AggregateChange;
import org.springframework.data.relational.core.mapping.event.BeforeSaveCallback;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Component
public class RoomCallback implements BeforeSaveCallback<RoomEntity> {
    @Override
    public RoomEntity onBeforeSave(RoomEntity roomEntity, AggregateChange<RoomEntity> aggregateChange) {
        if (Objects.isNull(roomEntity.getId())) {
            roomEntity.setId(UUID.randomUUID());
        }
        return roomEntity;
    }
}
