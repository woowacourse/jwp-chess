package chess.dao;

import chess.domain.board.Board;
import chess.dto.GameCreateRequest;
import chess.dto.GameDeleteResponse;
import chess.dto.GameDto;
import chess.dto.MoveRequest;
import java.util.List;

public interface ChessDao {
    int create(GameCreateRequest gameCreateRequest);

    List<GameDto> findAll();

    GameDto findById(int id);

    GameDeleteResponse deleteById(int id);

    Board findBoardByGameId(int id);

    void updateBoardByMove(MoveRequest moveRequest);

    void finishBoardById(int id);

    void changeTurnByGameId(int id);

    String findPasswordById(int id);
}
