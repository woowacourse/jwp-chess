package chess.player;

import chess.exception.InvalidConstructorValueException;
import chess.location.Col;
import chess.location.Location;
import chess.piece.type.Piece;
import chess.score.Score;
import chess.team.Team;

import java.util.Map;
import java.util.Objects;

public class ChessSet {
    private final Map<Location, Piece> chessSet;

    public ChessSet(Map<Location, Piece> chessSet) {
        validNullValue(chessSet);
        this.chessSet = chessSet;
    }

    private void validNullValue(Map<Location, Piece> chessSet) {
        if (Objects.isNull(chessSet) || chessSet.containsKey(null) || chessSet.containsValue(null)) {
            throw new InvalidConstructorValueException();
        }
    }

    public Score calculateScoreExceptPawnReduce() {
        double sum = chessSet.values().stream()
                .map(Piece::getScore)
                .mapToDouble(Score::getValue)
                .sum();

        return new Score(sum);
    }

    public void remove(Location location) {
        chessSet.remove(location);
    }

    public boolean hasNotKing() {
        return chessSet.values().stream()
                .noneMatch(Piece::isKing);
    }

    public void movePiece(Location location, Location after) {
        chessSet.put(after, chessSet.remove(location));
    }

    public int calculateExistPawnSizeInSame(Col col) {
        int count = 0;
        for (Map.Entry<Location, Piece> element : chessSet.entrySet()) {
            if (element.getKey().isSame(col)
                    && element.getValue().isSameTeam(getTeam())
                    && element.getValue().isPawn()
            ) {
                count++;
            }
        }
        return count;
    }

    public Team getTeam() {
        Piece piece = chessSet.values().stream()
                .filter(Objects::nonNull)
                .findAny()
                .get();

        return piece.getTeam();
    }
}
