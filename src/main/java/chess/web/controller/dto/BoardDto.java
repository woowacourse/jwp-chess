package chess.web.controller.dto;

import chess.domain.board.Board;
import chess.domain.board.piece.Piece;

import java.util.Map;
import java.util.stream.Collectors;

public class BoardDto {

    private final Long id;
    private final String turn;
    private final Map<String, String> board;
    private final boolean finish;

    public BoardDto(Long id, String turn, Map<String, String> board, boolean finish) {
        this.id = id;
        this.turn = turn;
        this.board = board;
        this.finish = finish;
    }

    public static BoardDto from(Long id, Board board) {
        Map<String, String> collect = board.getPieces().getPieces().stream()
                .collect(Collectors.toMap(
                        piece -> piece.getPosition().name(),
                        Piece::getName
                ));
        String turn = board.getTurn()
                .getTeam()
                .value();
        return new BoardDto(id, turn, collect, board.isDeadKing());
    }

    public Map<String, String> getBoard() {
        return board;
    }

    public String getTurn() {
        return turn;
    }

    public boolean isFinish() {
        return finish;
    }
}
