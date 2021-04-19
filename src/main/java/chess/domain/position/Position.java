package chess.domain.position;

import static chess.domain.color.type.TeamColor.BLACK;
import static chess.domain.position.type.Rank.SEVEN;
import static chess.domain.position.type.Rank.TWO;

import chess.domain.color.type.TeamColor;
import chess.domain.piece.type.Direction;
import chess.domain.position.cache.PositionsCache;
import chess.domain.position.type.File;
import chess.domain.position.type.Rank;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Position {

    private static final int FILE_INDEX = 0;
    private static final int RANK_INDEX = 1;

    private final File file;
    private final Rank rank;

    public Position(String fileValue, String rankValue) {
        file = File.of(fileValue);
        rank = Rank.of(rankValue);
    }

    public Position(File file, Rank rank) {
        this.file = file;
        this.rank = rank;
    }

    public static Position of(File file, Rank rank) {
        return PositionsCache.find(file, rank);
    }

    public static Position of(String position) {
        String file = String.valueOf(position.charAt(FILE_INDEX));
        String rank = String.valueOf(position.charAt(RANK_INDEX));
        return Position.of(File.of(file), Rank.of(rank));
    }

    public Direction calculateDirection(Position destination) {
        File destinationFile = destination.getFile();
        Rank destinationRank = destination.getRank();
        int fileDiff = destinationFile.getOrder() - file.getOrder();
        int rankDiff = destinationRank.getValue() - rank.getValue();
        return Direction.of(fileDiff, rankDiff);
    }

    public Position moveTo(Direction direction) {
        return Position.of(file.getMovedFile(direction), rank.getMovedRank(direction));
    }

    public boolean isRankForwardedBy(Position destination, int rankDiff) {
        return rank.isDiff(destination.getRank(), rankDiff) && file.isSameAs(destination.getFile());
    }

    public boolean isFirstPawnPosition(TeamColor teamColor) {
        if (teamColor == BLACK) {
            return rank == SEVEN;
        }
        return rank == TWO;
    }

    public File getFile() {
        return file;
    }

    public Rank getRank() {
        return rank;
    }

    public static List<Position> getAllPositionsInOrder() {
        List<Position> positions = new ArrayList<>();
        List<Rank> reversedRanks = Rank.reversedRanks();
        for (Rank rank : reversedRanks) {
            addPositionOfRank(positions, rank);
        }
        return positions;
    }

    private static void addPositionOfRank(List<Position> positions, Rank rank) {
        for (File file : File.values()) {
            positions.add(Position.of(file, rank));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Position)) {
            return false;
        }
        Position that = (Position) o;
        return getFile() == that.getFile() && getRank() == that.getRank();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFile(), getRank());
    }
}
