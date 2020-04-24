package wooteco.chess.domain.result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import wooteco.chess.domain.board.Line;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.EmptyPiece;
import wooteco.chess.domain.piece.GamePiece;
import wooteco.chess.domain.player.PlayerColor;

public class ChessResult {

    private static final int MINIMUM_PAWN_COUNT = 2;

    private Map<PlayerColor, Score> result;

    public ChessResult(Map<PlayerColor, Score> result) {
        this.result = result;
    }

    public static ChessResult from(Map<Position, GamePiece> board) {
        return new ChessResult(calculateScore(board));
    }

    private static Map<PlayerColor, Score> calculateScore(Map<Position, GamePiece> board) {
        Map<PlayerColor, Score> scores = new HashMap<>();
        List<GamePiece> gamePieces = new ArrayList<>(board.values());

        Map<GamePiece, Integer> gameWhitePiecesCount = getGamePieceCount(gamePieces, PlayerColor.WHITE);
        Map<GamePiece, Integer> gameBlackPiecesCount = getGamePieceCount(gamePieces, PlayerColor.BLACK);

        int sameColumnWhitePawnCount = getSameColumnPawnCount(board, PlayerColor.WHITE);
        int sameColumnBlackPawnCount = getSameColumnPawnCount(board, PlayerColor.BLACK);

        scores.put(PlayerColor.WHITE, Score.of(gameWhitePiecesCount, sameColumnWhitePawnCount));
        scores.put(PlayerColor.BLACK, Score.of(gameBlackPiecesCount, sameColumnBlackPawnCount));

        return scores;
    }

    private static Map<GamePiece, Integer> getGamePieceCount(List<GamePiece> gamePieces, PlayerColor playerColor) {
        return gamePieces.stream()
                .distinct()
                .filter(gamePiece -> gamePiece != EmptyPiece.getInstance())
                .filter(gamePiece -> gamePiece.isSame(playerColor))
                .collect(Collectors.toMap(gamePiece -> gamePiece,
                        gamePiece -> Collections.frequency(gamePieces, gamePiece)));
    }

    private static int getSameColumnPawnCount(Map<Position, GamePiece> board, PlayerColor playerColor) {
        return Line.listByColumn(board).stream()
                .mapToInt(column -> column.countPawnOf(playerColor))
                .filter(count -> count >= MINIMUM_PAWN_COUNT)
                .sum();
    }

    public Score getScore(PlayerColor playerColor) {
        return result.get(playerColor);
    }

    public Map<PlayerColor, Score> getResult() {
        return Collections.unmodifiableMap(result);
    }
}
