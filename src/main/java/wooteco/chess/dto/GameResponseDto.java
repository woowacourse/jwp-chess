package wooteco.chess.dto;

import wooteco.chess.domains.board.Board;
import wooteco.chess.domains.piece.Piece;
import wooteco.chess.domains.piece.PieceColor;

import java.util.List;
import java.util.stream.Collectors;

public class GameResponseDto {
    private static final String TURN_MSG_FORMAT = "%s의 순서입니다.";
    private static final String WINNING_MSG_FORMAT = "%s이/가 이겼습니다.";

    private Long roomId;
    private List<String> pieces;
    private String turn;
    private double whiteScore;
    private double blackScore;
    private String end;

    public GameResponseDto(Long roomId, Board board) {
        this.roomId = roomId;
        this.pieces = convertPieces(board);
        this.turn = String.format(TURN_MSG_FORMAT, board.getTeamColor());
        this.whiteScore = board.calculateScore(PieceColor.WHITE);
        this.blackScore = board.calculateScore(PieceColor.BLACK);
        if (board.isGameOver()) {
            this.end = String.format(WINNING_MSG_FORMAT, board.getTeamColor().changeTeam());
        }
    }

    private List<String> convertPieces(Board board) {
        return board.showBoard()
                .stream()
                .map(Piece::symbol)
                .collect(Collectors.toList());
    }

    public Long getRoomId() {
        return roomId;
    }

    public List<String> getPieces() {
        return pieces;
    }

    public String getTurn() {
        return turn;
    }

    public double getWhiteScore() {
        return whiteScore;
    }

    public double getBlackScore() {
        return blackScore;
    }

    public String getEnd() {
        return end;
    }
}
