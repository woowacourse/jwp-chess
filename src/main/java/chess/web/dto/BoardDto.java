package chess.web.dto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import chess.domain.GameState;
import chess.domain.board.Board;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.repository.entity.PieceEntity;

public class BoardDto {

    private final int boardId;
    private final GameStateDto state;
    private final List<PieceEntity> pieces;

    public BoardDto(int boardId, Board board) {
        this.boardId = boardId;

        GameState state = GameState.from(board);
        this.state = new GameStateDto(state.name().toLowerCase(), state.getTurn());

        Map<Position, Piece> pieces = board.getPieces();
        this.pieces = pieces.keySet().stream()
                .map(position -> PieceEntity.from(position, pieces.get(position)))
                .collect(Collectors.toList());;
    }

    public int getBoardId() {
        return boardId;
    }

    public GameStateDto getState() {
        return state;
    }

    public List<PieceEntity> getPieces() {
        return pieces;
    }
}
