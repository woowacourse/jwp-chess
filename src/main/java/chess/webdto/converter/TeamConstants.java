package chess.webdto.converter;

public class TeamConstants {
    public static final String WHITE = "white";
    public static final String BLACK = "black";

    public static String convert(boolean whiteTeamTurn) {
        if(whiteTeamTurn){
            return TeamConstants.WHITE;
        }
        return TeamConstants.BLACK;
    }
}
