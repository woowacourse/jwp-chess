package chess.dao;

import chess.domain.Color;
import chess.dto.BoardInfoDto;
import chess.dto.CreateBoardDto;
import java.util.List;

public interface BoardDao {

    int makeBoard(Color color, CreateBoardDto boardInfoDto);

    List<BoardInfoDto> getAllBoardInfo();

    void updateTurn(Color turn, int id);

    Color findTurn(int id);

    String getName(int id);

    void deleteBoard(int id, String password);

    boolean isGameEnd(int id);

    void end(int id);

    String getPassword(int id);
}
