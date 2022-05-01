package chess.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DateTimeConvertUtilsTest {

    @DisplayName("LocalDateTime 을 TimeStamp로 변환 후 다시 LocalDateTime으로 변환할 수 있다")
    @Test
    void Date_to_LocalDateTime() throws ParseException {
        LocalDateTime init = LocalDateTime.of(2022, 04, 19, 0, 0, 0);
        Timestamp timestamp = DateTimeConvertUtils.toTimestampFrom(init);
        LocalDateTime converted = DateTimeConvertUtils.toLocalDateTimeFrom(timestamp);
        assertThat(init).isEqualTo(converted);
    }
}
