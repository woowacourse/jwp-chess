package util;

import java.util.Objects;

public class NullChecker {

    private NullChecker() {
    }

    public static void validateNotNull(Object... objects) {
        for (int i = 0; i < objects.length; i++) {
            Objects.requireNonNull(objects[i], i + "번째 인자에 Null이 사용되었습니다.");
        }
    }
}
