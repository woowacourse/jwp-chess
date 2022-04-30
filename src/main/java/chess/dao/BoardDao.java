package chess.dao;

import chess.domain.Color;
import chess.domain.board.Board;
import chess.dto.BoardInfoDto;
import java.util.List;

public interface BoardDao {

    int makeBoard(Board board);

    List<BoardInfoDto> getAllBoardInfo();

    void updateTurn(Color turn, int id);

    Color findTurn(int id);

    String getName(int id);

    void deleteBoard(int id, String password);

    void end(int id);

    String getPassword(int id);
}
