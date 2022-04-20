package springchess.model.piece;

import springchess.model.square.File;
import springchess.model.square.Rank;
import springchess.model.square.Square;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Initializer {

    private static final int LINE_RANGE = 8;

    public static Map<Square, Piece> initialize() {
        Map<Square, Piece> startingMembers = new HashMap<>();
        final List<File> files = Arrays.asList(File.values());

        initMajorPieces(Team.WHITE, Rank.ONE, files, startingMembers);
        initMajorPieces(Team.BLACK, Rank.EIGHT, files, startingMembers);
        initPawns(Team.WHITE, Rank.TWO, files, startingMembers);
        initPawns(Team.BLACK, Rank.SEVEN, files, startingMembers);
        initEmpty(startingMembers);

        return startingMembers;
    }


    private static void initMajorPieces(Team team, Rank rank, List<File> files,
                                        Map<Square, Piece> startingMembers) {
        List<Piece> majorPiecesLineup = majorPiecesLineup(team);
        for (int i = 0; i < majorPiecesLineup.size(); i++) {

            startingMembers.put(Square.of(files.get(i), rank), majorPiecesLineup.get(i));
        }
    }

    private static void initPawns(Team team, Rank rank, List<File> files,
                                  Map<Square, Piece> startingMembers) {
        for (int i = 0; i < LINE_RANGE; i++) {
            startingMembers.put(Square.of(files.get(i), rank), new Pawn(team));
        }
    }

    private static List<Piece> majorPiecesLineup(final Team team) {
        return List.of(
                new Rook(team),
                new Knight(team),
                new Bishop(team),
                new Queen(team),
                new King(team),
                new Bishop(team),
                new Knight(team),
                new Rook(team)
        );
    }

    private static void initEmpty(Map<Square, Piece> startingMembers) {
        for (Rank rank : Rank.values()) {
            fillSquareByFile(rank, startingMembers);
        }
    }

    private static void fillSquareByFile(Rank rank,
                                         Map<Square, Piece> startingMembers) {
        for (File file : File.values()) {
            Square square = Square.of(file, rank);
            checkEmpty(square, startingMembers);
        }
    }

    private static void checkEmpty(Square square,
                                   Map<Square, Piece> startingMembers) {
        if (!startingMembers.containsKey(square)) {
            startingMembers.put(square, new Empty());
        }
    }

}
