package chess.domain.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import chess.domain.chess.Chess;
import chess.domain.chess.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceDto;
import chess.domain.position.Position;

public class BoardDto {

    private static final char START_FILE_CHARACTER = 'a';

    private final double blackScore;
    private final double whiteScore;
    private final List<PieceDto> pieceDtos;

    public BoardDto(Board board, List<PieceDto> pieceDtos) {
        this(board.score(Color.WHITE), board.score(Color.BLACK), pieceDtos);
    }

    public BoardDto(double blackScore, double whiteScore, List<PieceDto> pieceDtos) {
        this.blackScore = blackScore;
        this.whiteScore = whiteScore;
        this.pieceDtos = pieceDtos;
    }

    public static BoardDto from(Chess chess) {
        return from(chess.getBoard());
    }

    public static BoardDto from(final Board board) {
        final List<PieceDto> pieceDtos = new ArrayList<>();
        final Map<Position, Piece> boardMap = board.getBoard();
        for (Map.Entry<Position, Piece> entry : boardMap.entrySet()) {
            String position = getPosition(entry);
            String color = entry.getValue()
                                .getColor()
                                .name();

            String name = entry.getValue()
                               .getName();
            pieceDtos.add(new PieceDto(position, color, name));
        }

        return new BoardDto(board, pieceDtos);
    }

    public static BoardDto from(final List<PieceDto> pieceDtos) {
        Board board = Board.from(pieceDtos);
        return new BoardDto(board, pieceDtos);
    }

    private static String getPosition(Map.Entry<Position, Piece> entry) {
        char file = (char) (entry.getKey().getX() + START_FILE_CHARACTER);
        int rank = entry.getKey().getY() + 1;
        return Character.toString(file) + rank;
    }

    public double getBlackScore() {
        return blackScore;
    }

    public double getWhiteScore() {
        return whiteScore;
    }

    public List<PieceDto> getPieceDtos() {
        return pieceDtos;
    }
}
