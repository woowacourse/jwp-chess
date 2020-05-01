package wooteco.chess.utils;

import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.atomic.AtomicLong;

public class IdGenerator {

    private static final Long SEQUENCE_MASK = 0x0000000000001fffL;
    private static final Long BASE_EPOCH = 1546300800000L;

    private static Long prevTimestamp = 0L;

    @Value("${server-number}")
    private static Long SERVER_NUMBER;

    private static AtomicLong moveSequence = new AtomicLong();
    private static AtomicLong roomSequence = new AtomicLong();

    public static Long generateRoomId() {

        Long timestamp = System.currentTimeMillis();

        if(timestamp.equals(prevTimestamp)) {
            moveSequence.set((moveSequence.incrementAndGet() & SEQUENCE_MASK));
        } else {
            prevTimestamp = timestamp;
            moveSequence.set(0L);
        }

        Long objectId = timestamp - BASE_EPOCH;

        objectId = objectId << 23;
        objectId = objectId | (SERVER_NUMBER << 13);
        objectId = objectId | moveSequence.get();

        return objectId;
    }

    public static Long generateMoveId() {

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
}