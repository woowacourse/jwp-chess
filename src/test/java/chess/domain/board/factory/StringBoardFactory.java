package chess.domain.board.factory;

import chess.domain.board.position.Position;
import chess.domain.piece.Piece;
import chess.domain.piece.factory.PieceFactory;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringBoardFactory extends BoardFactory {

    private static final int BOARD_RANK_SIZE = 8;
    private static final int BOARD_FILE_SIZE = 8;
    private static final String EMPTY_SPACE_SYMBOL = ".";
    private static final List<String> UPPER_CASE_PIECE_SYMBOLS
            = List.of("R", "N", "B", "Q", "K", "P", EMPTY_SPACE_SYMBOL);

    static final String INVALID_RANK_SIZE_MESSAGE = "RANK 크기가 올바르지 않습니다.";
    static final String INVALID_FILE_SIZE_MESSAGE = "FILE 크기가 올바르지 않습니다.";
    static final String NOW_ALLOWED_SYMBOL_MESSAGE = "허용되지 않는 문자가 있습니다";

    private final List<String> stringChessBoard;

    private StringBoardFactory(List<String> stringChessBoard) {
        this.stringChessBoard = stringChessBoard;
    }

    public static BoardFactory getInstance(List<String> stringChessBoard) {
        validate(stringChessBoard);

        return new StringBoardFactory(stringChessBoard);
    }

    private static void validate(List<String> stringChessBoard) {
        validateRankSize(stringChessBoard);
        validateFileSize(stringChessBoard);
        validateSymbol(stringChessBoard);
    }

    private static void validateRankSize(List<String> stringChessBoard) {
        if (stringChessBoard.size() != BOARD_RANK_SIZE) {
            throw new IllegalArgumentException(INVALID_RANK_SIZE_MESSAGE);
        }
    }

    private static void validateFileSize(List<String> stringChessBoard) {
        boolean noneMatchFileSize = stringChessBoard
                .stream()
                .anyMatch(file -> file.length() != BOARD_FILE_SIZE);

        if (noneMatchFileSize) {
            throw new IllegalArgumentException(INVALID_FILE_SIZE_MESSAGE);
        }
    }

    private static void validateSymbol(List<String> stringChessBoard) {
        boolean noneMatchSymbol = stringChessBoard
                .stream()
                .flatMap(file -> Arrays.stream(file.split("")))
                .map(String::toUpperCase)
                .anyMatch(pieceSymbol -> !UPPER_CASE_PIECE_SYMBOLS.contains(pieceSymbol)
                        && !pieceSymbol.equals(EMPTY_SPACE_SYMBOL));

        if (noneMatchSymbol) {
            throw new IllegalArgumentException(NOW_ALLOWED_SYMBOL_MESSAGE);
        }
    }

    @Override
    public Map<Position, Piece> create() {

        Map<Position, Piece> board = new HashMap<>();

        makeBoardForRank(board);

        return board;
    }

    private void makeBoardForRank(Map<Position, Piece> board) {
        for (int rankCount = 1; rankCount <= stringChessBoard.size(); rankCount++) {
            makeBoardForFile(board, rankCount);
        }
    }

    private void makeBoardForFile(Map<Position, Piece> board, int rankCount) {

        String stringRank = stringChessBoard.get(BOARD_RANK_SIZE - rankCount);

        for (int fileCount = 1; fileCount <= stringRank.length(); fileCount++) {

            String pieceSymbol = String.valueOf(stringRank.charAt(fileCount - 1));
            Piece createdPiece = PieceFactory.create(pieceSymbol);
            Position createdPosition = Position.of(fileCount, rankCount);

            board.put(createdPosition, createdPiece);
        }
    }

    private boolean isUpperCase(String pieceSymbol) {
        return UPPER_CASE_PIECE_SYMBOLS.contains(pieceSymbol);
    }
}
