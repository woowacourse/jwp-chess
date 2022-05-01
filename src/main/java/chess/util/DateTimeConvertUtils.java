package chess.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DateTimeConvertUtils {

    public static Timestamp toTimestampFrom(LocalDateTime localDateTime) {
        return Timestamp.valueOf(localDateTime);
    }

    public static LocalDateTime toLocalDateTimeFrom(Timestamp timestamp) {
        return new Timestamp(timestamp.getTime()).toLocalDateTime();
    }
}
