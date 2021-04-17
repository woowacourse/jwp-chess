package chess.domain.position;

public class MovePositionVo {

    private static final char START_FILE_CHARACTER = 'a';
    private static final char START_RANK_CHARACTER = '1';

    private final String source;
    private final String target;

    public MovePositionVo(String source, String target) {
        this.source = source;
        this.target = target;
    }

    public static MovePositionVo from(MovePosition movePosition) {
        final String source = toRankAndFile(movePosition.getSourcePosition());
        final String target = toRankAndFile(movePosition.getTargetPosition());
        return new MovePositionVo(source, target);
    }

    private static String toRankAndFile(Position position) {
        char file = (char) (position.getX() + START_FILE_CHARACTER);
        char rank = (char) (position.getY() + START_RANK_CHARACTER);
        return Character.toString(file) + rank;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }
}
