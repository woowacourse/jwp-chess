package wooteco.chess.utils;

import org.springframework.data.relational.core.conversion.AggregateChange;
import org.springframework.data.relational.core.mapping.event.BeforeSaveCallback;
import org.springframework.stereotype.Component;
import wooteco.chess.entity.Room;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class RoomIdGenerator implements BeforeSaveCallback<Room> {

    private static final Long SEQUENCE_MASK = 0x0000000000001fffL;
    private static final Long BASE_EPOCH = 1546300800000L;
    private static final Long SERVER_NUMBER = 1L;

    private static Long prevTimestamp = 0L;

    private static AtomicLong roomSequence = new AtomicLong();

    public static Long generateId() {

        Long timestamp = System.currentTimeMillis();

        if(timestamp.equals(prevTimestamp)) {
            roomSequence.set((roomSequence.incrementAndGet() & SEQUENCE_MASK));
        } else {
            prevTimestamp = timestamp;
            roomSequence.set(0L);
        }

        Long objectId = timestamp - BASE_EPOCH;

        objectId = objectId << 23;
        objectId = objectId | (SERVER_NUMBER << 13);
        objectId = objectId | roomSequence.get();

        return objectId;
    }

    @Override
    public Room onBeforeSave(Room aggregate, AggregateChange<Room> aggregateChange) {
        if(Objects.isNull(aggregate.getId())) {
            aggregate.setId(generateId());
        }
        return aggregate;
    }
}