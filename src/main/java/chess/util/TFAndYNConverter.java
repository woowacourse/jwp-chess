package chess.util;

import java.util.Objects;

public class TFAndYNConverter {

    public static String convertYN(boolean changer) {
        return changer ? "Y" : "N";
    }

    public static boolean convertTF(String changer) {
        Objects.requireNonNull(changer, "바뀔 대상은 null일 수 없습니다.");
        if (changer.equalsIgnoreCase("Y")) {
            return true;
        }
        if (changer.equalsIgnoreCase("N")) {
            return false;
        }
        throw new IllegalArgumentException("YN이 아닙니다.");
    }
}
