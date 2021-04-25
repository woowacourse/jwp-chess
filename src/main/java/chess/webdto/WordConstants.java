package chess.webdto;

public class WordConstants {
    public static final String WHITE = "white";
    public static final String BLACK = "black";

    public static String convert(boolean whiteTeamTurn) {
        if(whiteTeamTurn){
            return WordConstants.WHITE;
        }
        return WordConstants.BLACK;
    }
}
