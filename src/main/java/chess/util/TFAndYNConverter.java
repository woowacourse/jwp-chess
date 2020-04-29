package chess.util;

public class TFAndYNConverter {

    public static String convertYN(boolean changer) {
        return changer ? "Y" : "N";
    }

    public static boolean convertTF(String changer) {
        if (changer.equalsIgnoreCase("Y")) {
            return true;
        }
        if (changer.equalsIgnoreCase("N")) {
            return false;
        }
        throw new IllegalArgumentException("YN이 아닙니다.");
    }
}
